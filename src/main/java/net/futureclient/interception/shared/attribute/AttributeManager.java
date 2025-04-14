package net.futureclient.interception.shared.attribute;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

public final class AttributeManager {
   private final Map<String, AttributeManager.Attribute<?>> registeredAttributes = new HashMap<>();
   private boolean dirty;

   public <T> AttributeManager.Attribute<T> register(String id, AttributeManager.Serializer<T> serializer, T defaultValue) {
      AttributeManager.Attribute<T> attribute = new AttributeManager.Attribute<>(this, serializer, defaultValue);
      registeredAttributes.put(id, attribute);

      return attribute;
   }

   public boolean isDirty() {
      return dirty;
   }

   public Map<String, AttributeManager.Attribute<?>> sync(boolean full) {
      if (!full && !dirty) {
         return Collections.emptyMap();
      } else {
         dirty = false;
         Map<String, AttributeManager.Attribute<?>> attributes = new HashMap<>();

         for (Entry<String, AttributeManager.Attribute<?>> entry : registeredAttributes.entrySet()) {
            String id = entry.getKey();
            AttributeManager.Attribute<?> attribute = entry.getValue();

            if (full || attribute.dirty) {
               attributes.put(id, attribute);
               attribute.dirty = false;
            }
         }

         return attributes;
      }
   }

   public byte[] writeAttributes(Map<String, AttributeManager.Attribute<?>> attributes) throws IOException {
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      DataOutputStream out = new DataOutputStream(bout);
      out.writeInt(attributes.size());

      for (Entry<String, AttributeManager.Attribute<?>> entry : attributes.entrySet()) {
         String id = entry.getKey();
         AttributeManager.Attribute<?> attribute = entry.getValue();
         out.writeUTF(id);
         attribute.write(out);
      }

      return bout.toByteArray();
   }

   public void readAttributes(byte[] bytes) throws IOException {
      DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
      int entries = in.readInt();

      for (int i = 0; i < entries; i++) {
         String id = in.readUTF();
         AttributeManager.Attribute attribute = registeredAttributes.get(id);
         if (attribute == null) {
            throw new IOException("Unable to read unregistered attribute " + id);
         }

         attribute.setValue0(attribute.read(in));
      }
   }

   public static final class Attribute<T> {
      private final AttributeManager parent;
      private final AttributeManager.Serializer<T> serializer;
      private T value;
      private boolean dirty = true;

      private Attribute(AttributeManager parent, AttributeManager.Serializer<T> serializer, T defaultValue) {
         this.parent = parent;
         this.serializer = serializer;
         this.setValue0(defaultValue);
      }

      private void write(DataOutputStream out) throws IOException {
         T value = this.value;

         if (value != null) {
            out.writeBoolean(true);
            serializer.write(out, value);
         } else {
            out.writeBoolean(false);
         }
      }

      private T read(DataInputStream in) throws IOException {
         return !in.readBoolean() ? null : serializer.read(in);
      }

      private void setValue0(T value) {
         this.value = value;
      }

      public void setValue(T value) {
         T prev = getValue();
         setValue0(value);

         if (!Objects.equals(prev, value)) {
            dirty = true;
            parent.dirty = true;
         }
      }

      public T getValue() {
         return value;
      }
   }

   public interface Serializer<T> {
      AttributeManager.Serializer<Byte> BYTE_SERIALIZER = new AttributeManager.Serializer<Byte>() {
         public void write(DataOutputStream out, Byte value) throws IOException {
            out.writeByte(value);
         }

         public Byte read(DataInputStream in) throws IOException {
            return in.readByte();
         }
      };

      AttributeManager.Serializer<Short> SHORT_SERIALIZER = new AttributeManager.Serializer<Short>() {
         public void write(DataOutputStream out, Short value) throws IOException {
            out.writeShort(value);
         }

         public Short read(DataInputStream in) throws IOException {
            return in.readShort();
         }
      };

      AttributeManager.Serializer<Integer> INTEGER_SERIALIZER = new AttributeManager.Serializer<Integer>() {
         public void write(DataOutputStream out, Integer value) throws IOException {
            out.writeInt(value);
         }

         public Integer read(DataInputStream in) throws IOException {
            return in.readInt();
         }
      };

      AttributeManager.Serializer<Long> LONG_SERIALIZER = new AttributeManager.Serializer<Long>() {
         public void write(DataOutputStream out, Long value) throws IOException {
            out.writeLong(value);
         }

         public Long read(DataInputStream in) throws IOException {
            return in.readLong();
         }
      };

      AttributeManager.Serializer<Float> FLOAT_SERIALIZER = new AttributeManager.Serializer<Float>() {
         public void write(DataOutputStream out, Float value) throws IOException {
            out.writeFloat(value);
         }

         public Float read(DataInputStream in) throws IOException {
            return in.readFloat();
         }
      };

      AttributeManager.Serializer<Double> DOUBLE_SERIALIZER = new AttributeManager.Serializer<Double>() {
         public void write(DataOutputStream out, Double value) throws IOException {
            out.writeDouble(value);
         }

         public Double read(DataInputStream in) throws IOException {
            return in.readDouble();
         }
      };

      AttributeManager.Serializer<Boolean> BOOLEAN_SERIALIZER = new AttributeManager.Serializer<Boolean>() {
         public void write(DataOutputStream out, Boolean value) throws IOException {
            out.writeBoolean(value);
         }

         public Boolean read(DataInputStream in) throws IOException {
            return in.readBoolean();
         }
      };

      AttributeManager.Serializer<String> STRING_SERIALIZER = new AttributeManager.Serializer<String>() {
         public void write(DataOutputStream out, String value) throws IOException {
            out.writeUTF(value);
         }

         public String read(DataInputStream in) throws IOException {
            return in.readUTF();
         }
      };

      void write(DataOutputStream var1, T var2) throws IOException;

      T read(DataInputStream var1) throws IOException;
   }
}
