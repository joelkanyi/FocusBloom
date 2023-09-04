plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.compose.multiplatform)
}

android {
    namespace = "com.joelkanyi.focusbloom.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.joelkanyi.focusbloom.android"
        minSdk = 21
        targetSdk = compileSdk
        versionCode = 1
        versionName = "1.0.0"
    }
    buildTypes {
        // debug
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
        }
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.compose.activity)
    implementation(libs.koin.android)
    coreLibraryDesugaring(libs.core.library.desugaring)
}
