import org.gradle.api.artifacts.VersionCatalogsExtension

plugins {
    `kotlin-dsl`
}

val catalog = extensions.getByType<VersionCatalogsExtension>().named("libs")
fun version(alias: String): String = catalog.findVersion(alias).get().requiredVersion

// Convention plugins that assemble the module tiers. Each Gradle plugin these
// scripts apply by id must be on the build-logic classpath; the plugin marker
// coordinate for an id "x" is "x:x.gradle.plugin", versioned from the catalog.
dependencies {
    fun plugin(id: String, version: String) = "$id:$id.gradle.plugin:$version"

    implementation(plugin("org.jetbrains.kotlin.multiplatform", version("kotlin")))
    implementation(plugin("org.jetbrains.kotlin.plugin.compose", version("kotlin")))
    implementation(plugin("com.android.kotlin.multiplatform.library", version("gradle")))
    implementation(plugin("org.jetbrains.compose", version("compose")))
}
