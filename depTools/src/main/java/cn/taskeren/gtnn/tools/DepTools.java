package cn.taskeren.gtnn.tools;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class DepTools {

	public static final String INST_MODS_PATH_ENV = System.getenv("INST_MODS_PATH");

	@SuppressWarnings("SpellCheckingInspection")
	public static final Dependency[] DEPS = {
		Dependency.impl("CodeChickenCore", "CodeChickenCore-([\\d.]+)", "com.github.GTNewHorizons:CodeChickenCore::dev"),
		Dependency.impl("CodeChickenLib", "CodeChickenLib-([\\d.]+)", "com.github.GTNewHorizons:CodeChickenLib::dev"),
		Dependency.impl("Gregtech", "gregtech-([\\d.]+)", "com.github.GTNewHorizons:GT5-Unofficial::dev"),
		Dependency.impl("GT++", "GT-PlusPlus-([\\d.]+)", "com.github.GTNewHorizons:GTplusplus::dev"),
		Dependency.impl("GTNHLib", "gtnhlib-([\\d.]+)", "com.github.GTNewHorizons:GTNHLib::dev"),
		Dependency.impl("NewHorizonsCore", "GTNewHorizonsCoreMod-([\\d.]+)", "com.github.GTNewHorizons:NewHorizonsCoreMod::dev"),
		Dependency.impl("GoodGenerator", "GoodGenerator-([\\d.]+)", "com.github.GTNewHorizons:GoodGenerator::dev"),
		Dependency.impl("TecTech", "TecTech-([\\d.]+)", "com.github.GTNewHorizons:TecTech::dev"),
		Dependency.impl("Forestry", "Forestry-([\\d.]+)", "com.github.GTNewHorizons:ForestryMC::api", true),
		Dependency.impl("Railcraft", "Railcraft-([\\d.]+)", "com.github.GTNewHorizons:Railcraft::dev", true),
		Dependency.impl("Buildcraft", "buildcraft-([\\d.]+)", "com.github.GTNewHorizons:BuildCraft::dev", true),
		Dependency.impl("EnderIO", "EnderIO-([\\d.]+)", "com.github.GTNewHorizons:EnderIO::dev", true),
		Dependency.impl("ProjectRed", "ProjRed-([\\d.]+)-GTNH", "com.github.GTNewHorizons:ProjectRed:%version%-GTNH:dev", true),
		Dependency.impl("TinkerConstruct", "TConstruct-([\\d.]+)-GTNH", "com.github.GTNewHorizons:TinkersConstruct:%version%-GTNH:dev", true),
	};

	public static String generateDependencyCode(Dependency[] dependencies, Path instModPath) throws IOException {
		return generateDependencyCode(dependencies, instModPath, true);
	}

	public static String generateDependencyCode(Dependency[] dependencies, Path instModPath, boolean throwIfNotFound) throws IOException {
		// get all file visitors from dependencies
		var visitors = Arrays.stream(dependencies).map(Dependency::getFileVisitor).toList();

		// iterate all visitors to walk the mod folder
		for(FileVisitor<? super Path> visitor : visitors) {
			Files.walkFileTree(instModPath, visitor);
		}

		var sb = new StringBuilder();
		sb.append("// generated code start - DON'T CHANGE!").append("\n");
		for(Dependency dep : dependencies) {
			if(dep.isFound()) {
				sb.append(dep.getDependencyNotationCode());
			} else {
				if(throwIfNotFound) {
					throw new IllegalStateException("Version for " + dep.getName() + " is missing!");
				}
				sb.append("// ERROR: dependency ").append(dep.getName()).append(" is missing!");
			}
			sb.append("\n");
		}
		sb.append("// generated code end");

		return sb.toString();
	}

	public static void main(String[] args) throws Exception {

		var path = args.length > 0 ? args[0] : INST_MODS_PATH_ENV;

		long timeStart = System.currentTimeMillis();
		System.out.println(generateDependencyCode(DEPS, Path.of(path)));
		System.out.println();
		System.out.println("Time elapsed: " + (System.currentTimeMillis() - timeStart) + "ms");

	}

}
