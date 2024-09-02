package com.example.uiuxapp;

public class BannersModel {

    String id, banner_image, redirect_link, display_order, display_time, created_on, created_by, status;

    public BannersModel(String id, String banner_image, String redirect_link, String display_order, String display_time, String created_on, String created_by, String status) {
        this.id = id;
        this.banner_image = banner_image;
        this.redirect_link = redirect_link;
        this.display_order = display_order;
        this.display_time = display_time;
        this.created_on = created_on;
        this.created_by = created_by;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getRedirect_link() {
        return redirect_link;
    }

    public void setRedirect_link(String redirect_link) {
        this.redirect_link = redirect_link;
    }

    public String getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(String display_order) {
        this.display_order = display_order;
    }

    public String getDisplay_time() {
        return display_time;
    }

    public void setDisplay_time(String display_time) {
        this.display_time = display_time;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
