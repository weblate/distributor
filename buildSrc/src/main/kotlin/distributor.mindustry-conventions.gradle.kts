import fr.xpdustry.toxopid.dsl.anukenJitpack
import fr.xpdustry.toxopid.dsl.mindustryDependencies

plugins {
    id("net.kyori.indra")
    id("com.github.johnrengelman.shadow")
    id("fr.xpdustry.toxopid")
}

val metadata = fr.xpdustry.toxopid.spec.ModMetadata.fromJson(rootProject.file("plugin.json"))

toxopid {
    compileVersion.set("v" + metadata.minGameVersion)
    platforms.add(fr.xpdustry.toxopid.spec.ModPlatform.HEADLESS)
}

repositories {
    mavenCentral()
    anukenJitpack()
}

dependencies {
    mindustryDependencies()
}

tasks.runMindustryClient {
    mods.setFrom()
}
