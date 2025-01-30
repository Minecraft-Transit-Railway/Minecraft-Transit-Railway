package org.mtr.mod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.mod.blocks.BlockMTR;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class BlockBreakMixin {
    
    @Inject(method = "tryBreakBlock", at = @At("HEAD"), cancellable = true)
    private void preventMtrBlockBreak(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerInteractionManager manager = (ServerPlayerInteractionManager) (Object) this;
        ServerPlayerEntity player = manager.player;
        World world = player.getWorld();
        BlockState state = world.getBlockState(pos);
        
        // Prevent breaking if block is an MTR-related block and player is NOT in creative mode
        if (state.getBlock() instanceof BlockMTR && !player.isCreative()) {
            cir.setReturnValue(false);  // Cancel block breaking
        }
    }
}
