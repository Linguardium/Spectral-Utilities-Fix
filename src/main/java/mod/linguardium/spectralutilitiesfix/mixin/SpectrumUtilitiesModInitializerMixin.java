package mod.linguardium.spectralutilitiesfix.mixin;

import com.oyosite.ticon.specutils.SpectralUtilities;
import com.oyosite.ticon.specutils.block.BlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SpectralUtilities.class, remap = false)
public class SpectrumUtilitiesModInitializerMixin {
    // force the BE class to initialize on load
    // fixes https://github.com/eehunter/Spectral-Utilities/issues/2
    @Inject(method="onInitialize", at= @At(value = "INVOKE", target = "Lcom/oyosite/ticon/specutils/block/BlockRegistry;invoke()V", shift = At.Shift.AFTER))
    private void initializeBlockEntities(CallbackInfo ci) {
        BlockRegistry.BlockEntities.INSTANCE.toString();
    }
}
