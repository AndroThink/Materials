package com.androthink.materials.callback;

public interface SelectPathCallback {
    void onSelected(String path);

    void onError(String error);
}
