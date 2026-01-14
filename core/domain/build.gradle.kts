plugins {
    kotlin("jvm")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    // NO Android dependencies
    // NO coroutines required
    testImplementation(kotlin("test"))
}
