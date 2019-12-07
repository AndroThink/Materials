package com.androthink.materials.models;

public class FileFolderModel {

    private String name;
    private boolean folder;

    public FileFolderModel(String name, boolean folder) {
        this.name = name;
        this.folder = folder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }
}
