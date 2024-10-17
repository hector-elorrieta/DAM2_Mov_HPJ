plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.fusisapk_java"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fusisapk_java"
        minSdk = 23
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

//    implementation("androidx.navigation:navigation-fragment-ktx:2.8.2")
//    implementation("androidx.navigation:navigation-ui-ktx:2.8.2")

    implementation("androidx.fragment:fragment:1.8.4")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}