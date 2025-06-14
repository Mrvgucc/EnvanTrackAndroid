package com.example.designs;

public class Asset {
    private int id;
    private String name;
    private Category category_id;
    private RegisteredPersonel registered_personal;
    private String usage_status;
    private String created_at;
    private String updated_at;
    private boolean isEditMode = false;


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

    public Category getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Category category_id) {
        this.category_id = category_id;
    }

    public RegisteredPersonel getRegistered_personal() {
        return registered_personal;
    }

    public void setRegistered_personal(RegisteredPersonel registered_personal) {
        this.registered_personal = registered_personal;
    }

    public String getUsage_status() {
        return  usage_status;
    }

    public void setUsage_status(String  usage_status) {
        this. usage_status =  usage_status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean getIsEditMode(){
        return isEditMode;
    }

    public void setEditMode(boolean editMode){
        isEditMode = editMode;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + (category_id != null ? category_id.getName() : "null") +
                '}';
    }
}

