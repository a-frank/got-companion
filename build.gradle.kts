plugins {
	id(Dependencies.Android.Plugin.application) version Dependencies.Android.Plugin.version apply false
	id(Dependencies.Kotlin.androidPlugin) version Dependencies.Kotlin.version apply false
}

buildscript {
	dependencies {
		classpath(Dependencies.Android.Hilt.Plugin.gradleClasspath)
		classpath(Dependencies.Kotlin.Serialization.gradleClasspath)
	}
}