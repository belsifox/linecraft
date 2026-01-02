package tv.belsi.linecraft.mixin;

import tv.belsi.linecraft.StripRules;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.noise.NoiseConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

// This keeps vanilla biomes/noise exactly and only wipes blocks after generation.
@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin {
    @Unique
    private static final BlockState AIR = Blocks.AIR.getDefaultState();


    @Inject(
            method = "populateNoise(Lnet/minecraft/world/gen/chunk/Blender;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/chunk/Chunk;)Ljava/util/concurrent/CompletableFuture;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void linecraft$wipeAfterPopulateNoise_noexec(
            Blender blender,
            NoiseConfig noiseConfig,
            StructureAccessor structureAccessor,
            Chunk chunk,
            CallbackInfoReturnable<CompletableFuture<Chunk>> cir
    ) {
        CompletableFuture<Chunk> original = cir.getReturnValue();
        cir.setReturnValue(original.thenApply(NoiseChunkGeneratorMixin::wipeOutsideStrip));
    }

    @Unique
    private static Chunk wipeOutsideStrip(Chunk chunk) {
        ChunkPos cp = chunk.getPos();
        int bottomY = chunk.getBottomY();
        int topY = chunk.getTopYInclusive();

        BlockPos.Mutable pos = new BlockPos.Mutable();

        for (int localX = 0; localX < 16; localX++) {
            int worldX = cp.getStartX() + localX;

            for (int localZ = 0; localZ < 16; localZ++) {
                int worldZ = cp.getStartZ() + localZ;

                if (StripRules.inStripZ(worldZ)) continue;

                for (int y = bottomY; y <= topY; y++) {
                    pos.set(worldX, y, worldZ);
                    chunk.setBlockState(pos, AIR, 0);
                }
            }
        }
        return chunk;
    }
}
