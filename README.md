GBAndroidWidget
===============

### Download

Clone the repository and import as Android Library in eclipse or grab via maven or gradle:

##### Maven
```xml
<dependency>
    <groupId>com.github.pidygb.android</groupId>
    <artifactId>pidygbandroid</artifactId>
    <version>(insert latest version)</version>
</dependency>
```
##### Gradle
```groovy
dependencies {
   compile 'com.github.pidygb.android:pidygbandroid:+'
}
```
### Usage

*For a working implementation of this project see the `PiDyGBAndroidSample/` folder.*

#### Layout

Use the following layouts in your xml.

```xml
<com.github.pidygb.android.widget.SlidingLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:expanded="true|false"
    app:duration="300">
</com.github.pidygb.android.widget.SlidingLayout>
```

```xml
<com.github.pidygb.android.widget.SlidingLinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:expanded="true|false"
    app:duration="300">
</com.github.pidygb.android.widget.SlidingLinearLayout>
```
    
##### When:

`app:expanded` : if the start status of the layout is expanded or not
`app:duration` : the animation duration in millis

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

#### Views

Use the following views in your xml:

```xml
<com.github.pidygb.android.widget.Button
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="text"
    app:typeface="Ubuntu-Regular.ttf"
    app:scaleLetterSpacing="2.5"
    app:textUpperCase="true"/>

<com.github.pidygb.android.widget.EditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:inputType="text"
    app:typeface="Ubuntu-Regular.ttf"/>
        
<com.github.pidygb.android.widget.TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="Ubuntu Regular font"
    app:typeface="Ubuntu-Regular.ttf"
    app:scaleLetterSpacing="2.5"
    app:textUpperCase="true"/>
```

##### When:

`app:typeface` : A font file present in ***assets/fonts*** folder
`app:scaleLetterSpacing` : A scale factor for letter spacing
`app:textUpperCase` : force the text in uppercase
