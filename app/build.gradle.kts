plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

    id("kotlin-kapt")
}

android {
    namespace = "com.example.androidbasics"
    compileSdk {
        version = release(36)

    }
    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.androidbasics"
        minSdk = 26
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    packaging {
        resources {
            excludes += "/META-INF/INDEX.LIST"
            excludes += "/META-INF/DEPENDENCIES"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Room database
    implementation("androidx.room:room-runtime:2.8.4")
    kapt("androidx.room:room-compiler:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")



//    Data store
    implementation("androidx.datastore:datastore-preferences:1.1.0")
    implementation("androidx.datastore:datastore-preferences-core:1.1.0")
    implementation("androidx.datastore:datastore-core:1.1.0")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:34.9.0"))
    implementation("com.google.firebase:firebase-analytics")


    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")

    //FCM
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.23.0")
    implementation("com.google.api-client:google-api-client:2.3.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    //Appwrite
    implementation("io.appwrite:sdk-for-android:12.0.0")

    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    //Crashlytics
    implementation("com.google.firebase:firebase-crashlytics")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
   // Compose dependencies are usually included by default in new projects

    //google ml kit
    implementation("com.google.mlkit:translate:17.0.3")

    // This bridges Google Play Services Tasks and Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2")

    implementation("com.google.mlkit:text-recognition:16.0.1")


    implementation("androidx.camera:camera-core:1.3.4")
    implementation("androidx.camera:camera-camera2:1.3.4")
    implementation("androidx.camera:camera-lifecycle:1.3.4")
    implementation("androidx.camera:camera-view:1.3.4")

    //Location
    implementation("com.google.android.gms:play-services-location:21.0.1")

    //chatboat
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

    //maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")

    implementation("com.razorpay:checkout:1.6.4")

    //Barcode scanning
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    implementation("com.airbnb.android:lottie:3.4.0")

    //Excel sheet
    implementation("org.apache.poi:poi:5.2.3")

    // OOXML support for .xlsx (using XSSFWorkbook)
    implementation("org.apache.poi:poi-ooxml:5.2.3")

    // Required to handle the XML structures under the hood
    implementation("com.fasterxml.woodstox:woodstox-core:6.5.0")

    //face detection
    implementation("com.google.mlkit:face-detection:16.1.7")

    //ads
    implementation("com.google.android.gms:play-services-ads:23.0.0")



}