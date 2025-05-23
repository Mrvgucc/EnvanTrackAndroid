plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.designs"
    compileSdk = 35

    buildFeatures {
        dataBinding = true
    }

    defaultConfig {
        applicationId = "com.example.designs"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation (libs.glide)

    implementation(libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.gson)

    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.7")

    implementation("androidx.lifecycle:lifecycle-livedata:2.8.7")

    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation (libs.recyclerview.swipedecorator)

    implementation(libs.androidx.recyclerview)





}