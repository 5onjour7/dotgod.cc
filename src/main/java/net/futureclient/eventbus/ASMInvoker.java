package net.futureclient.eventbus;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import net.futureclient.eventbus.util.SneakyThrowUtil;
import net.futureclient.eventbus.util.TryUtil;
import net.futureclient.eventbus.util.UnsafeUtil;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

public interface ASMInvoker {
   void invoke(Object var1, Event var2);

   public static final class Generator {
      private static final Method INVOKE_METHOD = Objects.requireNonNull(
         TryUtil.tryOrElseNull(() -> ASMInvoker.class.getDeclaredMethod("invoke", Object.class, Event.class)), "Failed to find method"
      );
      private static final String INVOKE_METHOD_NAME = INVOKE_METHOD.getName();
      private static final Type INVOKE_METHOD_TYPE = Type.getType(INVOKE_METHOD);
      private static final Type INVOKER_TYPE = Type.getType(ASMInvoker.class);
      private static final AtomicLong GENERATED_COUNT = new AtomicLong(0L);

      static ASMInvoker generate(Class<?> listenerClass, Class<? extends Event> eventClass, Method eventFunction) {
         Type listenerType = Type.getType(listenerClass);
         String eventFunctionName = eventFunction.getName();
         Type eventFunctionType = Type.getType(eventFunction);
         Type[] types = eventFunctionType.getArgumentTypes();
         
         if (types.length == 0) {
            throw new IllegalStateException("Empty argument types: " + eventFunctionName);
         } else {
            Type eventArgumentType = types[0];
            
            String listenerByteCodeName = listenerType.getInternalName();
            String byteCodeName = listenerByteCodeName + "$$Generated$" + eventClass.getSimpleName() + "$" + GENERATED_COUNT.incrementAndGet();
            String javaName = byteCodeName.replace("/", ".");
            
            ClassNode cn = new ClassNode();
            cn.visit(52, 49, byteCodeName, null, "java/lang/Object", new String[]{INVOKER_TYPE.getInternalName()});
            MethodVisitor mv = cn.visitMethod(1, "<init>", "()V", null, null);
            
            mv.visitCode();
            mv.visitVarInsn(25, 0);
            mv.visitMethodInsn(183, cn.superName, "<init>", "()V", false);
            mv.visitInsn(177);
            mv.visitEnd();
            
            boolean isStaticEvent = Modifier.isStatic(eventFunction.getModifiers());
            MethodVisitor mvx = cn.visitMethod(17, INVOKE_METHOD_NAME, INVOKE_METHOD_TYPE.getDescriptor(), null, null);
            mvx.visitCode();
            
            if (!isStaticEvent) {
               mvx.visitVarInsn(25, 1);
               mvx.visitTypeInsn(192, listenerByteCodeName);
            }

            mvx.visitVarInsn(25, 2);
            mvx.visitTypeInsn(192, eventArgumentType.getInternalName());
            mvx.visitMethodInsn(isStaticEvent ? 184 : 182, listenerByteCodeName, eventFunctionName, eventFunctionType.getDescriptor(), false);
            mvx.visitInsn(177);
            mvx.visitEnd();
            cn.visitEnd();
            
            ClassWriter cw = new ClassWriter(3);
            cn.accept(cw);
            byte[] generatedBytes = cw.toByteArray();

            try {
               Class<? extends ASMInvoker> invokerClass;
               
               if (Modifier.isPublic(listenerClass.getModifiers()) && Modifier.isPublic(eventFunction.getModifiers())) {
                  invokerClass = new ASMInvoker.Generator.InvokerClassLoader(listenerClass).defineClass(javaName, generatedBytes);
               } else {
                  invokerClass = defineAnonymousClass(listenerClass, generatedBytes);
               }

               return invokerClass.newInstance();
            } catch (IllegalAccessException | InstantiationException var15) {
               SneakyThrowUtil.throwSneaky(var15);
               throw null;
            }
         }
      }

      private static Class<? extends ASMInvoker> castVerify(Class<?> clazz) {
         if (!ASMInvoker.class.isAssignableFrom(clazz)) {
            throw new IllegalStateException(clazz + " is not assignable from " + ASMInvoker.class.getSimpleName());
         } else {
            return (Class<? extends ASMInvoker>) clazz;
         }
      }

      private static Class<? extends ASMInvoker> defineAnonymousClass(Class<?> listenerClass, byte[] bytes) {
         return castVerify(UnsafeUtil.defineAnonymousClass(listenerClass, bytes));
      }

      private static final class InvokerClassLoader extends ClassLoader {
         InvokerClassLoader(Class<?> listenerClass) {
            super(listenerClass.getClassLoader());
         }

         Class<? extends ASMInvoker> defineClass(String name, byte[] bytes) {
            return ASMInvoker.Generator.castVerify(defineClass(name, bytes, 0, bytes.length));
         }
      }
   }
}
