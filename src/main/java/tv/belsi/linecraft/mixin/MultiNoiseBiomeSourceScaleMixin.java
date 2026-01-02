package tv.belsi.linecraft.mixin;

import tv.belsi.linecraft.StripRules;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MultiNoiseBiomeSource.class)
public abstract class MultiNoiseBiomeSourceScaleMixin {
    // Scales the first int argument (x)
    @ModifyVariable(
            method = "getBiome",
            at = @At("HEAD"),
            ordinal = 0,
            argsOnly = true
    )
    private int linecraft$scaleX(int x) {
        return x * StripRules.BIOME_SCALE;
    }

    // Scales the third int argument (z)
    @ModifyVariable(
            method = "getBiome",
            at = @At("HEAD"),
            ordinal = 2,
            argsOnly = true
    )
    private int linecraft$scaleZ(int z) {
        return z * StripRules.BIOME_SCALE;
    }
}
