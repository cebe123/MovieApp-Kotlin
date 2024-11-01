// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
   alias(libs.plugins.ksp)
    id("com.android.application") version "8.6.1" apply false
    id("com.android.library") version "8.6.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    //id("com.google.devtools.ksp") version "2.0.0-1.0.23" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.kotlin.gradle.plugin) // Replace with the actual version

    }
}