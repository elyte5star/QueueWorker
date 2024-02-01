plugins {
    id("java")
}

group = "org.elyte"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("com.rabbitmq:amqp-client:5.20.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-log4j12:2.0.9")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.16.1")
    implementation("com.jcabi:jcabi-log:0.24.1")
    implementation("org.yaml:snakeyaml:2.2")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}