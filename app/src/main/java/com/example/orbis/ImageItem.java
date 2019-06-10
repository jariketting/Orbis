package com.example.orbis;

import java.io.Serializable;

class ImageItem implements Serializable {
    Integer id;
    String URI;

    ImageItem(Integer id, String URI) {
        this.id = id;
        this.URI = URI;
    }
}
