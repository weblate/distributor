plugins {
    id("distributor.base-conventions")
    id("distributor.publishing-conventions")
    id("distributor.mindustry-conventions")
}

dependencies {
    implementation(project(":distributor-api"))
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("org.aeonbits.owner:owner-java8:1.0.12")
    annotationProcessor("cloud.commandframework:cloud-annotations:${Versions.cloud}")
}

val metadata = fr.xpdustry.toxopid.spec.ModMetadata.fromJson(rootProject.file("plugin.json"))
metadata.version = rootProject.version.toString()
metadata.description = rootProject.description.toString()
metadata.name = "xpdustry-distributor-core"
metadata.displayName = "Distributor"
metadata.main = "fr.xpdustry.distributor.core.DistributorPlugin"

tasks.shadowJar {
    archiveClassifier.set("plugin")

    doFirst {
        val temp = temporaryDir.resolve("plugin.json")
        temp.writeText(metadata.toJson(true))
        from(temp)
    }

    minimize {
        exclude(dependency("fr.xpdustry:distributor-.*:.*"))
        exclude(dependency("cloud.commandframework:cloud-.*:.*"))
        exclude(dependency("org.slf4j:slf4j-api:.*"))
        exclude(dependency("io.leangen.geantyref:geantyref:.*"))
    }

    val shadowPackage = "fr.xpdustry.distributor.core.internal.shadow"
    relocate("org.spongepowered.configurate", "$shadowPackage.configurate")
    relocate("org.aeonbits.owner", "$shadowPackage.owner")
    relocate("org.yaml.snakeyaml", "$shadowPackage.snakeyaml")

    from(rootProject.file("LICENSE.md")) {
        into("META-INF")
    }
}

tasks.register("getArtifactPath") {
    doLast { println(tasks.shadowJar.get().archiveFile.get().toString()) }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
