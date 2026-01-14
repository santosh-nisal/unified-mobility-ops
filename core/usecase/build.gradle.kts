plugins {
    kotlin("jvm")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:usecase-api"))

    testImplementation(kotlin("test"))
}
