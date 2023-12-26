plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

version = "SNAPSHOT"

kotlin {
    jvm()
    androidTarget()

    jvmToolchain(17)
}

android {
    compileSdk = 34

    defaultConfig {
        namespace = "io.timemates.app"
    }

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_17
    }
}

