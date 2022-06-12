object Dependencies {
	object Kotlin {
		const val version = "1.6.10"
		const val androidPlugin = "org.jetbrains.kotlin.android"

		object Serialization {
			const val pluginId = "kotlinx-serialization"
			const val gradleClasspath = "org.jetbrains.kotlin:kotlin-serialization:$version"

		}
	}

	object Android {
		object Plugin {
			const val version = "7.2.1"
			const val application = "com.android.application"
		}

		object Compose {
			const val version = "1.1.0"

			const val ui = "androidx.compose.ui:ui:$version"
			const val material = "androidx.compose.material3:material3:1.0.0-alpha13"
			const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:$version"
			const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
			const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:1.0.0"
			const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$version"
			const val uiTestJunit = "androidx.compose.ui:ui-test-junit4:$version"

			const val navigation = "androidx.navigation:navigation-compose:2.4.2"
		}

		object Hilt {
			private const val version: String = "2.38.1"

			object Plugin {
				const val id: String = "dagger.hilt.android.plugin"
				const val gradleClasspath: String = "com.google.dagger:hilt-android-gradle-plugin:$version"
			}

			const val hiltAndroid = "com.google.dagger:hilt-android:$version"
			const val hiltCompiler = "com.google.dagger:hilt-compiler:$version"
		}

		object Room {
			private const val version = "2.4.2"

			const val runtime = "androidx.room:room-runtime:$version"
			const val ktx = "androidx.room:room-ktx:$version"
			const val compiler = "androidx.room:room-compiler:$version"
			const val paging = "androidx.room:room-paging:$version"
		}

		object Paging3 {
			const val runtime = "androidx.paging:paging-runtime:3.1.1"
			const val compose = "androidx.paging:paging-compose:1.0.0-alpha15"
		}

		const val dataStore = "androidx.datastore:datastore-preferences:1.0.0"

		const val coreKtx = "androidx.core:core-ktx:1.7.0"
		const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
		const val activityCompose = "androidx.activity:activity-compose:1.3.1"

		const val extJunit = "androidx.test.ext:junit:1.1.3"
		const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
	}

	object Ktor {
		private const val version = "2.0.2"
		const val client = "io.ktor:ktor-client-core:$version"
		const val engine = "io.ktor:ktor-client-android:$version"
		const val contentNegotiation = "io.ktor:ktor-client-content-negotiation:$version"
		const val kotlinXJson = "io.ktor:ktor-serialization-kotlinx-json:$version"
	}

	const val timber = "com.jakewharton.timber:timber:5.0.1"

	const val junit = "junit:junit:4.13.2"
}