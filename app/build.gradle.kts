plugins {
    id("com.android.application")
    kotlin("android") version "1.9.10" // Atualize a versão do Kotlin para 1.9.10
    kotlin("kapt") // Para Room Database
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
        jvmTarget = "1.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.7.8")
    val composeBom = platform("androidx.compose:compose-bom:2024.03.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation ("androidx.compose.ui:ui:1.4.0") // ou a versão mais recente
    implementation ("androidx.compose.material:material:1.4.0") // para Material Components
    implementation ("androidx.compose.material:material-icons-extended:1.4.0") // para ícones
    implementation ("androidx.compose.foundation:foundation:1.4.0") // para o layout
    implementation ("androidx.navigation:navigation-compose:2.5.0") // para navegação
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.0") // para ViewModel
}
