/**
 * Compose Multiplatform convention: applies the Compose plugin and the Compose
 * compiler. Modules that render UI apply this on top of the library convention;
 * they pull their own Compose and Jenga dependencies.
 */
plugins {
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}
