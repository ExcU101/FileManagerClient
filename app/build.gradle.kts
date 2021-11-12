plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.parcelize")
    kotlin("android")
    kotlin("kapt")
}

repositories {
    google()
    mavenCentral()
}
kapt {
    correctErrorTypes = true
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.excu_fcd.filemanageclient"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "3.1.4"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    }
}

dependencies {
    implementation(project(mapOf("path" to ":core")))
    implementation(project(mapOf("path" to ":plugin")))
    // Lifecycle
    val lifecycleVersion = "2.4.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")

    // Room
    val roomVersion = "2.3.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Hilt
    val hiltVersion = "2.40.1"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")

    // Work manager
    val workVersion = "2.7.0"
    implementation("androidx.work:work-runtime-ktx:$workVersion")

    // Navigation
    val navVersion = "2.3.5"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // AndroidX
    val fragmentVersion = "1.3.6"
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.transition:transition-ktx:1.4.1")
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("com.google.android.material:material:1.5.0-alpha05")
}