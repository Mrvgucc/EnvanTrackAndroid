package com.example.designs;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EmployeeListResponse {

    @SerializedName("employees")
    private List<Employee> employees;

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
