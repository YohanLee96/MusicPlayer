package com.massivcode.androidmusicplayer.models;


public class Member {
    private String departmentNo;

    private String department;

    private String password;

    private String name;

    public Member(String departmentNo, String department, String password, String name) {
        this.departmentNo = departmentNo;
        this.department = department;
        this.password = password;
        this.name = name;
    }

    public String getDepartmentNo() {
        return departmentNo;
    }

    public String getDepartment() {
        return department;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
