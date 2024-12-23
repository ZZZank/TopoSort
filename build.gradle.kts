plugins {
    id("java")
}

group = "zank.lib.script_topo_sort"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val lombok_version = "1.18.36"
    compileOnly("org.projectlombok:lombok:${lombok_version}")
    annotationProcessor("org.projectlombok:lombok:${lombok_version}")
    // https://mvnrepository.com/artifact/it.unimi.dsi/fastutil
//    implementation("it.unimi.dsi:fastutil:8.5.9")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testCompileOnly("org.projectlombok:lombok:${lombok_version}")
    testAnnotationProcessor("org.projectlombok:lombok:${lombok_version}")
}

tasks.test {
    useJUnitPlatform()
}