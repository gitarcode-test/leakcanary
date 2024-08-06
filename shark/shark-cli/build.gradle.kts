import org.jetbrains.kotlin.gradle.dsl.KotlinCompile

plugins {
  id("org.jetbrains.kotlin.jvm")
  id("application")
  id("com.vanniktech.maven.publish")
}

sourceCompatibility = JavaVersion.VERSION_1_8
targetCompatibility = JavaVersion.VERSION_1_8

// Workaround for https://stackoverflow.com/questions/48988778
// /cannot-inline-bytecode-built-with-jvm-target-1-8-into-bytecode-that-is-being-bui
tasks.withType(KotlinCompile).configureEach {
  kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

dependencies {
  api projects.shark.sharkAndroid

  implementation libs.clikt
  implementation libs.neo4j
  implementation libs.jline
  implementation libs.kotlin.stdlib
}

application {
  mainClassName = 'shark.MainKt'
}

def generatedVersionDir = "${buildDir}/generated-version"

sourceSets {
  main {
    output.dir(generatedVersionDir, builtBy: 'generateVersionProperties')
  }
}

tasks.register("generateVersionProperties") {
  doLast {
    def propertiesFile = file "$generatedVersionDir/version.properties"
    propertiesFile.parentFile.mkdirs()
    def properties = new Properties()
    properties.setProperty("version_name", rootProject.VERSION_NAME.toString())
    propertiesFile.withWriter { properties.store(it, null) }
  }
}
tasks.named("processResources") {
  dependsOn("generateVersionProperties")
}

