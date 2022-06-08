object Dependencies {
    object Kotlin {
        val version = "1.6.10"
        val androidPlugin = "org.jetbrains.kotlin.android"
    }

    object Android {
        object Plugin {
            val version = "7.2.1"
            val application = "com.android.application"
        }

        object Compose {
            val version = "1.1.0"

            val ui = "androidx.compose.ui:ui:$version"
            val material = "androidx.compose.material:material:$version"
            val toolingPreview = "androidx.compose.ui:ui-tooling-preview:$version"
            val uiTooling = "androidx.compose.ui:ui-tooling:$version"
            val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$version"
            val uiTestJunit = "androidx.compose.ui:ui-test-junit4:$version"
        }

        val coreKtx = "androidx.core:core-ktx:1.7.0"
        val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
        val activityCompose = "androidx.activity:activity-compose:1.3.1"

        val extJunit = "androidx.test.ext:junit:1.1.3"
        val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
    }

    val junit = "junit:junit:4.13.2"
}