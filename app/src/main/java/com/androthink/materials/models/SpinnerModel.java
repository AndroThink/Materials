package com.androthink.materials.models;

import android.graphics.drawable.Drawable;

public class SpinnerModel {

    private int id;
    private String name;
    private Drawable drawable;
    private boolean selectable;

    public SpinnerModel(int id, String name, Drawable drawable, boolean selectable) {
        this.id = id;
        this.name = name;
        this.drawable = drawable;
        this.selectable = selectable;
    }

    public SpinnerModel(int id, String name, Drawable drawable) {
        this.id = id;
        this.name = name;
        this.drawable = drawable;
        this.selectable = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }
}
