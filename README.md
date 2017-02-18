# NetSugar

![NetSugar](/gifs/display.gif)
this gif is displaying a situation...

# Introduction
NetSugar is an Android + AOP production. This open source project was created by way of Aspect-Oriented Program
with AspectJ(ApsectJ 5) on Android. It is a simple and lightweight framework to check network state. Sometimes,
when we write functions which are needed to check network:
```sample
void playVideo() {
	boolean isNetworkConnected = NetworkUtils.isConnect(SomeActivity.this);
	if (isNetworkConnected) {
		// playVideo
	} else {
		// do some logical operations
	}
}
```
When I got the idea to change this, I've tried a lot of ways to solve it but failed until I was aware of
AOP and ApsectJ. I analyzed an Annotation open source and then I tried my best to finish NetSugar by AspectJ
and annotation.

Therefore, NetSugar just solve this small piece of code and change it into an annotation.

# Usage
--------
1.first, using `NetworkSugar.inject(this);` to finish an injection.
```
public class MainActivity extends AppCompatActvity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		// just like this
        NetworkSugar.inject(this);
		...
    }
}
```

2.To annotate a method with `@NetSugar` annotation. Please do not consider the offline situation.
`offline = @Offline(method = "offline")` will handle it.
```
@NetSugar(
            check = true,
            online = @Online(type = NetworkType.WIFI),
            offline = @Offline(method = "offline"))
    public void playVideo(int x) {
		// play video action without offline situation.
        videoPlayer.play();
    }
```
`check` means that the method needs to check network state. true: check; false: not check.
`online` means that what the type of network is the method requiring.
`type` is an enumeration which has MOBILE / WIFI / ALL, there types.
`offline` has a method, means that when network is not connected, which method should to handle it.

3.Declares a method that you give in @Offline.
```
// this method to handle offline
public void offline() {
	// do some notifications by a toast or a dialg.
	// or other logical operations.
}
```

4.Now, there is no need to do anything, AspectJ is coming.

# Note
You'd better to config all arguments to @NetSugar, specially the method in @Offline.
Method searching depends on Java reflection, if there is no method matching, a `NoSuchMethodException`
will be thrown. The application will stop unexpectly.

# Configuration
We finish NetSugar with AOP and AspectJ, it is hard for me to config the app gradle and module gradle. It
spent me a lot of time. Android Studio and your project do not support AspectJ and its compiling env.
We should complete configuration manually.
Joel Dean with his Open-Source Flender provides a kind of configuration, that is plug-in by groovy.
For us, we could only configs the app and project gradle file.

1.app build.gradle
```
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'org.aspectj:aspectjtools:1.8.5'
    }
}

apply plugin: 'com.android.application'

repositories {
    mavenCentral()
}

android {
    compileSdkVersion 25
    buildToolsVersion "25.0.2"
    defaultConfig {
        applicationId "su.hm.netsugar"
        minSdkVersion 9
        targetSdkVersion 25
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"

        jackOptions {
            enabled true
        }
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    compile 'com.android.support:appcompat-v7:25.1.1'
    testCompile 'junit:junit:4.12'
    compile 'org.aspectj:aspectjrt:1.8.5'
    compile project(path: ':netsugar-master')
}

final def log = project.logger
final def variants = project.android.applicationVariants

variants.all { variant ->
    if (!variant.buildType.isDebuggable()) {
        log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
        return;
    }

    JavaCompile javaCompile = variant.javaCompile
    javaCompile.doLast {
        String[] args = ["-showWeaveInfo",
                         "-1.7",
                         "-inpath", javaCompile.destinationDir.toString(),
                         "-aspectpath", javaCompile.classpath.asPath,
                         "-d", javaCompile.destinationDir.toString(),
                         "-classpath", javaCompile.classpath.asPath,
                         "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
        log.debug "ajc args: " + Arrays.toString(args)

        MessageHandler handler = new MessageHandler(true);
        new Main().run(args, handler);
        for (IMessage message : handler.getMessages(null, true)) {
            switch (message.getKind()) {
                case IMessage.ABORT:
                case IMessage.ERROR:
                case IMessage.FAIL:
                    log.error message.message, message.thrown
                    break;
                case IMessage.WARNING:
                    log.warn message.message, message.thrown
                    break;
                case IMessage.INFO:
                    log.info message.message, message.thrown
                    break;
                case IMessage.DEBUG:
                    log.debug message.message, message.thrown
                    break;
            }
        }
    }
}
```
You can config it according to my file and find difference between the two files.

# Thanks
1.Joel Dean and his project Flender
He gave me the idea to be the whole solution to solve my project.

2.Innost
His blog helped me a lot.

3.android10
His demo on github gave me some ideas.

# Version
0.1.2

# Declaration
NetSugar looks like a baby for me though she has a lot of drawbacks and bugs.
I will maintain it to make her better and better.
I really want some guys to do it together after all it is AOP.
Codes and functions are not enough and good, your suggestions, advices, even your contribution will make
me appreciated. Thank you.

#License
--------

    Copyright 2017 SuCong from Huami

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
