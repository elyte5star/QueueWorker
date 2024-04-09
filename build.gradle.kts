plugins {
    id("java")
    // Apply the application plugin to add support for building a CLI application in Java.
    id("application")
   
}



group = "org.elyte"
version = "1.0-SNAPSHOT"


application {
    // Define the main class for the application.
    mainClass.set("org.elyte.Main")
}



repositories {
    mavenCentral()
}


dependencies {

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("com.rabbitmq:amqp-client:5.20.0")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-log4j12:2.0.9")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.16.1")
    implementation("com.jcabi:jcabi-log:0.24.1")
    implementation("org.yaml:snakeyaml:2.2")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<Jar>{
    manifest {
        attributes["Main-Class"] = "org.elyte.Main"
    }
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    
}


