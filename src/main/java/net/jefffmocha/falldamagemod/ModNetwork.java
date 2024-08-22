package net.jefffmocha.falldamagemod;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.jefffmocha.falldamagemod.mixin.ExampleMixin;
import net.minecraft.util.Identifier;
import net.jefffmocha.falldamagemod.FallDamageMod;

public class ModNetwork {
    public static final Identifier FALL_DAMAGE_PACKET_ID = new Identifier(FallDamageMod.MOD_ID, "fall_damage");

    public static void sendFallDamagePacket() {
        ClientPlayNetworking.send(FALL_DAMAGE_PACKET_ID, PacketByteBufs.empty());
    }

    public static void registerServerReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(FALL_DAMAGE_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                // Cast to the interface to access the injected mixin methods
                if (player instanceof PreventFallDamageAccessor) {
                    ((PreventFallDamageAccessor) player).setPreventFallDamage(true);
                }
            });
        });
    }
}



