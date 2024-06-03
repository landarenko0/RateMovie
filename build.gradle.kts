// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.4.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false

    kotlin("plugin.serialization") version "1.9.22"
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false

    kotlin("kapt") version "1.9.23"
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}