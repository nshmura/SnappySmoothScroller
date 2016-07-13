package com.nshmura.snappysmoothscroller.demo;

public enum DemoType {
    Demo("Demo"),
    Basic("Basic"),
    LinearVertical(" LinearLayout (Vertical)"),
    LinearHorizontal(" LinearLayout (Horizontal)"),
    GridVertival("GridLayout (Vertical)"),
    GridHorizontal("GridLayout (Horizontal)"),
    StaggeredGridVertival("StaggeredGridLayout (Vertical)"),
    StaggeredGridHorizontal("StaggeredGridLayout (Horizontal)"),
    Basic2("Set SnappySmoothScroller Directly");

    public final String title;

    DemoType(String title) {
        this.title = title;
    }
}
