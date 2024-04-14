package mod.linguardium.spectralutilitiesfix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Logging {
    public static final Logger LOGGER = LoggerFactory.getLogger("Spectral Utilities Fix");
    public static void print(String msg) {
        LOGGER.info("[{}] {}",LOGGER.getName(),msg);
    }
}
