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

    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")

    //Crashlytics
    implementation("com.google.firebase:firebase-crashlytics")

}