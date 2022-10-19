import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone

plugins {
    id("net.kyori.indra")
    id("net.kyori.indra.checkstyle")
    id("net.kyori.indra.licenser.spotless")
    id("net.ltgt.errorprone")
}

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.xpdustry.fr/releases") {
        name = "xpdustry-repository-releases"
        mavenContent { releasesOnly() }
    }
}

dependencies {
    compileOnly("org.jetbrains:annotations:23.0.0")

    val junit = "5.8.2"
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junit")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit")

    annotationProcessor("com.uber.nullaway:nullaway:0.9.4")
    errorprone("com.google.errorprone:error_prone_core:2.10.0")
}

indra {
    checkstyle("9.3")

    javaVersions {
        target(17)
        minimumToolchain(17)
    }
}

indraSpotlessLicenser {
    licenseHeaderFile(rootProject.file("LICENSE_HEADER.md"))
}

tasks.withType<JavaCompile> {
    options.errorprone {
        disableWarningsInGeneratedCode.set(true)
        disable(
            "MissingSummary",
            "BadImport",
            "FutureReturnValueIgnored",
            "InlineMeSuggester",
            "EmptyCatch"
        )
        if (!name.contains("test", true)) {
            check("NullAway", CheckSeverity.ERROR)
            option("NullAway:AnnotatedPackages", "fr.xpdustry.distributor")
        }
    }
}

// Gradle provider issues with Indra
tasks.compileJava {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}
tasks.compileTestJava {
    sourceCompatibility = "17"
    targetCompatibility = "17"
}
