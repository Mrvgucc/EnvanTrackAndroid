package com.example.designs;

import com.google.gson.annotations.SerializedName;

public class DemirbaslGuncelleRequest {

    @SerializedName("name")
    private String name;

    @SerializedName("category_id")
    private int category_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

}
