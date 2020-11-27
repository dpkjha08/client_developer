package com.example.datascrapper.Model;

public class TaskFragmentModel {

    String assigned_to_email;
    String assigned_to_name;
    String end_time;
    String task_description;
    String task_name;
    String status;
    String task_id;
    String project_name;
    String project_desc;

    public TaskFragmentModel() {
    }

    public TaskFragmentModel(String assigned_to_email, String assigned_to_name, String end_time, String task_description, String task_name,String status, String task_id, String project_name, String project_desc) {
        this.assigned_to_email = assigned_to_email;
        this.assigned_to_name = assigned_to_name;
        this.end_time = end_time;
        this.task_description = task_description;
        this.task_name = task_name;
        this.status = status;
        this.task_id = task_id;
        this.project_desc = project_desc;
        this.project_name = project_name;
    }

    public String getAssigned_to_email() {
        return assigned_to_email;
    }

    public void setAssigned_to_email(String assigned_to_email) {
        this.assigned_to_email = assigned_to_email;
    }

    public String getAssigned_to_name() {
        return assigned_to_name;
    }

    public void setAssigned_to_name(String assigned_to_name) {
        this.assigned_to_name = assigned_to_name;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getTask_description() {
        return task_description;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
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
