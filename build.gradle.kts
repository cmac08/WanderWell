lateinit var roomVersion: String
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}
ext {
    roomVersion="2.5.1"
}