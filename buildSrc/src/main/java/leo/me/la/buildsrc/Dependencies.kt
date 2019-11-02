object Versions {
    const val androidGradlePlugin = "3.5.1"
    const val androidTest = "1.3.0-alpha02"
    const val androidX = "1.1.0"
    const val assertJ = "3.13.2"
    const val assertJAndroid = "1.2.0"
    const val cardView = "1.0.0"
    const val constraintlayout = "2.0.0-beta1"
    const val coreKtx = "1.2.0-beta01"
    const val coreTesting = "1.1.1"
    const val coroutinesCore = "1.3.2"
    const val espresso = "3.3.0-alpha02"
    const val glide = "4.10.0"
    const val junit = "4.13-beta-3"
    const val koin = "2.1.0-alpha-1"
    const val kotlin = "1.3.50"
    const val lifecycle = "2.2.0-rc01"
    const val material = "1.2.0-alpha01"
    const val mockk = "1.9.3"
    const val mockWebServer = "3.14.1"
    const val moshi = "1.8.0"
    const val okHttp = "3.14.4"
    const val recyclerView = "1.1.0-rc01"
    const val retrofit = "2.6.2"
    const val updatePluginVersion = "0.27.0"
    const val epoxy = "3.8.0"
}

object Dependencies {
    const val appCompat = "androidx.appcompat:appcompat:${Versions.androidX}"
    const val cardView = "androidx.cardview:cardview:${Versions.cardView}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesCore}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesCore}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val glideOkHttp = "com.github.bumptech.glide:okhttp3-integration:${Versions.glide}"
    const val koinCore = "org.koin:koin-core:${Versions.koin}"
    const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val kotlinAllOpen = "org.jetbrains.kotlin:kotlin-allopen:${Versions.kotlin}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val livecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val materialDesign = "com.google.android.material:material:${Versions.material}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttp}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitConverterMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    const val epoxy = "com.airbnb.android:epoxy:${Versions.epoxy}"
    const val epoxyProcessor = "com.airbnb.android:epoxy-processor:${Versions.epoxy}"
}


object TestDependencies {
    const val archCoreTesting = "android.arch.core:core-testing:${Versions.coreTesting}"
    const val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"
    const val assertJAndroid = "com.squareup.assertj:assertj-android:${Versions.assertJAndroid}"
    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val espressoCont = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesCore}"
    const val junit = "junit:junit:${Versions.junit}"
    const val koinTest = "org.koin:koin-test:${Versions.koin}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val mockkAndroid = "io.mockk:mockk-android:${Versions.mockk}"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:${Versions.mockWebServer}"
    const val testRule = "androidx.test:rules:${Versions.androidTest}"
    const val testRunner = "androidx.test:runner:${Versions.androidTest}"
}
