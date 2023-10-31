plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.kotlin) apply false
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.jvm) apply false
    alias(libs.plugins.nativeCocoapod) apply false
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.spotless)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.gradleVersionUpdates)
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint().userData(mapOf("disabled_rules" to "filename"))
            licenseHeaderFile(
                rootProject.file("${project.rootDir}/spotless/copyright.kt"),
                "^(package|object|import|interface)"
            )
            trimTrailingWhitespace()
            endWithNewline()
        }
        format("kts") {
            target("**/*.kts")
            targetExclude("$buildDir/**/*.kts")
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"), "(^(?![\\/ ]\\*).*$)")
        }
        format("misc") {
            target("**/*.md", "**/.gitignore")
            trimTrailingWhitespace()
            indentWithTabs()
            endWithNewline()
        }
    }
}

task("addPreCommitGitHookOnBuild") {
    println("⚈ ⚈ ⚈ Running Add Pre Commit Git Hook Script on Build ⚈ ⚈ ⚈")
    exec {
        commandLine("cp", "./.scripts/pre-commit", "./.git/hooks")
    }
    println("✅ Added Pre Commit Git Hook Script.")
}
