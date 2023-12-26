plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    compileSdk = libs.versions.android.target.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.min.get().toInt()
        targetSdk = libs.versions.android.target.get().toInt()

        multiDexEnabled = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true

            proguardFile(
                getDefaultProguardFile("proguard-android-optimize.txt"),
            )
        }
    }

    packaging {
        resources {
            excludes.add("META-INF/INDEX.LIST")
            excludes.add("META-INF/io.netty.versions.properties")
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.jetpackComposeCompilerVersion.get()
    }

    namespace = "io.timemates.app"
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.timemates.sdk)
    implementation(libs.timemates.engine.rsocket)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.android)
    implementation(libs.androidx.appcompat)

    implementation(libs.android.multidex)

    implementation(libs.ktor.client.cio)

    implementation(libs.sqldelight.android.driver)

    implementation(projects.feature.common.domain)

    implementation(projects.feature.users.data.database)
    implementation(projects.feature.authorization.dependencies)
    implementation(projects.feature.authorization.data.database)

    implementation(projects.feature.timers.data.database)
    implementation(projects.feature.timers.dependencies)
    implementation(projects.feature.timers.data.database)

    implementation(libs.compose.accompanist.systemUiController)

    implementation(projects.foundation.time)

    implementation(libs.androidx.compose.activity)
    implementation(projects.navigation)
    implementation(projects.styleSystem)
}