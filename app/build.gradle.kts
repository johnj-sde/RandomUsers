import org.gradle.kotlin.dsl.testImplementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.devtools.ksp")
}

android {
    namespace = "com.developer.randomusers"
    compileSdk = libs.versions.targetSdkVersion.get().toInt()

    defaultConfig {
        applicationId = "com.developer.randomusers"
        minSdk = libs.versions.minSdkVersion.get().toInt()
        targetSdk = libs.versions.targetSdkVersion.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.developer.randomusers.TestAppRunner"


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
        val javaVersion = libs.versions.javaVersion.get()
        sourceCompatibility = JavaVersion.toVersion(javaVersion)
        targetCompatibility = JavaVersion.toVersion(javaVersion)
    }

    kotlinOptions {
        jvmTarget = libs.versions.javaVersion.get()
    }

    buildFeatures {
        compose = true
    }

    testOptions.unitTests {
        all { tests ->
            tests.useJUnitPlatform()
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.koin)
    implementation(libs.koin.compose)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.serialization.converter)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.coil.compose)
    implementation(libs.coil.network)

    implementation(libs.room.runtime)
    ksp(libs.room.ksp)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter.api)

    testRuntimeOnly(libs.junit.jupiter.engine)
  //  testImplementation(kotlin("test"))

    androidTestImplementation(platform(libs.androidx.compose.bom))


}