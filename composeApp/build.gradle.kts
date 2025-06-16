import org.gradle.kotlin.dsl.implementation
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinSerialization)
    //id("app.cash.sqldelight") version "2.0.2"


}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm("desktop")
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
//                implementation(libs.androidx.lifecycle.viewmodel)
//                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.kermit)
                implementation(libs.decompose)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.decompose.extensionsComposeJetbrains)
                //implementation("org.jetbrains.compose.resources:resources:1.5.10")
                //implementation("app.cash.sqldelight:sqlite-driver:2.0.2")

            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.kermit)
                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesSwing)
                implementation(libs.kermit)
                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)

            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies{
                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)
            }


        }

        val wasmJsMain by getting
    }
    
//    sourceSets {
//        val desktopMain by getting
//
//        androidMain.dependencies {
//            implementation(compose.preview)
//            implementation(libs.androidx.activity.compose)
//        }
//        commonMain.dependencies {
//            implementation(compose.runtime)
//            implementation(compose.foundation)
//            implementation(compose.material3)
//            implementation(compose.ui)
//            implementation(compose.components.resources)
//            implementation(compose.components.uiToolingPreview)
//            implementation(libs.androidx.lifecycle.viewmodel)
//            implementation(libs.androidx.lifecycle.runtimeCompose)
//        }
//        commonTest.dependencies {
//            implementation(libs.kotlin.test)
//        }
//        desktopMain.dependencies {
//            implementation(compose.desktop.currentOs)
//            implementation(libs.kotlinx.coroutinesSwing)
//        }
//    }
}

android {
    namespace = "com.amaurysdelossantos.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.amaurysdelossantos.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
//room {
//    schemaDirectory("$projectDir/schemas")
//}
dependencies {
    debugImplementation(compose.uiTooling)
    //ksp(libs.room.compiler)

}

compose.desktop {
    application {
        mainClass = "com.amaurysdelossantos.project.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.amaurysdelossantos.project"
            packageVersion = "1.0.0"
        }
    }
}
