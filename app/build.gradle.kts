plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("kapt")
}

android {
    namespace = "com.example.commyproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.commyproject"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.retrofit2.retrofit)
    implementation (libs.logging.interceptor)

    //gsonConverter
    implementation (libs.converter.gson)

    implementation (libs.socket.io.client)
    implementation (libs.gson)

    implementation(libs.lottie)

    //Glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    //roomDB
    val room_version = "2.5.0"
    implementation (libs.androidx.room.runtime)
    kapt (libs.androidx.room.compiler)
    implementation (libs.androidx.room.ktx)

    //dagger-hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)

    // WorkManager dependency
    implementation (libs.androidx.work.runtime.ktx)

    implementation("io.socket:socket.io-client:2.0.0") {
        exclude(group = "com.google.android.gms")
    }

}