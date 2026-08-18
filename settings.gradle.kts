enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // PREFER_SETTINGS: the Kotlin/Wasm + Kotlin/JS toolchain adds its own
    // project-level Node.js distribution repo, which the strict mode rejects.
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        // Jenga is consumed from Maven Local until it publishes the wasm target
        // to Maven Central; remove once a release including Web is available.
        mavenLocal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        // Kotlin/Wasm + Kotlin/JS toolchain distributions.
        ivy {
            name = "Node.js Distributions"
            setUrl("https://nodejs.org/dist")
            patternLayout { artifact("v[revision]/[artifact](-v[revision]-[classifier]).[ext]") }
            metadataSources { artifact() }
            content { includeModule("org.nodejs", "node") }
        }
        ivy {
            name = "Yarn Distributions"
            setUrl("https://github.com/yarnpkg/yarn/releases/download")
            patternLayout { artifact("v[revision]/[artifact]-v[revision].[ext]") }
            metadataSources { artifact() }
            content { includeModule("com.yarnpkg", "yarn") }
        }
    }
}

rootProject.name = "FocusBloom"

// ---- Module tiers (see docs/reimagine/DESIGN.md) ---------------------------
// core:      foundation, no business opinion
// capability: business services, api/impl pairs
// feature:   UI + presentation, one per screen
// app:       composition roots (added in Phase 0.6)
include(":core:model")
include(":core:common")
include(":core:testing")
include(":core:designsystem")
include(":core:database")
include(":core:datastore")
include(":core:navigation")
include(":shell")
include(":app:android")
