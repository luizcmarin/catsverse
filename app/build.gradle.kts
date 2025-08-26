// =============================================================================
// Arquivo: app/build.gradle.kts
// Descrição: Configuração do Gradle para o módulo :app.
// =============================================================================
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.marin.catsverse.app"
    compileSdk = libs.versions.compileSdk.get().toInt()
    flavorDimensions += listOf("app")

    defaultConfig {
        applicationId = "com.marin.catsverse"
        minSdk =  libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["dagger.hilt.android.internal.disableAndroidSuperclassValidation"] = "true"
    }

    buildTypes {
        debug {
            isCrunchPngs = false
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
            freeCompilerArgs.add("-Xannotation-default-target=param-property")
        }
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
        arg("room.incremental", "true")
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        jniLibs {
            keepDebugSymbols += "**/libandroidx.graphics.path.so"
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.bundles.compose.base)
    implementation(libs.bundles.compose.ui)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.room)

    ksp(libs.hilt.compiler)
    ksp(libs.androidx.room.compiler)

    // Dependências de teste
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.compose.testing)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}