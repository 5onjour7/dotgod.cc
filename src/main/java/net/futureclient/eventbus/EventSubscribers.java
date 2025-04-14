package net.futureclient.eventbus;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public final class EventSubscribers {
   private final ObjectOpenHashSet<Object> listeners = new ObjectOpenHashSet<>();
   private final Object lock = new Object();
   private EventBus bus;

   public void register(Object listener) {
      synchronized (lock) {
         listeners.add(listener);
         EventBus bus = this.bus;

         if (bus != null) {
            bus.register(listener);
         }
      }
   }

   public void unregister(Object listener) {
      synchronized (lock) {
         listeners.remove(listener);
         EventBus bus = this.bus;
         
         if (bus != null) {
            bus.unregister(listener);
         }
      }
   }

   public EventBus bus() {
      return bus;
   }

   void attachedToBus(EventBus bus) {
      synchronized (lock) {
//         if (bus != null) {
//            throw new IllegalStateException("Can't be attached to multiple buses.");
//         } else {
//            this.bus = bus;
//
//            for (Object listener : listeners) {
//               bus.register(listener);
//            }
//         }

         this.bus = bus;

         for (Object listener : listeners) {
            bus.register(listener);
         }
      }
   }

   void detachedFromBus(EventBus bus) {
      synchronized (lock) {
         if (bus == null) {
            throw new IllegalStateException("Not attached to a bus.");
         } else {
            for (Object listener : listeners) {
               bus.unregister(listener);
               bus.flushCache(listener);
            }

            this.bus = null;
         }
      }
   }

   public void detach() {
      synchronized (lock) {
         EventBus currBus = bus;
         if (currBus != null) {
            currBus.detach(this);
         }

         bus = null;
      }
   }
}
