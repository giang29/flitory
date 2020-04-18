plugins {
    kotlin("jvm")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(Dependencies.coroutinesCore)

    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.dagger)
    kapt(Dependencies.daggerCompiler)

    testImplementation(TestDependencies.assertJ)
    testImplementation(TestDependencies.junit)
    testImplementation(TestDependencies.mockk)
}

java {
    sourceCompatibility = AndroidSettings.sourceCompatibility
    targetCompatibility = AndroidSettings.targetCompatibility
}
