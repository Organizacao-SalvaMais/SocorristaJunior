plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.hilt) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.androidx.room) apply false

    id("com.google.gms.google-services") version "4.4.4" apply false
}