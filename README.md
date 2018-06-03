# A JADE'S MODERN AGENT

This library aims to provide a more modern and easy to use JADE agent.

### Installing

Add the [JitPack.io](http://jitpack.io) repository to the project `build.grade`:
```
repositories {
    maven { url 'https://jitpack.io' }
    maven { url 'http://jade.tilab.com/maven/' }
}
```

Then import the latest version in the `build.gradle` of the modules you need:

```
dependencies {
    implementation 'com.github.Lamba92:jade-modern-agent:{latest_version}'
    implementation 'com.tilab.jade:jade:4.5.0'
    implementation 'commons-codec:commons-codec:1.9'
}
```
Latest version: [![](https://jitpack.io/v/Lamba92/jade-modern-agent.svg)](https://jitpack.io/#Lamba92/jade-modern-agent)

If using Gradle Kotlin DSL:
```
repositories {
    maven(url = "https://jitpack.io")
    maven(url = "http://jade.tilab.com/maven/")
}
...
dependencies {
    implementation("com.github.Lamba92", "jade-modern-agent", "{latest_version}")
    implementation("com.tilab.jade", "jade", "4.5.0")
    implementation("commons-codec", "commons-codec", "1.9")
}
```
If you are using Maven, switch to Gradle, it's 2018.

**Note that this this library already include latest JADE distribution and documentation. Do not import it manually.**
### Usage

Just extend a class using `ModernAgent` as parent and implement it's methods and you should be good to go:
```
class MyAgent: ModernAgent() {
   
    override fun onMessageReceived(message: ACLMessage) {}
    override fun onCreate(args: Array<String>) {}
    override fun onDestroy() {}
    override fun onDispose() {} 
}
```

`startListeningMessages()` and `stopListeningMessages()` allows you to manage `ACLMessages`.

`searchAgents()` searches for an agent through the `DFService`.