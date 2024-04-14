package mod.linguardium.spectralutilitiesfix;

import com.bawnorton.mixinsquared.api.MixinCanceller;
import mod.linguardium.spectralutilitiesfix.mixin.CommonMixinPlugin;

import java.util.List;

public class SpectralUtilitiesMixinCanceller implements MixinCanceller {
    @Override
    public boolean shouldCancel(List<String> targetClassNames, String mixinClassName) {
        if (mixinClassName.equals("com.oyosite.ticon.specutils.mixin.JadeVineRootsBlockMixin")) {
            Logging.print("Removing mixin from Spectral Utilities due to bug");
            return true;
        }
        return false;
    }
}
