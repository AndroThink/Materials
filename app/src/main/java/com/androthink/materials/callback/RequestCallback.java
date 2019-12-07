package com.androthink.materials.callback;

public interface RequestCallback {
    void onCallback(String[] permissions, boolean[] grantResults);
}
