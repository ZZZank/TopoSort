plugins {
    id("java")
}

group = "zank.lib.script_topo_sort"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    val lombok_version = "1.18.36";
    compileOnly("org.projectlombok:lombok:${lombok_version}")
    annotationProcessor("org.projectlombok:lombok:${lombok_version}")
    testCompileOnly("org.projectlombok:lombok:${lombok_version}")
    testAnnotationProcessor("org.projectlombok:lombok:${lombok_version}")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}