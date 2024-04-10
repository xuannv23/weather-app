plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.myappweather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myappweather"
        minSdk = 29
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
    implementation("com.android.volley:volley:1.2.1") //thu vien doc du lieu tu url
    implementation("com.squareup.picasso:picasso:2.8") //doc du lieu hinh anh thong qua internet
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")


    //noinspection GradleCompatible
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}