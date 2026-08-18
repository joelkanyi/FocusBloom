plugins {
    id("focusbloom.kmp.feature")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.capability.session.api)
            implementation(projects.core.model)
            implementation(projects.core.designsystem)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
        val desktopTest by getting {
            dependencies {
                implementation(projects.core.testing)
                implementation(kotlin("test"))
            }
        }
    }
}
