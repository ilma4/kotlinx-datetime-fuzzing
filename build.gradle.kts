plugins {
    kotlin("jvm") version "2.0.10"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

application {
    // Specify the main class of your application
    mainClass.set("kotlinx.fuzz.FindTargets") // Replace with your actual main class
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    testImplementation("com.code-intelligence:jazzer-api:0.0.0-dev")
    testImplementation("com.code-intelligence:jazzer-junit:0.0.0-dev")

    testImplementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

    implementation(kotlin("reflect"))
    implementation("org.reflections:reflections:0.10.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    classpath(sourceSets["test"].compileClasspath + sourceSets["main"].runtimeClasspath)
}


kotlin {
    jvmToolchain(17)
}