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
    namespace = "com.marin.catsverse"
    compileSdk = libs.versions.compileSdk.get().toInt()
    flavorDimensions += listOf("app")

    defaultConfig {
        applicationId = "com.marin.catsverse"
        minSdk =  libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        buildConfigField("boolean", "ROOM_EXPORT_SCHEMA", "true")
        buildConfigField("int", "VERSION_CODE", libs.versions.versionCode.get())
        buildConfigField("String", "VERSION_NAME", "\"${libs.versions.versionName.get()}\"")

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
            buildConfigField( "boolean", "ROOM_EXPORT_SCHEMA", "false")

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
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        jniLibs {
            keepDebugSymbols += "**/libandroidx.graphics.path.so"
            keepDebugSymbols += "**/libdatastore_shared_counter.so"
        }
    }

    lint {
        // Gera o relatório em formato SARIF
        sarifReport = true
        abortOnError = false
        checkReleaseBuilds = true
        warningsAsErrors = false
    }

    sourceSets["main"].java.srcDirs("src/main/java", "src/main/kotlin")
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.bundles.compose.base)
    implementation(libs.bundles.compose.ui)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.room)
    implementation(libs.androidx.animation.graphics)
    implementation(libs.androidx.core.animation)

    ksp(libs.hilt.compiler)
    ksp(libs.androidx.room.compiler)

    // Dependências de teste
    testImplementation(libs.bundles.testing)
    androidTestImplementation(libs.bundles.compose.testing)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

tasks.register("openDebugLintReport") {
    description = "Abre o relatório HTML do Lint para a variante debug no navegador padrão."
    group = "Verification"
    val reportFile = project.layout.buildDirectory.file("reports/lint-results-debug.html")
    doLast {
        if (reportFile.get().asFile.exists()) {
            val reportPath = reportFile.get().asFile.absolutePath
            println("Abrindo relatório do Lint: $reportPath")
            val os = System.getProperty("os.name").lowercase()
            try {
                when {
                    os.contains("win") -> Runtime.getRuntime().exec(arrayOf("cmd", "/c", "start", "\"\"", reportPath))
                    os.contains("mac") -> Runtime.getRuntime().exec(arrayOf("open", reportPath))
                    os.contains("nix") || os.contains("nux") || os.contains("aix") -> Runtime.getRuntime().exec(arrayOf("xdg-open", reportPath))
                    else -> println("SO não suportado para abrir auto.")
                }
            } catch (e: Exception) {
                println("Erro ao abrir relatório Lint: ${e.message}")
            }
        } else {
            println("Relatório Lint debug não encontrado: ${reportFile.get().asFile.absolutePath}")
        }
    }
}

tasks.register("openReleaseLintReport") {
    description = "Abre o relatório HTML do Lint para a variante release no navegador padrão."
    group = "Verification"
    val reportFile = project.layout.buildDirectory.file("reports/lint-results-release.html")
    doLast {
        if (reportFile.get().asFile.exists()) {
            val reportPath = reportFile.get().asFile.absolutePath
            println("Abrindo relatório do Lint (Release): $reportPath")
            val os = System.getProperty("os.name").lowercase()
            try {
                when {
                    os.contains("win") -> Runtime.getRuntime().exec(arrayOf("cmd", "/c", "start", "\"\"", reportPath))
                    os.contains("mac") -> Runtime.getRuntime().exec(arrayOf("open", reportPath))
                    os.contains("nix") || os.contains("nux") || os.contains("aix") -> Runtime.getRuntime().exec(arrayOf("xdg-open", reportPath))
                    else -> println("SO não suportado.")
                }
            } catch (e: Exception) {
                println("Erro ao abrir relatório (Release): ${e.message}")
            }
        } else {
            println("Relatório Lint (Release) não encontrado: ${reportFile.get().asFile.absolutePath}")
        }
    }
}

// Adia a configuração de finalizedBy para depois que o projeto for avaliado
project.afterEvaluate {
    // Para Debug
    project.tasks.findByName("lintReportDebug")?.let { lintTask ->
        project.tasks.findByName("openDebugLintReport")?.let { openReportTask ->
            lintTask.finalizedBy(openReportTask)
        } ?: println("AVISO: Tarefa 'openDebugLintReport' não encontrada para configurar finalizedBy.")
    } ?: println("AVISO: Tarefa 'lintReportDebug' não encontrada para configurar finalizedBy.")

    // Para Release
    project.tasks.findByName("lintReportRelease")?.let { lintTask ->
        project.tasks.findByName("openReleaseLintReport")?.let { openReportTask ->
            lintTask.finalizedBy(openReportTask)
        } ?: println("AVISO: Tarefa 'openReleaseLintReport' não encontrada para configurar finalizedBy.")
    } ?: println("AVISO: Tarefa 'lintReportRelease' não encontrada para configurar finalizedBy.")
}