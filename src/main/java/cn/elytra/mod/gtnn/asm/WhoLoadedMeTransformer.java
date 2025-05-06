package cn.elytra.mod.gtnn.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A dummy class transformer used to find which part of code triggers the classloading of the given targets.
 * <p>
 * Set targets by adding them to {@code GTNN_WhoLoadedMe} environment with semicolon({@code ;}) as separator.
 */
public class WhoLoadedMeTransformer implements IClassTransformer {

	private static final Logger LOG = LogManager.getLogger("GTNN Who the F**K Loaded Me");

	public static final Set<String> TARGET_CLASSES = new HashSet<>();

	static {
		try {
			String targetProperty = System.getenv("GTNN_WhoLoadedMe");
			if(targetProperty != null) {
				String[] targets = targetProperty.split(";");
				TARGET_CLASSES.addAll(Arrays.asList(targets));
				LOG.info("Loaded targets from env \"GTNN_WhoLoadedMe\": {}", Arrays.toString(targets));
			}
		}catch(Exception e) {
			LOG.error("Failed to initialize WhoLoadedMe from envs.");
		}
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(TARGET_CLASSES.contains(transformedName)) {
			LOG.info("Hit target {}", transformedName, new Exception("Gotcha!"));
		}
		return basicClass;
	}

}
