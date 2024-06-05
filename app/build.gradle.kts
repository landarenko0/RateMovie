import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleServices)

    alias(libs.plugins.serialization)
    alias(libs.plugins.safeArgs)

    alias(libs.plugins.kapt)
    alias(libs.plugins.daggerHiltAndroid)
}

android {
    namespace = "com.example.ratemovie"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ratemovie"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        val localProperties = Properties()
        localProperties.load(FileInputStream(rootProject.file("local.properties")))
        localProperties.load(rootProject.file("local.properties").inputStream())

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "API_KEY", localProperties.getProperty("API_KEY"))
            buildConfigField("String", "BASE_URL", localProperties.getProperty("BASE_URL"))
        }

        debug {
            buildConfigField("String", "API_KEY", localProperties.getProperty("API_KEY"))
            buildConfigField("String", "BASE_URL", localProperties.getProperty("BASE_URL"))
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.cardview)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.hilt.navigation.fragment)

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)

    implementation(libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    implementation(libs.coil)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kapt {
    correctErrorTypes = true
}