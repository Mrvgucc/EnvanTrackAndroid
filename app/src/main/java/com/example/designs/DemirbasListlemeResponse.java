package com.example.designs;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DemirbasListlemeResponse {

    @SerializedName("assets")
    private List<Asset> assets;

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }
}
