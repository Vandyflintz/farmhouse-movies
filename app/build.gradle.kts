plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.gradle.versions)
}

android {
    namespace = "com.vandyflintz.farmhousemovies"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vandyflintz.farmhousemovies"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

/*dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.ucrop)
    implementation(libs.volley)
    implementation (libs.picasso)
    implementation(libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation (libs.scottyab.aescrypt)
    implementation (libs.mpandroidchart)
    implementation (libs.android.ratingreviews)
    implementation (libs.simpleratingbar)
    implementation (libs.androidx.fragment)
    implementation (libs.library)
    implementation (libs.slider.library)
    implementation (libs.glide)
    implementation (libs.annotations)
    implementation (libs.commons.text)
    implementation (libs.commons.net)
    implementation (libs.joda.time)
    implementation(libs.flexbox)
    implementation (libs.jakewharton.butterknife)
    implementation (libs.exoplayer)
    implementation (libs.karumi.dexter)
    implementation(libs.glide.okhttp3.integration)

    testImplementation(libs.junit)
    implementation (libs.retrofit2.converter.scalars)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}*/

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3) // Keep this, remove the legacy material
    implementation(libs.ucrop)
    implementation(libs.volley)
    implementation(libs.picasso)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.scottyab.aescrypt)
    implementation(libs.mpandroidchart)
    implementation(libs.android.ratingreviews)
    implementation(libs.simpleratingbar)
    implementation(libs.androidx.fragment)
    implementation(libs.library)
    implementation(libs.slider.library)
    implementation(libs.glide)
    implementation(libs.annotations)
    implementation(libs.commons.text)
    implementation(libs.commons.net)
    implementation(libs.joda.time)
    implementation(libs.flexbox)
    implementation(libs.jakewharton.butterknife)
    implementation(libs.exoplayer)
    implementation(libs.karumi.dexter)
    implementation(libs.glide.okhttp3.integration)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
