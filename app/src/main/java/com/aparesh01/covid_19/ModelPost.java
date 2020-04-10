package com.aparesh01.covid_19;

public class ModelPost {

    String pId,pTitle,pDesc,pImage,pTime,uid,uEmail,uDp,uName;
    public ModelPost(){


    }
    public ModelPost(String pId,String pImage,String pTime,String uid,String uEmail,String uDp,String uName,String pTitle,String pDesc){

        this.pId = pId;
        this.pImage = pImage;
        this.pTime = pTime;
        this.uid = uid;
        this.uEmail = uEmail;
        this.uDp = uDp;
        this.uName = uName;
        this.pTitle = pTitle;
        this.pDesc = pDesc;

    }

    public void setpId(String pId){

        this.pId = pId;
    }
    public String getpId(){
        return pId;
    }
    public void setpImage(String pImage){
        this.pImage = pImage;
    }
    public String getpImage(){
        return pImage;
    }
    public void setpTime(String pTime){

        this.pTime = pTime;
    }
    public String getpTime(){
        return pTime;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getpDesc() {
        return pDesc;
    }


    public String getpTitle() {
        return pTitle;
    }

    public String getuDp() {
        return uDp;
    }

    public String getuEmail() {
        return uEmail;
    }

    public String getuName() {
        return uName;
    }

    public void setpDesc(String pDesc) {
        this.pDesc = pDesc;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
