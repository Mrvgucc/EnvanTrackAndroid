package com.example.designs;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssetListWithIdResponse {

    @SerializedName("count")
    private int count;

    @SerializedName("employee_id")
    private String id;

    @SerializedName("assets")
    private List<Asset> assets;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }
}
