package tv.belsi.linecraft.mixin;

import tv.belsi.linecraft.StripRules;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.rule.GameRules;
import net.minecraft.world.chunk.ChunkLoadProgress;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerSpawnMixin {

    @Inject(
            method = "setupSpawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/level/ServerWorldProperties;ZZLnet/minecraft/world/chunk/ChunkLoadProgress;)V",
            at = @At("HEAD")
    )
    private static void linecraft$forceSpawn(
            ServerWorld world,
            ServerWorldProperties worldProperties,
            boolean bonusChest,
            boolean debugWorld,
            ChunkLoadProgress chunkLoadProgress,
            CallbackInfo ci
    ) {
        // Only change overworld spawn
        if (!world.getRegistryKey().getValue().toString().equals("minecraft:overworld")) return;

        // Set respawn radius to half of the strip width
        world.getGameRules().setValue(GameRules.RESPAWN_RADIUS, StripRules.HALF, null);

        int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, 0, 0);
        var sp = new WorldProperties.SpawnPoint(new net.minecraft.util.math.GlobalPos(World.OVERWORLD, new BlockPos(0, y, 0)), StripRules.YAW, 0.0f);
        world.setSpawnPoint(sp);

        worldProperties.setSpawnPoint(sp);
    }
}
