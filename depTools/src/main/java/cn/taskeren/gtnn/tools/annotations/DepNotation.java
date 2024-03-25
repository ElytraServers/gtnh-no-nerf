package cn.taskeren.gtnn.tools.annotations;

import org.intellij.lang.annotations.Pattern;

/**
 * Used to hint the dependency notation string error.
 * <p>
 * Examples:
 * <p>
 * <code>com.example:example-artifact</code>
 * <code>com.example:example-artifact:</code>
 * <code>com.example:example-artifact::dev</code>
 * <code>com.example:example-artifact:%version%-suffix:dev</code>
 */
@Pattern("([^:]+):([^:]+)(?::([^:]*))?(?::([^:]+))?")
public @interface DepNotation {
}
