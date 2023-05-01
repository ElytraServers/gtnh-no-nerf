package cn.taskeren.gtnn.mixinplugin;

import com.google.common.io.Files;

import java.nio.file.Path;

@SuppressWarnings({"UnstableApiUsage"})
public enum TargetedMod {

	GregTech("GregTech", "GregTech"),
	NewHorizonsCoreMod("GT: New Horizons", "GTNewHorizonsCoreMod")
	;

	public final String modName;
	public final String jarNamePrefixLowercase;
	public final boolean shouldLoadInDev;

	TargetedMod(String modName, String jarNamePrefixAnyCase) {
		this(modName, jarNamePrefixAnyCase, true);
	}

	TargetedMod(String modName, String jarNamePrefixLowercase, boolean shouldLoadInDev) {
		this.modName = modName;
		this.jarNamePrefixLowercase = jarNamePrefixLowercase;
		this.shouldLoadInDev = shouldLoadInDev;
	}

	public boolean isMatchingJar(Path path) {
		return Files.getNameWithoutExtension(path.toString()).toLowerCase().startsWith(jarNamePrefixLowercase)
			&& Files.getFileExtension(path.toString()).equals("jar");
	}

}
