plugins {
    kotlin("jvm") version "1.3.61"
    kotlin("kapt") version "1.3.70"
}

group = "dev.mgambati"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile("de.vandermeer:asciitable:0.3.2")
    compile("info.picocli:picocli:4.2.0")
    kapt("info.picocli:picocli-codegen:4.2.0")
}

val fatJar = task("fatJar", type = Jar::class) {
    archiveBaseName.set("compiler")
    manifest {
        attributes["Main-Class"] = "dev.mgambati.CompilerKt"
    }
    from(configurations.runtime.get().map { if (it.isDirectory) it else zipTree(it) })
    with(tasks["jar"] as CopySpec)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }

    build {
        dependsOn(fatJar)
    }
}

kapt {
    arguments {
        arg("project", "${project.group}/${project.name}")
    }
}
