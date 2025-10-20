import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("jvm") version "2.2.20"
    java
    id("com.gradleup.shadow") version "9.2.2"
    id("org.jetbrains.dokka") version "2.1.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0"
    id("com.github.jk1.dependency-license-report") version "2.9"
    id("com.diffplug.spotless") version "8.0.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        url = uri("https://raw.githubusercontent.com/beehive-lab/tornado/maven-tornadovm")
    }
    mavenCentral()
}

dependencies {
    implementation("dev.langchain4j:langchain4j:1.7.1")
    implementation("dev.langchain4j:langchain4j-gpu-llama3:1.7.1-beta14")
    implementation("dev.langchain4j:langchain4j-open-ai:1.7.1")
    implementation("tornado:tornado-matrices:1.1.1")
    implementation("tornado:tornado-api:1.1.1")
    testImplementation(kotlin("test"))
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_21
    }

    compileTestKotlin {
        compilerOptions.jvmTarget = JvmTarget.JVM_21
    }

    compileJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "21"
        targetCompatibility = "21"
        options.compilerArgs.add("--enable-preview")
    }

    compileTestJava {
        options.encoding = "UTF-8"
        sourceCompatibility = "21"
        targetCompatibility = "21"
        options.compilerArgs.add("--enable-preview")
    }

    test {
        useJUnitPlatform()
    }

    withType<Detekt>().configureEach {
        // Target version of the generated JVM bytecode. It is used for type resolution.
        jvmTarget = "21"
        reports {
            html.required.set(true)
            xml.required.set(true)
            txt.required.set(true)
            sarif.required.set(true)
        }
    }

    val execute by registering(JavaExec::class) {
        group = "application"
        mainClass.set(
            if (project.hasProperty("mainClass")) {
                project.property("mainClass") as String
            } else {
                application.mainClass.get()
            },
        )
        classpath = sourceSets.main.get().runtimeClasspath
    }
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("org.example.MainKt")
    applicationDefaultJvmArgs = listOf("--add-modules=jdk.incubator.vector", "--enable-preview")
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
        reporter(ReporterType.HTML)
    }
    filter {
        exclude("**/style-violations.kt")
    }
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$projectDir/config/detekt.yml")
}

dokka.dokkaSourceSets {
    configureEach {
        jdkVersion.set(21)
        enableJdkDocumentationLink.set(false)
        enableKotlinStdLibDocumentationLink.set(false)
    }
}

spotless {
    java {
        target("src/*/java/**/*.java")
        // Use the default importOrder configuration
        importOrder()
        removeUnusedImports()
        googleJavaFormat("1.28.0")
        formatAnnotations()
    }
}
