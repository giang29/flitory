plugins {
    kotlin("jvm")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":exception"))

    implementation(Dependencies.coroutinesCore)

    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.okHttp)
    implementation(Dependencies.okHttpLoggingInterceptor)
    api(Dependencies.retrofit) {
        exclude(module = "okhttp")
    }
    api(Dependencies.moshi)
    implementation(Dependencies.moshiKotlin)
    api(Dependencies.retrofitConverterMoshi)
    implementation(Dependencies.dagger)
    kapt(Dependencies.daggerCompiler)

    testImplementation(TestDependencies.assertJ)
    testImplementation(TestDependencies.junit)
    testImplementation(TestDependencies.mockWebServer)
    testImplementation(TestDependencies.coroutinesTest)
}

java {
    sourceCompatibility = AndroidSettings.sourceCompatibility
    targetCompatibility = AndroidSettings.targetCompatibility
}
