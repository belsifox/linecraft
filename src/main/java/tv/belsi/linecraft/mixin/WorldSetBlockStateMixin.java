package tv.belsi.linecraft.mixin;

import tv.belsi.linecraft.StripRules;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldSetBlockStateMixin {
    @Inject(
            method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void linecraft$blockOutsideStrip_3args(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<Boolean> cir) {
        denyNonAirOutsideStrip(pos, state, cir);
    }

    @Inject(
            method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void linecraft$blockOutsideStrip_4args(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        denyNonAirOutsideStrip(pos, state, cir);
    }

    @Unique
    private static void denyNonAirOutsideStrip(BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (StripRules.inStripZ(pos.getZ())) return;

        // Allow clearing/removal outside strip.
        if (state.isAir()) return;

        // Otherwise deny
        cir.setReturnValue(false);
    }
}
