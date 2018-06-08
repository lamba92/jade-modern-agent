import org.gradle.internal.impldep.org.apache.commons.collections.Closure
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.gradle.LinkMapping
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    var kotlinVersion: String by extra
    var dokkaVersion: String by extra
    kotlinVersion = "1.2.41"
    dokkaVersion = "0.9.17"

    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", kotlinVersion))
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")
    }
}

plugins {
    java
    maven
}

group = "com.github.lamba92"
version = "1.0"

apply {
    plugin("kotlin")
    plugin("org.jetbrains.dokka")
}

val kotlinVersion: String by extra

repositories {
    mavenCentral()
    maven(url="http://jade.tilab.com/maven/")
}

dependencies {
    implementation("com.tilab.jade", "jade", "4.5.0")
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation(kotlin("reflect", kotlinVersion))
    implementation("commons-codec", "commons-codec", "1.9")
    testImplementation("junit", "junit", "4.12")
}

java.sourceSets["main"].resources.srcDir(file("src/main/java/jade/gui/images"))

//val dokkaHub = tasks.withType<DokkaTask> {
//    outputFormat    = "html"
//    outputDirectory = "src/docs"
//    jdkVersion = 8
//    val mapping = LinkMapping().apply {
//        dir = "src/docs"
//        url = "https://raw.githubusercontent.com/lamba92/jade-modern-agent/master/src/main/docs"
//    }
//    linkMappings = arrayListOf(mapping)
//}

val dokka by tasks.getting(DokkaTask::class) {
    outputFormat = "html"
    outputDirectory = "src/main/docs"
    jdkVersion = 8
    val mapping = LinkMapping().apply {
        dir = "src/docs"
        url = "https://raw.githubusercontent.com/lamba92/jade-modern-agent/master/src/main/docs"
    }
    linkMappings = arrayListOf(mapping)
}
val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    classifier = "javadoc"
    from(dokka)
}
val sourcesJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles sources JAR"
    classifier = "sources"
    from(java.sourceSets.getByName("main").allSource)
}
val javaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles sources JAR"
    classifier = "sources"
    from(java.sourceSets.getByName("main").allSource)
}

artifacts.add("archives", javaJar)
artifacts.add("archives", sourcesJar)
artifacts.add("archives", dokkaJar)

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}