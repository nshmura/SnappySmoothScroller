# SnappySmoothScroller
An Android library that enables snappy smooth scrolling in RecyclerView.

![DEMO](assets/demo.gif)

## Samples
<a href="https://play.google.com/store/apps/details?id=com.nshmura.snappysmoothscroller.demo"><img src="assets/googleplay.png"/></a>

## Getting Started

In your `build.gradle`:

```gradle
 repositories {
    jcenter()
 }

 dependencies {
    compile 'com.nshmura:snappysmoothscroller:1.0.0'
 }
```

Setup the `SnappyLayoutManager`:
```java
// Instantiate layout manager
layoutManager = new SnappyLinearLayoutManager(context);

// Set the SnapType
layoutManager.setSnapType(SnapType.CENTER);

// Set the Interpolator
layoutManager.setSnapInterpolator(new DecelerateInterpolator());

// Attach layout manager to the RecyclerView:
recyclerView.setLayoutManager(layoutManager);
```

Call `smoothScrollToPosition(int)`:
```java
recyclerView.smoothScrollToPosition(position);
```

## SnappyLayoutManager

There is same pre-set SnappyLayoutManager inherits existing layout manager:

- `SnappyLinearLayoutManager` (subclass of LinearLayoutManager)
- `SnappyGridLayoutManager` (subclass of GridLayoutManager)
- `SnappyStaggeredGridLayoutManager` (subclass of StaggeredGridLayoutManager)


## Change the behavior

You can change the behavior:

```java
//Change the SnapType. SnapType indicates the stoping position of smooth scroll.
layoutManager.setSnapType(SnapType.CENTER);

//Change the durations for snap animation.
layoutManager.setSnapDuration(1000);

//Change the interpolator for the snaping animation.
layoutManager.setSnapInterpolator(new DecelerateInterpolator());

//Change the padding for the start parts of the view.
layoutManager.setSnapPaddingStart(10);

//Change the padding for the end parts of the view.
layoutManager.setSnapPaddingEnd(10);

//Change the padding for the start and end parts of the view.
layoutManager.setSnapPadding(10);

//Change the durations for the seeking animation.
layoutManager.setSeekDuration(1000);
```

## Attatching SnappySmoothScroller Directly

You can also set the `SnappySmoothScroller` to layout manager by overriding the `smoothScrollToPosition` method:
```java
layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        SnappySmoothScroller scroller = new SnappySmoothScroller.Builder()
                .setPosition(position)
                .setScrollVectorDetector(new LinearLayoutScrollVectorDetector(this))
                .build(recyclerView.getContext());

        startSmoothScroll(scroller);
    }
};
recyclerView.setLayoutManager(layoutManager);
```

## Thanks
[ Customizing SmoothScroller for the RecyclerView](https://mcochin.wordpress.com/2015/05/13/android-customizing-smoothscroller-for-the-recyclerview/)

## License
```
Copyright (C) 2016 nshmura

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
