package com.example.datascrapper.Model;

public class SettingsFragmentModel {

    String email;
    String full_name;
    int tasks_completed;
    int tasks_pending;
    int total_tasks;

    public SettingsFragmentModel() {

    }

    public SettingsFragmentModel(String email, String full_name, int tasks_completed, int tasks_pending, int total_tasks) {
        this.email = email;
        this.full_name = full_name;
        this.tasks_completed = tasks_completed;
        this.tasks_pending = tasks_pending;
        this.total_tasks = total_tasks;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public int getTasks_completed() {
        return tasks_completed;
    }

    public void setTasks_completed(int tasks_completed) {
        this.tasks_completed = tasks_completed;
    }

    public int getTasks_pending() {
        return tasks_pending;
    }

    public void setTasks_pending(int tasks_pending) {
        this.tasks_pending = tasks_pending;
    }

    public int getTotal_tasks() {
        return total_tasks;
    }

    public void setTotal_tasks(int total_tasks) {
        this.total_tasks = total_tasks;
    }
}


