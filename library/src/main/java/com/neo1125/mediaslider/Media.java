package com.neo1125.mediaslider;

public class Media {

    public enum Type {
        image, video;
    }

    String title;
    String uri;
    Type type;
    int drawable;

    public Media(String title, String uri) {
        this.type = Type.image;
        this.title = title;
        this.uri = uri;
    }

    public Media(Type type, String title, String uri) {
        this.type = type;
        this.title = title;
        this.uri = uri;
    }

    public Media(String title, int drawable) {
        this.type = Type.image;
        this.title = title;
        this.drawable = drawable;
    }
}
