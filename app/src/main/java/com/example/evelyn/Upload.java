package com.example.evelyn;

public class Upload {

    private String name,age,bloodGroup,thana,district,contact,hospital,lastDate;
    private String imageUrl;

    public Upload(){

    }
    public Upload(String n,String a,String b,String t,String d,String c,String h,String url,String lastDate){
        this.name = n;
        this.age = a;
        this.bloodGroup = b;
        this.thana = t;
        this.district = d;
        this.contact = c;
        this.hospital = h;
        this.imageUrl = url;
        this.lastDate = lastDate;

    }


    public String  getName(){
        return name;
    }
    public void setName(String n){
        this.name = n;
    }
    public String  getAge(){
        return age;
    }
    public void setAge(String n){
        this.age = n;
    }
    public String  getBloodGroup(){
        return bloodGroup;
    }
    public void setBloodGroup(String n){
        this.bloodGroup = n;
    }
    public String  getThana(){
        return thana;
    }
    public void setThana(String n){
        this.thana = n;
    }public String  getDistrict(){
        return district;
    }
    public void setDistrict(String n){
        this.district = n;
    }
    public String  getContact(){
        return contact;
    }
    public void setContact(String n){
        this.contact = n;
    }
    public String  getHospital(){
        return hospital;
    }
    public void setHospital(String n){
        this.hospital = n;
    }
    public String  getImageUrl(){
        return imageUrl;
    }
    public void setImageUrl(String n){
        this.imageUrl = n;
    }


    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }




}
