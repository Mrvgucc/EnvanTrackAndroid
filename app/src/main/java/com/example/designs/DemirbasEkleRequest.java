package com.example.designs;

import com.google.gson.annotations.SerializedName;

public class DemirbasEkleRequest {

    @SerializedName("name")
    private String name;

    @SerializedName("category_id")
    private int catefory_id;

    @SerializedName("registered_personal")
    private Integer employeeId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCatefory_id() {
        return catefory_id;
    }

    public void setCatefory_id(int catefory_id) {
        this.catefory_id = catefory_id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
