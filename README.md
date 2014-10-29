android-slidinglayout
===============

### Download

Clone the repository and import as Android Library in eclipse or grab via maven or gradle:

##### Maven
```xml
<dependency>
    <groupId>com.github.pidygb</groupId>
    <artifactId>slidinglayout</artifactId>
    <version>(insert latest version)</version>
</dependency>
```
##### Gradle
```groovy
dependencies {
   compile 'com.github.pidygb:slidinglayout:+'
}
```
### Usage

*For a working implementation of this project see the `sample/` folder.*

#### Layout

Use the following layouts in your xml.

```xml
<com.github.pidygb.slidinglayout.widget.SlidingLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:expanded="true|false"
    app:duration="300">
</com.github.pidygb.slidinglayout.widget.SlidingLayout>
```

```xml
<com.github.pidygb.slidinglayout.widget.SlidingLinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:expanded="true|false"
    app:duration="300">
</com.github.pidygb.slidinglayout.widget.SlidingLinearLayout>
```
    
##### When:

* `app:expanded` : if the start status of the layout is expanded or not
* `app:duration` : the animation duration in millis

Fill these layouts with your Views via xml or programmatically

Setup a SlideListener or a SlideListenerAdapter for animation callbacks:

```java
new SlidingLinearLayout().setSlideListener(new SlideListener() {
    @Override
    public void onSlideStart(View slidingView) {
        // Do something
    }

    @Override
    public void onSlideEnd(View slidingView) {
        // Do something
    }
});
```

## License

    Copyright 2014 Giuseppe Buzzanca

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
