# RotateLabelView
>This is library to help you to add a sticky rotation label into your view.

## Import into your project

```
implementation 'com.qifan.rotateLabelView:rotateLabelView:1.0.0'
```

## ScreenShots
 <img src="https://raw.githubusercontent.com/underwindfall/blogAssets/master/lib/RotationLabel/1.png" width="200px" />
 <img src="https://raw.githubusercontent.com/underwindfall/blogAssets/master/lib/RotationLabel/2.png" width="200px" />
 <img src="https://raw.githubusercontent.com/underwindfall/blogAssets/master/lib/RotationLabel/3.png" width="200px" />
 <img src="https://raw.githubusercontent.com/underwindfall/blogAssets/master/lib/RotationLabel/4.png" width="200px" />

## Example Usage
```xml
 <com.qifan.library.RotateLabelView
        android:id="@+id/rotate_label"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        app:background_color="@color/colorAccent"
        app:label="Android"
        app:layout_constraintEnd_toEndOf="@id/image"
        app:layout_constraintTop_toTopOf="@id/image"
        app:text_color="@android:color/white"
        app:text_size="15sp" />
```

## Custom Attributes

| attr        | type          | default | meaning |
| ------------- |:-------------:| -----:|:-------------:|
| label    | string | NONE |  label content text|
| text_color     | color      |   android.R.color.white | label color|
| background_color| color      |    android.R.color.black |label background color|
| text_size| dimension      |    30f |label text size|
| rotation_angle| integer      |    45 |label rotation angle|
| type| enum      |    TOP_RIGHT |label rotation position(TOP_RIGHT,TOP_LEFT,BOTTOM_RIGHT,BOTTOM_LEFT)|

## Available Setter Attrs Function
```kotlin
interface RotateLabelComponent {
    fun setLabel(text: Label)
    fun setLabel(@StringRes textRes: Int)
    fun setBackgroundColor(@ColorRes color: Int)
    fun setTextColor(@ColorRes color: Int)
    fun setTextSize(textSize: Float)
    fun setType(type: RotateLabelView.Type)
}
```

## License

```
MIT License

Copyright (c) 2019 Qifan Yang

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
