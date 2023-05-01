import java.util.Properties

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "org.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
tasks.shadowJar {
    dependsOn(configurations)
    archiveClassifier.set(null as String?)
    archiveBaseName.set("JVMGithubActions")
    destinationDirectory.set(File(rootDir, "jars").also(File::mkdirs))
}

tasks.create("PublishPrimaryVersion") {
    val file = File("./.github/version.env")
    if (!file.exists()) {
        file.parentFile.mkdirs()
        file.createNewFile()
    }
    val properties = Properties()
    properties.load(file.reader())
    properties.setProperty("MAJOR_VERSION", "1.0.0")

    properties.store(file.writer(), null)
    // Remove first comment
    file.readLines().toMutableList().apply {
        removeFirst()
        file.writeText(joinToString("\n"))
    }
}