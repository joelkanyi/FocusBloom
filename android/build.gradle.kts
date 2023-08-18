plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.company.kmp_template.android"
        minSdk = 21
        targetSdk = compileSdk
        versionCode = 1
        versionName = "1.0.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
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
        kotlinCompilerExtensionVersion = "1.4.0"
    }
}

dependencies {
    api(project(":shared"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidX.core)

    implementation(libs.material)

    implementation(libs.compose.ui)
    implementation(libs.compose.material)
    implementation(libs.compose.tooling)
    implementation(libs.compose.runtime)
    implementation(libs.compose.util)
    implementation(libs.compose.activity)

    implementation(libs.lifecycle.runtime)

    // Koin-Dependency injection
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    // Compose Navigation-Navigation between various screens
    implementation(libs.navigation.compose)

    testImplementation(libs.jUnitKtx)
    testImplementation(libs.kotlinX.coroutines.test)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.archTestCore)
    testImplementation(libs.robolectric)

    androidTestImplementation(libs.test.rules)
    androidTestImplementation(libs.test.runner)
}
