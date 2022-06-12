plugins {
	id(Dependencies.Android.Plugin.application)
	id(Dependencies.Kotlin.androidPlugin)
	id(Dependencies.Android.Hilt.Plugin.id)
	id(Dependencies.Kotlin.Serialization.pluginId)
	kotlin("kapt")
}

android {
	compileSdk = 32

	defaultConfig {
		applicationId = "de.gnarly.got"
		minSdk = 21
		targetSdk = 32
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		getByName("release") {
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
		kotlinOptions {
			freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
		}
	}
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = Dependencies.Android.Compose.version
	}
	packagingOptions {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {
	implementation(Dependencies.Android.coreKtx)

	implementation(Dependencies.Android.Compose.ui)
	implementation(Dependencies.Android.Compose.material)
	implementation(Dependencies.Android.Compose.toolingPreview)
	implementation(Dependencies.Android.Compose.navigation)
	implementation(Dependencies.Android.Compose.hiltNavigation)

	implementation(Dependencies.Android.lifecycleRuntimeKtx)
	implementation(Dependencies.Android.activityCompose)

	implementation(Dependencies.Android.Hilt.hiltAndroid)
	kapt(Dependencies.Android.Hilt.hiltCompiler)

	implementation(Dependencies.Android.Room.runtime)
	implementation(Dependencies.Android.Room.ktx)
	implementation(Dependencies.Android.Room.paging)
	kapt(Dependencies.Android.Room.compiler)

	implementation(Dependencies.Ktor.client)
	implementation(Dependencies.Ktor.engine)
	implementation(Dependencies.Ktor.contentNegotiation)
	implementation(Dependencies.Ktor.kotlinXJson)

	implementation(Dependencies.Android.Paging3.runtime)
	implementation(Dependencies.Android.Paging3.compose)

	implementation(Dependencies.Android.dataStore)

	implementation(Dependencies.timber)

	debugImplementation(Dependencies.Android.Compose.uiTooling)
	debugImplementation(Dependencies.Android.Compose.uiTestManifest)

	testImplementation(Dependencies.junit)
	testImplementation(Dependencies.mockk)
	testImplementation(Dependencies.truth)

	androidTestImplementation(Dependencies.Android.extJunit)
	androidTestImplementation(Dependencies.Android.espressoCore)
	androidTestImplementation(Dependencies.Android.Compose.uiTestJunit)
}

kapt {
	correctErrorTypes = true
}