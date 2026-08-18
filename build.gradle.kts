import org.gradle.api.artifacts.ProjectDependency

plugins {
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.android.kmp.library) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin) apply false
    alias(libs.plugins.jvm) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.nativeCocoapod) apply false
    alias(libs.plugins.spotless)
    id("dev.iurysouza.modulegraph") version "0.12.0"
}

moduleGraphConfig {
    readmePath.set("./README.md")
    heading = "### Module Graph"
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    spotless {
        kotlin {
            target("**/*.kt")
            licenseHeaderFile(
                rootProject.file("${project.rootDir}/spotless/copyright.kt"),
                "^(package|object|import|interface)",
            )
            trimTrailingWhitespace()
            endWithNewline()
        }
        format("kts") {
            target("**/*.kts")
            targetExclude("${layout.buildDirectory.get()}/**/*.kts")
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"), "(^(?![\\/ ]\\*).*$)")
        }
    }
}

// ---- Module dependency law -------------------------------------------------
// Enforces the layering from docs/reimagine/DESIGN.md:
//   feature -> capability:api -> core; impls are known only to app.
// Grows as modules are added; run in CI via ./gradlew checkModuleGraph.
tasks.register("checkModuleGraph") {
    group = "verification"
    description = "Enforces the module dependency law (feature -> capability:api -> core)."
    doLast {
        val violations = mutableListOf<String>()
        subprojects.forEach { project ->
            val path = project.path
            val projectDeps = project.configurations
                .flatMap { config -> config.dependencies.withType(ProjectDependency::class.java) }
                .map { it.path }
                .toSet()

            projectDeps.forEach { dep ->
                // Ignore self-references (some AGP-internal configurations add them).
                if (dep == path) return@forEach
                val isFeature = path.startsWith(":feature:")
                when {
                    isFeature && dep.startsWith(":feature:") ->
                        violations += "$path (feature) depends on another feature $dep"
                    isFeature && (dep == ":core:database" || dep == ":core:datastore") ->
                        violations += "$path (feature) depends on data module $dep"
                    isFeature && dep.startsWith(":capability:") && dep.endsWith(":impl") ->
                        violations += "$path (feature) depends on capability impl $dep"
                    dep.startsWith(":app") ->
                        violations += "$path depends on app module $dep"
                }
            }
        }
        if (violations.isNotEmpty()) {
            throw GradleException("Module graph violations:\n" + violations.joinToString("\n"))
        }
        logger.lifecycle("checkModuleGraph: ok (${subprojects.size} modules)")
    }
}

// ---- Features must use jenga, not raw Compose --------------------------------
// A :feature:* module renders only through jenga (JengaBox/JengaStack/JengaInline,
// Jenga* components) and jenga tokens. Raw Compose containers, Canvas, Material, or
// hardcoded dp belong in :core:designsystem composites, never in a feature.
tasks.register("checkFeatureUi") {
    group = "verification"
    description = "Fails if a :feature:* module uses raw Compose layout, Canvas, Material, or hardcoded dp."
    doLast {
        val forbiddenImports = listOf(
            "import androidx.compose.foundation.layout.Column",
            "import androidx.compose.foundation.layout.Row",
            "import androidx.compose.foundation.layout.Box",
            "import androidx.compose.foundation.layout.FlowRow",
            "import androidx.compose.foundation.lazy.",
            "import androidx.compose.foundation.Canvas",
            "import androidx.compose.material",
        )
        val dpLiteral = Regex("""\b\d+(\.\d+)?\.dp\b""")
        val violations = mutableListOf<String>()
        subprojects.filter { it.path.startsWith(":feature:") }.forEach { project ->
            val src = project.projectDir.resolve("src")
            if (!src.exists()) return@forEach
            src.walkTopDown()
                .filter { it.isFile && it.extension == "kt" }
                .forEach { file ->
                    file.readLines().forEachIndexed { index, line ->
                        val trimmed = line.trimStart()
                        forbiddenImports.forEach { bad ->
                            if (trimmed.startsWith(bad)) {
                                violations += "${file.relativeTo(rootDir)}:${index + 1}: ${bad.removePrefix("import ")}"
                            }
                        }
                        if (dpLiteral.containsMatchIn(line)) {
                            violations += "${file.relativeTo(rootDir)}:${index + 1}: hardcoded dp (use a jenga token or a :core:designsystem composite)"
                        }
                    }
                }
        }
        if (violations.isNotEmpty()) {
            throw GradleException(
                "Features must render through jenga, not raw Compose:\n" + violations.joinToString("\n"),
            )
        }
        logger.lifecycle("checkFeatureUi: ok")
    }
}
