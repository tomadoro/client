import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose.multiplatform)
}

version = libs.versions.app.version.get()

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.runtime)

    implementation(libs.koin.core)

    implementation(libs.sqldelight.jvm.driver)

    implementation(projects.feature.common.domain)

    implementation(libs.timemates.sdk)
    implementation(libs.timemates.engine.rsocket)

    implementation(libs.ktor.client.cio)
    
    implementation(projects.feature.users.data.database)
    implementation(projects.feature.authorization.dependencies)
    implementation(projects.feature.authorization.data.database)

    implementation(projects.feature.timers.data.database)
    implementation(projects.feature.timers.dependencies)

    implementation(projects.navigation)

    implementation(projects.foundation.time)

    implementation(projects.styleSystem)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<Zip> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.withType<Tar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

compose.desktop {

    application {
        mainClass = "io.timemates.app.MainKt"

        buildTypes.release {
            proguard {
                optimize = true
                obfuscate = false

                configurationFiles.setFrom(File("src/main/compose-desktop.pro"))
            }
        }

        nativeDistributions {
            packageName = "TimeMates"
            description = "The ultimate tool for organizing time and tasks, collaborating with team members, and tracking progress."
            modules("java.instrument", "java.management", "java.sql", "jdk.unsupported")

            val iconsFolder = "src/main/resources/icons"

            outputBaseDir.set(File("build/distributions"))

            windows {
                iconFile.set(File("$iconsFolder/app-icon.ico"))
            }
            macOS {
                iconFile.set(File("$iconsFolder/app-icon.icns"))
                dockName = "TimeMates"
            }
            linux {
                iconFile.set(File("$iconsFolder/app-icon.png"))
            }

            licenseFile.set(rootProject.file("LICENSE.txt"))
            vendor = "TimeMates"

            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
        }
    }
}