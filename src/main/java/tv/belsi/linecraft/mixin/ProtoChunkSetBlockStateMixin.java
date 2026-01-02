package tv.belsi.linecraft.mixin;

import tv.belsi.linecraft.StripRules;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ProtoChunk.class)
public abstract class ProtoChunkSetBlockStateMixin {

    @Inject(method = "setBlockState", at = @At("HEAD"), cancellable = true)
    private void linecraft$denyOutsideStrip(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<BlockState> cir) {
        if (StripRules.inStripZ(pos.getZ())) return;
        if (state.isAir()) return;

        // otherwise, continue as normal
        cir.setReturnValue(((ProtoChunk) (Object) this).getBlockState(pos));
    }
}
