import org.jetbrains.kotlin.gradle.tasks.FatFrameworkTask

plugins {
    kotlin("multiplatform") version "1.3.30"
    id("kotlinx-serialization") version "1.3.30"
}

repositories {
    jcenter()
    maven { setUrl("https://dl.bintray.com/kotlin/kotlin-dev") }
}



kotlin {
    jvm()
    js()
    wasm32("wasm")
    val ios32 = iosArm32("ios32")
    val ios64 = iosArm64("ios64")
    val iosSim = iosX64("iosSim")
    sourceSets["commonMain"].dependencies {
        implementation(kotlin("stdlib-common"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.11.0")
    }
    sourceSets["commonTest"].dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
    }
    sourceSets["jvmMain"].dependencies {
        implementation(kotlin("stdlib-jdk8"))
    }
    sourceSets["jvmTest"].dependencies {
        implementation(kotlin("test"))
        implementation(kotlin("test-junit"))
    }
    sourceSets["jsMain"].dependencies {
        implementation(kotlin("stdlib-js"))
    }
    sourceSets["jsTest"].dependencies {
        implementation(kotlin("test-js"))
    }

    configure(listOf(ios32, ios64, iosSim)) {
        binaries.framework {
            baseName = "Soyuz"
        }
    }

    tasks.create("debugFatFramework", FatFrameworkTask::class.java) {
        baseName = "Soyuz"
        from(
            ios32.binaries.getFramework("DEBUG"),
            ios64.binaries.getFramework("DEBUG"),
            iosSim.binaries.getFramework("DEBUG")
        )
        destinationDir = buildDir.resolve("fat-framework/debug")
        group = "Universal framework"
        description = "Builds a universal (fat) debug framework"
    }


    tasks.create("releaseFatFramework", FatFrameworkTask::class.java) {
        baseName = "Soyuz"
        from(
            ios32.binaries.getFramework("RELEASE"),
            ios64.binaries.getFramework("RELEASE"),
            iosSim.binaries.getFramework("RELEASE")
        )
        destinationDir = buildDir.resolve("fat-framework/release")
        group = "Universal framework"
        description = "Builds a universal (fat) release framework"
    }
}

