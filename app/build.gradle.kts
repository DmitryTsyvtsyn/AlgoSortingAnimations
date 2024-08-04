plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "io.github.dmitrytsyvtsyn.algosortinganimations"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.dmitrytsyvtsyn.algosortinganimations"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes.add("/META-INF/*")
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.org.jetbrains.kotlin.coroutines)
    testImplementation(libs.junit)
}