plugins {
    id("com.android.application")
    kotlin("kapt")
    kotlin("android")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.parcelize")
    id("com.google.gms.google-services")
}

android {
    lint {
        baseline = file("lint-baseline.xml")
    }
    namespace = "com.blruia.mycar"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.blruia.mycar"
        minSdk = 26
        targetSdk = 36
        versionCode = 3
        versionName = "1.0.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    // Core & Lifecycle
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.0")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.3.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Navigation & ViewModel
    implementation("androidx.navigation:navigation-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Room
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-rxjava3:2.6.1")

    // RxJava
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.49")
    kapt("com.google.dagger:hilt-android-compiler:2.49")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.7.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.3")

    // Material & Icons
    implementation("androidx.compose.material:material-icons-extended")
    implementation("com.google.android.material:material:1.11.0")

    // Firebase
    implementation (platform("com.google.firebase:firebase-bom:33.5.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.firebase:firebase-firestore")
    implementation ("com.google.firebase:firebase-storage")


    //Test
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    //
    implementation ("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.6.1")
}
