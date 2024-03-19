package cn.taskeren.gtnn.tools;

import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.regex.Pattern;

public class Dependency implements Serializable {

	/**
	 * Skip matching if the file has already found. Set to false for debugging.
	 */
	public static boolean SkipIfFound = true;

	public static final String VERSION_TEMPLATE = "%version%";

	private final String name;

	private final Pattern pattern;
	private final int versionGroupIndex;

	private final String groupName;
	private final String artifactName;
	private final String versionTemplate;
	private final String subversion;

	private final String dependencyType;
	private final boolean noTransitiveDependency;

	private transient boolean found;
	private transient String foundVersion;

	/**
	 * Create a dependency with given parameters.
	 *
	 * @param name                   the name of the dependency
	 * @param pattern                the matching pattern used to get the version string
	 * @param versionGroupIndex      the index of the version string in the matching groups
	 * @param groupName              the group name of the dependency used to generate gradle code
	 * @param artifactName           the artifact name of the dependency used to generate gradle code
	 * @param versionTemplate        the version template of the dependency used to generate gradle code. {@code %version%} will be replaced to the matched version string
	 * @param subversion             the subversion of the dependency used to generate gradle code
	 * @param dependencyType         the import type of the dependency used to generate gradle code
	 * @param noTransitiveDependency if the dependency not transitive used to generate gradle code
	 */
	public Dependency(
		String name,
		Pattern pattern,
		int versionGroupIndex,
		String groupName,
		String artifactName,
		String versionTemplate,
		String subversion,
		String dependencyType,
		boolean noTransitiveDependency
	) {
		this.name = name;
		this.pattern = pattern;
		this.versionGroupIndex = versionGroupIndex;
		this.groupName = groupName;
		this.artifactName = artifactName;
		this.versionTemplate = versionTemplate;
		this.subversion = subversion;
		this.dependencyType = dependencyType;
		this.noTransitiveDependency = noTransitiveDependency;
	}

	/**
	 * Copy a dependency from another one.
	 *
	 * @param source the source
	 */
	public Dependency(Dependency source) {
		this(
			source.name,
			source.pattern,
			source.versionGroupIndex,
			source.groupName,
			source.artifactName,
			source.versionTemplate,
			source.subversion,
			source.dependencyType,
			source.noTransitiveDependency
		);
	}

	/**
	 * Create a dependency instance.
	 * <p>
	 * DependencyNotation should follow the style of gradle dependency notation like
	 * {@code com.example:example-artifact::subversion}, which version part should be ignored.
	 *
	 * @param name               the dependency name
	 * @param regex              the regex pattern to read the version, where the version should be in the first group
	 * @param dependencyNotation the version-unassigned dependency notation
	 * @return the new instance
	 */
	public static Dependency impl(
		String name,
		String regex,
		String dependencyNotation
	) {
		return impl(name, regex, dependencyNotation, false);
	}

	/**
	 * Create a dependency instance.
	 * <p>
	 * DependencyNotation should follow the style of gradle dependency notation like
	 * {@code com.example:example-artifact::subversion}, which version part should be ignored.
	 *
	 * @param name               the dependency name
	 * @param regex              the regex pattern to read the version, where the version should be in the first group
	 * @param dependencyNotation the version-unassigned dependency notation
	 * @param noTransitive       if the dependency not transitive
	 * @return the new instance
	 */
	public static Dependency impl(
		String name,
		String regex,
		String dependencyNotation,
		boolean noTransitive
	) {
		var builder = builder()
			.name(name)
			.pattern(regex)
			.dependencyNotation(dependencyNotation)
			.dependencyType("implementation");
		if(noTransitive) {
			builder.noTransitive();
		}
		return builder.build();
	}

	void testFile(Path path) {
		if(SkipIfFound && found) {
			return;
		}

		var name = path.getFileName().toString();
		if(!name.endsWith(".jar")) return;
		name = name.substring(0, name.length() - 4);

		var matcher = pattern.matcher(name);

		if(matcher.find()) {
			found = true;
			foundVersion = matcher.group(versionGroupIndex);
		}
	}

	public String getName() {
		return name;
	}

	public boolean isFound() {
		return found;
	}

	public String getFoundVersion() {
		return isFound() ? foundVersion : "";
	}

	public String getTemplatedVersion() {
		return versionTemplate.replace(VERSION_TEMPLATE, getFoundVersion());
	}

	private StringBuilder getDependencyNotationBase() {
		return new StringBuilder()
			.append(groupName).append(":")
			.append(artifactName);
	}

	public String getDependencyNotation() {
		var sb = getDependencyNotationBase();
		sb.append(":").append(getTemplatedVersion());
		if(subversion != null) {
			sb.append(":").append(subversion);
		}
		return sb.toString();
	}

	public String getDependencyNotationCode() {
		var sb = new StringBuilder();
		// append comment
		sb.append("// ").append(name).append("\n");
		// append dependency code
		sb.append(dependencyType).append("(")
			.append("\"")
			.append(getDependencyNotation())
			.append("\"")
			.append(")");
		if(noTransitiveDependency) {
			sb.append(" { ");
			sb.append("transitive = false");
			sb.append(" }");
		}
		return sb.toString();
	}

	public FileVisitor<? super Path> getFileVisitor() {
		return new SimpleFileVisitor<>() {
			@Override
			public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
				testFile(path);
				return isFound() ? FileVisitResult.TERMINATE : FileVisitResult.CONTINUE;
			}
		};
	}

	@Override
	public String toString() {
		return "Dependency(%s, %s, [%s])".formatted(name, pattern.pattern(), getDependencyNotationBase());
	}

	public static Builder builder() {
		return new Builder();
	}

	@SuppressWarnings("UnusedReturnValue")
	public static class Builder {

		private String name;

		private Pattern pattern;
		private int versionGroupIndex = 1;

		private String groupName;
		private String artifactName;
		private String versionTemplate;
		private String subversion;

		private String dependencyType;
		private boolean noTransitiveDependency;

		public Builder() {
		}

		public Builder name(String name) {
			this.name = name;
			return this;
		}

		public Builder pattern(String regex) {
			this.pattern = Pattern.compile(regex);
			return this;
		}

		public Builder versionGroupIndex(int versionGroupIndex) {
			this.versionGroupIndex = versionGroupIndex;
			return this;
		}

		public Builder groupName(String groupName) {
			this.groupName = groupName;
			return this;
		}

		public Builder artifactName(String artifactName) {
			this.artifactName = artifactName;
			return this;
		}

		public Builder versionTemplate(String versionTemplate) {
			this.versionTemplate = versionTemplate;
			return this;
		}

		public Builder subversion(String subversion) {
			this.subversion = subversion;
			return this;
		}

		public Builder dependencyNotation(String dependencyNotation) {
			var notation = dependencyNotation.split(":");
			if(notation.length < 2 || notation.length > 4)
				throw new IllegalStateException("invalid dependencyNotation: there should be only 2-4 parts in the notation");

			groupName(notation[0]);
			artifactName(notation[1]);
			versionTemplate(
				notation.length == 2 // if there is no version part
					? VERSION_TEMPLATE
					: notation[2].isEmpty() // if the version part is empty
					? VERSION_TEMPLATE
					: notation[2]
			);
			subversion(notation.length > 3 ? notation[3] : null);
			return this;
		}

		public Builder dependencyType(String dependencyType) {
			this.dependencyType = dependencyType;
			return this;
		}

		public Builder implementationType() {
			dependencyType("implementation");
			return this;
		}

		public Builder noTransitive() {
			return noTransitive(true);
		}

		public Builder noTransitive(boolean noTransitiveDependency) {
			this.noTransitiveDependency = noTransitiveDependency;
			return this;
		}

		public Dependency build() {
			Objects.requireNonNull(name);
			Objects.requireNonNull(pattern);

			return new Dependency(
				name,
				pattern,
				versionGroupIndex,
				groupName,
				artifactName,
				versionTemplate,
				subversion,
				dependencyType,
				noTransitiveDependency
			);
		}

	}

}
