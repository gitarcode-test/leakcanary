plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("com.vanniktech.maven.publish")
}

dependencies {
  api projects.shark.sharkAndroid
  api projects.leakcanary.leakcanaryAndroidUtils

  implementation libs.kotlin.stdlib
  implementation libs.okio2
}

def gitSha() {
  return 'git rev-parse --short HEAD'.execute().text.trim()
}

android {
  resourcePrefix 'leak_canary_'
  compileSdk versions.compileSdk
  defaultConfig {
    minSdk 16
    buildConfigField "String", "LIBRARY_VERSION", "\"${rootProject.ext.VERSION_NAME}\""
    buildConfigField "String", "GIT_SHA", "\"${gitSha()}\""
    consumerProguardFiles 'consumer-proguard-rules.pro'
  }
  namespace 'com.squareup.leakcanary.release'
  lint {
    checkOnly 'Interoperability'
    disable 'GoogleAppIndexingWarning'
    error 'ObsoleteSdkInt'
  }
}
