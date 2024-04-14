package mod.linguardium.spectralutilitiesfix.mixin;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.oyosite.ticon.specutils.config.CommonConfig;
import de.dafuqs.spectrum.blocks.jade_vines.JadeVineRootsBlock;
import de.dafuqs.spectrum.blocks.jade_vines.JadeVineRootsBlockEntity;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumItems;
import me.shedaniel.autoconfig.AutoConfig;
import mod.linguardium.spectralutilitiesfix.Logging;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JadeVineRootsBlock.class)
public class JadeVineRootsBlockMixinReplacement {
    @Unique
    private static final TagKey<Block> PROVIDES_MOONLIGHT_TO_JADE_VINES = TagKey.of(RegistryKeys.BLOCK, new Identifier("spectralutilitiesfix","moonlight_provider"));

    // minor changes to using a mutable blockpos instead of generating new ones
    // also uses isOf instead of == checks
    @Inject(
            method = {"onNaturesStaffUse(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)Z"},
            at = {@At("HEAD")}
    )
    void dropJadeJelly(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {

        if (AutoConfig.getConfigHolder(CommonConfig.class).getConfig().getJadeVinesDropJadeJellyWhenRevived()) {
            BlockPos.Mutable mutable = pos.mutableCopy();
            while(world.getBlockState(mutable).isOf(SpectrumBlocks.JADE_VINE_ROOTS)) {
                mutable.move(Direction.DOWN);
            }

            if (world.getBlockState(mutable).isOf(SpectrumBlocks.JADE_VINES)) {
                Vec3d c = mutable.toCenterPos();
                ItemScatterer.spawn(world, c.x, c.y, c.z, new ItemStack(SpectrumItems.JADE_JELLY, world.random.nextInt(3) + 3));
            }
        }
    }

    // Completely modified this logic.
    // Now uses a block tag for light providers instead of hardcoding block type.
    // Also uses the actual luminance of the block instead of specific block properties to allow for more mod compatibility
    // uses Local to get the jade vine roots BE instead of localcapture with an exception on the BE
    @ModifyExpressionValue(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getLightLevel(Lnet/minecraft/world/LightType;Lnet/minecraft/util/math/BlockPos;)I"),
            method = {"canGrow(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Z"}
    )
    int allowMoonstoneGrowLampToProvideLightForVines(int original, World world, BlockPos pos, @Local JadeVineRootsBlockEntity jadeVineRootsBlockEntity) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        int lightLevel = original;
        for (int i=0;i<15;i++) {
            mutable.move(Direction.UP);
            BlockState state = world.getBlockState(mutable);
            if (state.isIn(PROVIDES_MOONLIGHT_TO_JADE_VINES)) {
                lightLevel = Math.max(lightLevel, state.getLuminance() - i);
            }
            if (state.isOpaque()) break;
        }
        if (lightLevel < 0) lightLevel = 0;
        return lightLevel;
    }
}
