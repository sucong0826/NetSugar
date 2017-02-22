# NetSugar V1.0 Stable

Hey, NetSugar stable version 1.0 is coming, why not to have a try?
![NetSugar](/images/netsugar_ad.jpg)

# ScreenShots for Situations
--------
![NetSugar](/screenshots/all_disconnect.png)
1. No matter what type of network state (connected), but wifi or mobile network is disconnected.

![NetSugar](/screenshots/mobile_disconnect.png)
2. Mobile network is required but disconnect.

![NetSugar](/screenshots/wifi_disconnect.png)
3. Wifi is required but disconnect or is a captive portal network.

![NetSugar](/screenshots/wrong_matched.png)
4. Wifi is required but mobile network now.

![NetSugar](/screenshots/normal.png)
5. Everything is ok. Claps! *^.^*

# Introduction
--------
NetSugar is an Android + AOP framework. It was a way of Aspect-Oriented Programming
with AspectJ(ApsectJ 5) on Android. It is an easy-use and lightweight framework to check network state.
Sometimes, when we need functions which are needed to check network:
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
It makes feel uncomfortable, what I want is the separation of situations. Of course, if a method with
strong logical and network state requiring, NetSugar will be helpful. What I think is a method only handle
one situation whatever the network state is. Just like below:
```
private void playVideo() {...}
```
when network is not satisfied,
```
private void offlineOfPlayingVideo() {...}
```

To be honestly, AspectJ for Android can do a lot of things, it is extremely strong and comprehensive beyond your thought.
As for me, NetSugar is just a way to learn and try to use AOP and AspectJ. Therefore, NetSugar is a crosscutting like AOP.
In the future I will do more things on Android with AOP and AspectJ, in the meanwhile, optimization, performance will be
considered more and more.

NetSugar v1.0 stable made a big step, I simplify its usage and performance, new functions are pending when they are tested.
Now Let's see now.

# Usage
--------
1. Pre-declaration
When I want to upload NetSugar to Maven with Bintray, I didn't do that.
This NetSugar is still beyond my goal, I fix it version by version.
NetSugar will be uploaded to Maven when it is good enough.
So everybody now can use it from git cloning and commit your contributions.
Thanks here.

2. Because we check network state, so you must authorize the permission of accessing network state. To add permission in your
app `AndroidManifest.xml`.

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

3. Please copy netsugar-master folder with sources into your own project as a single module.
Let your 'app' module add a dependence on it or you do this in your app `build.gradle`:

``` groovy
dependencies {
    compile project(':netsugar-master')
    compile 'org.aspectj:aspectjrt:1.8.5'
}
```

4. Please compile `'org.aspectj:aspectjrt:1.8.5'` or other versions you know such as 1.8.9+.
Just like above, oh, it should be added in your 'app' module `build.gradle`.

5. Config 'app' `build.gradle`.
If you don't know how to config, please check target project. Give a link below:
[app build.gradle configuration][1]

6. Add a Pair of Annotation
NetSugar provides three annotations for you to use:
    * `@NetSugar(type=[NetworkType], pair=[int])`
    * `@Offline(pair=[int])`
    * `@Global`

@NetSugar and @Offline are aimed at method only.<strong>Please remember, they MUST be used as a PAIR with same value.</strong>

```java
@NetSugar(type=NetworkType.WIFI, pair=0x1)
private void playVideo() {
    // only prepare playing video works.
}

@Offline(pair=0x1)
private void handlePlayVideoOffline(MatchResult result) {
    // when network work state is not satisfied, this method will be invoked.
}
```
As you see, @NetSugar and @Offline are showing with the same value of 'pair' at same time, it is NECESSARY.
If you think there is no need for actions to handle offline or network state not matching situations,
you should also provide this kind of method like above.
If you won't, `NoSuchMethodException` will be thrown. It is caused by method searching mechanism.
You may read `netsugar-master/src/main/java/su.hm.netsugar_master/aspect/aj_ns/NetSugarAspect.java` to get
more details.

7. Config 'pair' value in them
Like above, pairs are same value. Why I do that? For method hooking.
In the previous version, there was a big issue, is that when a class has two methods with the same name,
NetSugarAspect is puzzled. It will handle the execution in order. However, problems happened. Two methods
which having the same name but different arguments are designed to cope with different things, method
reflection api cannot do that just because of arguments.
Thus, this version, I change method pairing mechanism, use a 'pair' to hook method one on one.
Please note that:
    * if you won't provide @Offline or its pair value, a `NoHookException` or `WrongPairException` will be thrown.
    * if you annotate method with @Offline and same value more than one, only the first method framework checked will be executed.
    * The default pair value of @Offline is 0x0, it is used for validating inside, please don't use it. Pair value will be taken bigger than 0x0.

8. Receiving the callback argument:MatchResult
You may be inquisitive about why we provide the method with a MatchResult as above.
The method annotated with @Offline will be handled automatically, that is, there is no need to consider how
and where to invoke this method. The method is executed by NetSugar framework. As for you, just compose your
codes when this situation happen.
MatchResult is an argument which NetSugar gives you. It represents network checking result and framework has
already initialized for you, just use it like an normal object.
It will tell you `matchType(int)`, `reason(String)`, `currentNetworkType(NetworkType)`.

```java
    @Offline(pair=0x1)
    private void handleOffline(MatchResult result) {
        Toast.makeText(MainActivity.this, result.getReason(), /* time */).show();
    }
```
Please note that:
    * The method you provide must have an argument and its type must be MatchResult.
    * You can define your own arguments after MatchResult in params list.

9. Finish Injection
In some Activity, you must finish this operation:

``` java
    // this means context fo current Activity
    // onCreate(Bundle savedInstanceState)
    NetworkSugar.inject(this);
```

Alright, everything is ok, just enjoy the convenience NetSugar brings to you.(A little awkward... ^.~)

# Limitations & Issues
--------
1. If you find @Global annotation, please do not use it. It has some issues about service and broadcast.
2. Must provide correct pair value between @NetSugar and @Offline.
3. The method annotated with @Offline will receive an argument MatchResult. Must remember that.
4. Config your sample according to github project configuration when you find sample won't work.

# Something Funny
--------
When you read project carefully, the core has a new hierarchy of aspect.
Do you know? Aspect folder tortured me to death almost.
Now aspect welcomes its new hierarchy:
- aspect
____-aj_d
_________- DemoAspect.java
____-aj_global
_________- GlobalAspect.java
____-aj_ns
_________- NetSugarAspect.java

Don't ask me why? To ask for AspectJ!!! I don't know either.

# Note
--------
You'd better to config all arguments to @NetSugar, specially the method in @Offline.
Method searching depends on Java reflection, if there is no method matching, a `NoSuchMethodException`
will be thrown. The application will stop unexpectedly.

If you want to get more details, please read modification documents.
[ModifyDoc.][2]

# Thanks
--------
1.Joel Dean and his project Flender
He gave me the idea to be the whole solution to solve my project.

2.Innost
His blog helped me a lot.

3.android10
His demo on github gave me some ideas.

# Version
v1.0 (Stable)

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

[1]:https://github.com/sucong0826/NetSugar/blob/master/app/build.gradle
[2]:https://github.com/sucong0826/NetSugar/tree/master/modifications/m_v1_0.txt