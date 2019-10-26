plugins {
    kotlin("jvm")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":data"))
    implementation(project(":exception"))

    implementation(Dependencies.koinCore)
    implementation(Dependencies.kotlinStdLib)
    implementation(Dependencies.okHttp)
    implementation(Dependencies.okHttpLoggingInterceptor)
    api(Dependencies.retrofit) {
        exclude(module = "okhttp")
    }
    implementation(Dependencies.moshi)
    implementation(Dependencies.moshiKotlin)
    implementation(Dependencies.retrofitCoroutinesAdapter)
    implementation(Dependencies.retrofitConverterMoshi)

    testImplementation(TestDependencies.assertJ)
    testImplementation(TestDependencies.junit)
    testImplementation(TestDependencies.mockWebServer)
}

java {
    sourceCompatibility = AndroidSettings.sourceCompatibility
    targetCompatibility = AndroidSettings.targetCompatibility
}
