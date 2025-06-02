plugins {
    id("com.android.application")
    kotlin("android") version "1.9.10"
    kotlin("kapt") // Para Room Database
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(20)
    }
}

kapt {
    correctErrorTypes = true
}

android {
    namespace = "com.example.gerenciamentodeviagem"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.gerenciamentodeviagem"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "20" // Use o jvmTarget compatível com JDK 20
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_20
        targetCompatibility = JavaVersion.VERSION_20
    }
}

dependencies {
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    val composeBom = platform("androidx.compose:compose-bom:2024.03.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation("androidx.compose.ui:ui-tooling-preview-android:1.7.8")
    implementation("androidx.compose.ui:ui:1.4.0")
    implementation("androidx.compose.material:material:1.6.0")
    implementation("androidx.compose.material:material-icons-extended:1.4.0")
    implementation("androidx.compose.foundation:foundation:1.4.0")

}
