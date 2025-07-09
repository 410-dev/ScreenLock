plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.hysong"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.java.dev.jna:jna:5.14.0")
    implementation("net.java.dev.jna:jna-platform:5.14.0")
}

application {
    mainClass.set("me.hysong.app.screenlock.Application")  // ← adjust to your actual entry point
}

// Configure the fat-jar task:
tasks {
    // ShadowJar will produce build/libs/<name>-all.jar
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
//        archiveClassifier.set("all")
        archiveFileName.set("ScreenLock.jar")
        destinationDirectory.set(file("./"))
        mergeServiceFiles()           // if your dependencies have service files
        manifest {
            attributes("Main-Class" to application.mainClass.get())
        }
    }

    // Make `build` depend on the fat JAR:
    build {
        dependsOn(shadowJar)
    }

    test {
        useJUnitPlatform()
    }
}
