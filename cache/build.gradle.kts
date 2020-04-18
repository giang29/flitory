plugins {
    kotlin("jvm")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(Dependencies.dagger)
    kapt(Dependencies.daggerCompiler)


    implementation(Dependencies.kotlinStdLib)

    testImplementation(TestDependencies.assertJ)
    testImplementation(TestDependencies.junit)
    testImplementation(TestDependencies.mockWebServer)
}

java {
    sourceCompatibility = AndroidSettings.sourceCompatibility
    targetCompatibility = AndroidSettings.targetCompatibility
}
