plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.developer.randomusers"
    compileSdk = libs.versions.targetSdkVersion.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdkVersion.get().toInt()

        consumerProguardFiles("consumer-rules.pro")
    }

    packaging {
        resources {
            excludes.add("/META-INF/LICENSE.md")
            excludes.add("/META-INF/LICENSE-notice.md")
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
        val javaVersion = libs.versions.javaVersion.get().toInt()
        sourceCompatibility = JavaVersion.toVersion(javaVersion)
        targetCompatibility = JavaVersion.toVersion(javaVersion)
    }
    kotlinOptions {
        jvmTarget = libs.versions.javaVersion.get()
    }
}

dependencies {

    implementation(projects.app)
    implementation(libs.androidx.core.ktx)
    implementation(libs.retrofit)

    api(libs.kotlinx.coroutines.test)
    api(libs.junit.jupiter.api)

    api(libs.junit.jupiter.engine)
    api(libs.androidx.compose.ui.test.junit4)

    api(platform(libs.androidx.compose.bom))


}