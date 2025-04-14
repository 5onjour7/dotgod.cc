package me.eclipcen.butterflyclient.launch.mixin.client.multiplayer;

import me.eclipcen.butterflyclient.Butterfly;
import me.eclipcen.butterflyclient.event.world.WorldEvent;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public class MixinWorldClient {

   @Inject(
      method = "<init>",
      at = @At("RETURN")
   )
   public void onInit(CallbackInfo callbackInfo) {
      WorldClient worldClient = (WorldClient) (Object) this;
      Butterfly.getEventBus().transmit(new WorldEvent.Load(worldClient));
   }
}
