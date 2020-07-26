package com.example.datascrapper.Model;

public class ProjectsModel {
    String project_name;
    String project_desc;

    private ProjectsModel(){}

    private ProjectsModel(String project_name,String project_desc){
        this.project_name = project_name;
        this.project_desc = project_desc;

    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_desc() {
        return project_desc;
    }

    public void setProject_desc(String project_desc) {
        this.project_desc = project_desc;
    }
}
