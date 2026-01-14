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

    // Kotlin
    testImplementation(kotlin("test"))

    // Coroutines test
    testImplementation(libs.kotlinx.coroutines.test)

    // Mocking
    testImplementation(libs.mockk)

    // JUnit 5
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}
