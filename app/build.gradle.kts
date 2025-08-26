// =============================================================================
// Arquivo: app/build.gradle.kts
// Descrição: Configuração do Gradle para o módulo :app.
// =============================================================================
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.file.DirectoryProperty
import java.io.File
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.DefaultTask

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

// Classe que define a tarefa personalizada para converter SVGs para Compose.
@CacheableTask
abstract class SvgToComposeTask : DefaultTask() {
    // Define o diretório de entrada onde os arquivos SVG estão.
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val inputDirectory: DirectoryProperty

    // Define o diretório de saída onde o arquivo Kotlin será gerado.
    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @TaskAction
    fun generate() {
        // Pega o diretório de saída como um objeto File.
        val outputDirFile = outputDirectory.get().asFile
        // Limpa e recria o diretório de saída para garantir um build limpo.
        outputDirFile.deleteRecursively()
        outputDirFile.mkdirs()

        // Pega o diretório de entrada como um objeto File.
        val sourceDir = inputDirectory.get().asFile

        // Constrói o conteúdo do arquivo Kotlin como uma única string.
        val kotlinCode = buildString {
            appendLine("package com.marin.catsverse.utils")
            appendLine()
            appendLine("import androidx.compose.ui.graphics.vector.ImageVector")
            appendLine("import androidx.compose.ui.unit.dp")
            appendLine("import androidx.compose.ui.graphics.vector.addPathNodes")
            appendLine()
            appendLine("// Arquivo gerado automaticamente pelo Gradle.")
            appendLine("// Não o edite manualmente, pois as alterações serão sobrescritas.")
            appendLine("object Icones {")

            // Itera sobre todos os arquivos SVG no diretório de origem.
            sourceDir.walk().forEach { svgFile ->
                if (svgFile.extension.equals("svg", ignoreCase = true)) {
                    // Lógica para criar um nome de propriedade válido para a variável Kotlin.
                    val rawName = svgFile.nameWithoutExtension
                    val iconName = if (rawName.firstOrNull()?.isLetter() == true) {
                        rawName.replace("-", "_").replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    } else {
                        "_" + rawName.replace("-", "_").replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                    }

                    val svgContent = svgFile.readText()

                    // Extrai as informações de viewBox e paths do SVG.
                    val viewBox = Regex("viewBox=\"(.*?)\"").find(svgContent)?.groupValues?.get(1) ?: "0 0 24 24"
                    val viewBoxParts = viewBox.split(" ").map { it.toFloat() }
                    val pathDataList = Regex("d=\"(.*?)\"").findAll(svgContent).map { it.groupValues[1] }.toList()

                    // Gera o código para a propriedade ImageVector.
                    appendLine()
                    appendLine("    val ${iconName}: ImageVector by lazy {")
                    appendLine("        ImageVector.Builder(")
                    appendLine("            name = \"${iconName}\",")
                    appendLine("            defaultWidth = ${viewBoxParts[2]}f.dp,")
                    appendLine("            defaultHeight = ${viewBoxParts[3]}f.dp,")
                    appendLine("            viewportWidth = ${viewBoxParts[2]}f,")
                    appendLine("            viewportHeight = ${viewBoxParts[3]}f")
                    appendLine("        ).apply {")

                    // Itera sobre os paths e adiciona-os ao builder.
                    pathDataList.forEach { pathData ->
                        appendLine("            addPathNodes(\"${pathData}\")")
                    }

                    appendLine("        }.build()")
                    appendLine("    }")
                }
            }
            appendLine("}")
            appendLine()
        }

        // Pega o caminho do arquivo de saída.
        val outputFile = File(outputDirFile, "Icones.kt")
        // Escreve o conteúdo gerado no arquivo.
        outputFile.writeText(kotlinCode)
    }
}
// Registra a tarefa "generateImageVectors" e configura seus diretórios de entrada e saída.
tasks.register("generateImageVectors", SvgToComposeTask::class.java) {
    inputDirectory.set(file("src/main/assets/svgs"))
    outputDirectory.set(file("src/main/kotlin/com/marin/catsverse/utils"))
}

// Garante que a tarefa de geração de ícones rode antes da compilação com KSP
tasks.matching { it.name.startsWith("ksp") }.configureEach {
    dependsOn("generateImageVectors")
}
