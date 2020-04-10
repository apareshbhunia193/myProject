package com.aparesh01.covid_19.doc;

public class DoctorsList {

    String email,image,cover,name,job,uid;
    DoctorsList(String name,String email,String job,String image, String cover,String uid){
        this.name = name;
        this.email = email;
        this.job = job;
        this.image = image;
        this.cover = cover;
        this.uid = uid;
    }
    DoctorsList(){

    }

    public String getName() {
        return name;
    }

    public String getCover() {
        return cover;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getJob() {
        return job;
    }

    public String getUid() {
        return uid;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String toString(){
        return this.name;
    }
}
