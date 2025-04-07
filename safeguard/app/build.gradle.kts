plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.sg.safeguard"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sg.safeguard"
        minSdk = 35
        targetSdk = 35
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
    buildFeatures {
        viewBinding = true
    }
    // Thêm khối packaging để xử lý file trùng lặp
    packaging {
        resources {
            excludes += "META-INF/NOTICE.md" // Loại bỏ file trùng lặp
            excludes += "META-INF/LICENSE.md" // Nếu có thêm file khác trùng, thêm vào đây
        }
    }
}

dependencies {
    implementation(libs.play.services.maps)
    implementation(libs.material.v1120)
    implementation(libs.android.mail)
    implementation(libs.android.activation)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}