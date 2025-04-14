package net.futureclient.eventbus;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.futureclient.eventbus.util.SneakyThrowUtil;

public final class EventBus {
   private final ObjectOpenHashSet<EventSubscribers> attachedSubscribers = new ObjectOpenHashSet<>();
   private final ObjectOpenHashSet<Object> registeredListeners = new ObjectOpenHashSet<>();
   private final WeakHashMap<Object, List<EventBus.SubscribedEventListener<?>>> listenerCache = new WeakHashMap<>();
   private Reference2ObjectOpenHashMap<Class<? extends Event>, EventBus.SubscribedEventListener<?>[]> eventsToSubscribedEventsToListener = new Reference2ObjectOpenHashMap();

   public synchronized EventSubscribers attach(EventSubscribers eventSubscribersToAttach) {
       if (!attachedSubscribers.contains(eventSubscribersToAttach)) {
           eventSubscribersToAttach.attachedToBus(this);
           attachedSubscribers.add(eventSubscribersToAttach);
       }

       return eventSubscribersToAttach;
   }

   public synchronized void detach(EventSubscribers eventSubscribersToDetach) {
      if (attachedSubscribers.contains(eventSubscribersToDetach)) {
         eventSubscribersToDetach.detachedFromBus(this);
         attachedSubscribers.remove(eventSubscribersToDetach);
      }
   }

   synchronized void flushCache(Object listener) {
      if (listener != null) {
         listenerCache.remove(listener);
      }
   }

   void register(Object listener) {
      if (listener != null) {
         if (listener instanceof Class) {
            Class<?> listenerClass = (Class<?>)listener;
            register0(null, listenerClass);
         } else {
            register0(listener, listener.getClass());
         }
      }
   }

   private synchronized void register0(@Nullable Object listener, Class<?> listenerClass) {
      Object parentListener = listener != null ? listener : listenerClass;
      if (registeredListeners.add(parentListener)) {
         List<EventBus.SubscribedEventListener<?>> cachedListeners = listenerCache
            .computeIfAbsent(
               parentListener,
               $ -> {
                  List<EventBus.SubscribedEventListener<?>> listeners = new ArrayList<>();
                  boolean staticEvents = listener == null;

                  for (Method method : listenerClass.getDeclaredMethods()) {
                     SubscribeEvent subscribeEvent = method.getAnnotation(SubscribeEvent.class);
                     if (subscribeEvent != null) {
                        Class<?>[] parameters = method.getParameterTypes();
                        if (parameters.length != 0) {
                           Class<?> parameter = parameters[0];
                           if (Event.class.isAssignableFrom(parameter)) {
                              boolean isStatic = Modifier.isStatic(method.getModifiers());
                              if ((staticEvents || !isStatic) && (!staticEvents || isStatic)) {
                                 Class<? extends Event> eventTypeParameterx = (Class<? extends Event>) parameter;

                                 ASMInvoker invoker;
                                 try {
                                    invoker = ASMInvoker.Generator.generate(listenerClass, eventTypeParameterx, method);
                                 } catch (Throwable var19) {
                                    handleCodeGenerationException(var19, listenerClass, (Class<? extends Event>) parameter, method);
                                    continue;
                                 }

                                 EventPriority priority = subscribeEvent.priority();
                                 boolean receiveCanceled = subscribeEvent.receiveCanceled();
                                 EventBus.SubscribedEventListener<?> subscribedEventListenerx;
                                 if (!staticEvents) {
                                    subscribedEventListenerx = new EventBus.SubscribedEventListener.Virtual(
                                       listener, priority, receiveCanceled, invoker, (Class<? extends Event>) parameter
                                    );
                                 } else {
                                    subscribedEventListenerx = new EventBus.SubscribedEventListener.Static(
                                       listenerClass, priority, receiveCanceled, invoker, (Class<? extends Event>) parameter
                                    );
                                 }

                                 listeners.add(subscribedEventListenerx);
                              }
                           }
                        }
                     }
                  }

                  return listeners;
               }
            );
         if (!cachedListeners.isEmpty()) {
            Reference2ObjectOpenHashMap<Class<? extends Event>, EventBus.SubscribedEventListener<?>[]> eventsToSubscribedEventsToListener = this.eventsToSubscribedEventsToListener
               .clone();

            for (EventBus.SubscribedEventListener<?> subscribedEventListener : cachedListeners) {
               Class<? extends Event> eventTypeParameter = subscribedEventListener.eventTypeParameter;
               EventBus.SubscribedEventListener<?>[] events = eventsToSubscribedEventsToListener.get(eventTypeParameter);
               EventBus.SubscribedEventListener<?>[] eventsCopy;
               if (events != null) {
                  eventsCopy = new EventBus.SubscribedEventListener[1 + events.length];
                  eventsCopy[0] = subscribedEventListener;
                  System.arraycopy(events, 0, eventsCopy, 1, events.length);
                  Arrays.sort(eventsCopy, null);
               } else {
                  eventsCopy = new EventBus.SubscribedEventListener[]{subscribedEventListener};
               }

               eventsToSubscribedEventsToListener.put(eventTypeParameter, eventsCopy);
            }

            this.eventsToSubscribedEventsToListener = eventsToSubscribedEventsToListener;
         }
      }
   }

   void unregister(Object listener) {
      if (listener != null) {
         if (listener instanceof Class) {
            Class<?> listenerClass = (Class<?>)listener;
            unregister0(null, listenerClass);
         } else {
            unregister0(listener, listener.getClass());
         }
      }
   }

   private synchronized void unregister0(@Nullable Object listener, Class<?> listenerClass) {
      Object parentListener = listener != null ? listener : listenerClass;
      if (registeredListeners.remove(parentListener)) {
         Reference2ObjectOpenHashMap<Class<? extends Event>, EventBus.SubscribedEventListener<?>[]> eventsToSubscribedEventsToListener = this.eventsToSubscribedEventsToListener
            .clone();
         Iterator<Entry<Class<? extends Event>, EventBus.SubscribedEventListener<?>[]>> iterator = eventsToSubscribedEventsToListener.entrySet().iterator();

         while (iterator.hasNext()) {
            Entry<Class<? extends Event>, EventBus.SubscribedEventListener<?>[]> entry = iterator.next();
            EventBus.SubscribedEventListener<?>[] subscribedEventListeners = entry.getValue();
            EventBus.SubscribedEventListener<?>[] copy = null;
            int copied = 0;

            for (EventBus.SubscribedEventListener<?> subscribedEventListener : subscribedEventListeners) {
               if (subscribedEventListener.listener != parentListener) {
                  if (copy == null) {
                     copy = new EventBus.SubscribedEventListener[subscribedEventListeners.length];
                  }

                  copy[copied++] = subscribedEventListener;
               }
            }

            if (copy == null) {
               iterator.remove();
            } else {
               EventBus.SubscribedEventListener<?>[] chopped = new EventBus.SubscribedEventListener[copied];
               System.arraycopy(copy, 0, chopped, 0, chopped.length);
               entry.setValue(chopped);
            }
         }

         this.eventsToSubscribedEventsToListener = eventsToSubscribedEventsToListener;
      }
   }

   public void transmit(Event event) {
      EventBus.SubscribedEventListener<?>[] subscribedEventListeners = eventsToSubscribedEventsToListener
         .get(event.getClass());
      if (subscribedEventListeners != null) {
         for (EventBus.SubscribedEventListener<?> subscribedEventListener : subscribedEventListeners) {
            if (subscribedEventListener.receiveCanceled || !(event instanceof EventCancelable) || !((EventCancelable)event).canceled) {
               try {
                  subscribedEventListener.invoker.invoke(subscribedEventListener.listener, event);
               } catch (Throwable var8) {
                  handleTransmitException(var8, subscribedEventListener.listener, event);
               }
            }
         }
      }
   }

   private void handleCodeGenerationException(Throwable t, Class<?> listenerClass, Class<? extends Event> eventClass, Method eventFunction) {
      SneakyThrowUtil.throwSneaky(t);
      throw null;
   }

   private void handleTransmitException(Throwable t, Object listener, Event event) {
      SneakyThrowUtil.throwSneaky(t);
      throw null;
   }

   public abstract static class SubscribedEventListener<T> implements Comparable<EventBus.SubscribedEventListener<?>> {
      public final T listener;
      public final EventPriority priority;
      public final boolean receiveCanceled;
      public final ASMInvoker invoker;
      public final Class<? extends Event> eventTypeParameter;

      SubscribedEventListener(T listener, EventPriority priority, boolean receiveCanceled, ASMInvoker invoker, Class<? extends Event> eventTypeParameter) {
         this.listener = listener;
         this.priority = priority;
         this.receiveCanceled = receiveCanceled;
         this.invoker = invoker;
         this.eventTypeParameter = eventTypeParameter;
      }

      public int compareTo(@Nonnull EventBus.SubscribedEventListener event) {
         if (event == this) {
            return 0;
         } else {
            return priority.compareTo(event.priority) >= 0 ? 1 : -1;
         }
      }

      static final class Static extends EventBus.SubscribedEventListener<Class<?>> {
         Static(Class<?> listenerClass, EventPriority priority, boolean receiveCanceled, ASMInvoker invoker, Class<? extends Event> eventTypeParameter) {
            super(listenerClass, priority, receiveCanceled, invoker, eventTypeParameter);
         }
      }

      static final class Virtual extends EventBus.SubscribedEventListener<Object> {
         Virtual(Object listener, EventPriority priority, boolean receiveCanceled, ASMInvoker invoker, Class<? extends Event> eventTypeParameter) {
            super(listener, priority, receiveCanceled, invoker, eventTypeParameter);
         }
      }
   }
}
