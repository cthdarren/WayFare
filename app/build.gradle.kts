plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.example.wayfare"
    compileSdk = 34
    packaging{
        resources{
            excludes.add("META-INF/native-image/org.mongodb/bson/native-image.properties")
            excludes.add("META-INF/INDEX.LIST")
            excludes.add("META-INF/AL2.0")
            excludes.add("META-INF/LGPL2.1")
            excludes.add("META-INF/io.netty.versions.properties")
        }
    }
    defaultConfig {
        applicationId = "com.example.wayfare"
        minSdk = 26
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
            buildConfigField("String", "API_URL", "\"http://api.tingtangwalawalabingbang.com\"")
            signingConfig = signingConfigs.getByName("debug")
        }
        debug {
//            buildConfigField("String", "API_URL", "\"http://10.0.2.2:8080\"")
            buildConfigField("String", "API_URL", "\"http://api.tingtangwalawalabingbang.com\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        dataBinding = true
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.libraries.places:places:3.4.0")
    implementation("id.zelory:compressor:3.0.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("org.mongodb:mongodb-driver-sync:4.11.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.activity:activity:1.8.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.google.android.gms:play-services-auth:20.3.0")
    // media3 formally exo Player
    val media3_version = "1.3.0"
    implementation("androidx.media3:media3-exoplayer:$media3_version")
    implementation("androidx.media3:media3-exoplayer-dash:$media3_version")
    implementation("androidx.media3:media3-ui:$media3_version")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.21")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    val camerax_version = "1.2.2"
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-video:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")
    implementation("androidx.camera:camera-extensions:$camerax_version")
    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation(platform("com.azure:azure-sdk-bom:1.2.22"))
    implementation("com.azure:azure-storage-blob")
    implementation("com.azure:azure-identity")
    implementation("com.azure:azure-storage-queue")
    implementation("com.azure:azure-storage-file-share")
    implementation("com.azure:azure-storage-file-datalake")
    implementation("com.azure:azure-security-keyvault-secrets")
    implementation("com.google.maps:google-maps-services:2.2.0")
    implementation("org.slf4j:slf4j-simple:1.7.25")
//    implementation("androidx.media3:media3-exoplayer-dash:1.3.1")
    implementation("com.arthenica:mobile-ffmpeg-full-gpl:4.4")
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.intuit.ssp:ssp-android:1.0.6")
    implementation("de.hdodenhof:circleimageview:3.1.0")
}