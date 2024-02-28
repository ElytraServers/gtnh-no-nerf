package cn.taskeren.gtnn.mixinplugin;

import com.google.common.io.Files;

import java.nio.file.Path;
import java.util.Locale;

@SuppressWarnings({"UnstableApiUsage"})
public enum TargetedMod {

	GregTech("GregTech", "GregTech"),
	NewHorizonsCoreMod("GT: New Horizons", "GTNewHorizonsCoreMod"),
	GoodGenerator("GoodGenerator", "GoodGenerator"),
	TecTech("TecTech", "TecTech"),
	GTPlusPlus("GT++", "GTPlusPlus")
	;

	public final String modName;
	public final String jarNamePrefixLowercase;
	public final boolean shouldLoadInDev;

	TargetedMod(String modName, String jarNamePrefixAnyCase) {
		this(modName, jarNamePrefixAnyCase, true);
	}

	TargetedMod(String modName, String jarNamePrefixAnyCase, boolean shouldLoadInDev) {
		this.modName = modName;
		this.jarNamePrefixLowercase = jarNamePrefixAnyCase.toLowerCase(Locale.US);
		this.shouldLoadInDev = shouldLoadInDev;
	}

	public boolean isMatchingJar(Path path) {
		return Files.getNameWithoutExtension(path.toString()).toLowerCase().startsWith(jarNamePrefixLowercase)
			&& Files.getFileExtension(path.toString()).equals("jar");
	}

}
