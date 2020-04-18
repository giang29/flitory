plugins {
    kotlin("jvm")
    id("kotlin-kapt")
}

dependencies {
    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.dagger)
    kapt(Dependencies.daggerCompiler)
}

java {
    sourceCompatibility = AndroidSettings.sourceCompatibility
    targetCompatibility = AndroidSettings.targetCompatibility
}
