plugins {
    // Apply the Java Gradle plugin development plugin to add support for developing Gradle plugins
    `java-gradle-plugin`
    id("net.researchgate.release") version "3.0.2"
}

group = property("GROUP").toString()
version = property("VERSION").toString()

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
}

release {
    tagTemplate.set("v${version}")
}

gradlePlugin {
    // Define the plugin
    val greeting by plugins.creating {
        id = property("ID").toString()
        implementationClass = property("IMPLEMENTATION_CLASS").toString()
        version = property("VERSION").toString()
    }
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

// Add a task to run the functional tests
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

tasks.check {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}

tasks.test {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
