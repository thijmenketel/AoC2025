import java.time.LocalDate

plugins {
    kotlin("jvm") version "2.2.21"
}

kotlin {
    jvmToolchain(21)
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

val dayDirs =
    file("src")
        .listFiles()
        ?.filter { it.isDirectory && it.name.startsWith("day") }
        ?: emptyList()

dayDirs.forEach { dayDir ->
    val dayName = dayDir.name
    val taskName = "run$dayName"

    tasks.register<JavaExec>(taskName) {
        group = "application"
        description = "Run $dayName"
        mainClass.set("$dayName.MainKt")
        classpath = sourceSets["main"].runtimeClasspath
    }
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
    }
    register("runAll") {
        group = "application"
        description = "Run all days"
        dependsOn(dayDirs.map { "run${it.name}" })
    }

    register("today") {
        group = "application"
        description = "Run today's day"
        val dayName =
            LocalDate
                .now()
                .dayOfMonth
                .toString()
                .padStart(2, '0')
        dependsOn("runday$dayName")
    }
}
