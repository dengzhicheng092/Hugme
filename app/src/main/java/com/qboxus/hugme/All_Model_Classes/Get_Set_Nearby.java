package com.qboxus.hugme.All_Model_Classes;

import java.io.Serializable;

public class Get_Set_Nearby implements Serializable {
    String fb_id;
    String first_name;
    String last_name;
    String birthday;
    String about_me;
    String distance;
    String gender;
    String image1;
    String Swipe;

    String job_title, company, school, living, children, smoking, drinking, relationship, sexuality, block;

    String image2, image3, image4, image5, image6;

    public Get_Set_Nearby(){}

    public Get_Set_Nearby(String fb_id, String first_name, String last_name, String birthday, String about_me,
                          String distance, String image1, String Swipe,
    String job_title, String company, String school, String living, String children, String smoking, String drinking,
                          String relationship, String sexuality, String block,
                          String image2,String image3,String image4, String image5,String image6

                          ) {
        this.fb_id = fb_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.birthday = birthday;
        this.about_me = about_me;
        this.distance = distance;
        this.image1 = image1;
        this.Swipe = Swipe;
        this.job_title = job_title;

        this.company = company;
        this.school = school;
        this.living = living;
        this.children = children;
        this.smoking = smoking;
        this.drinking = drinking;
        this.relationship = relationship;
        this.sexuality = sexuality;
        this.block = block;

        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.image6 = image6;


    }


    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public String getImage6() {
        return image6;
    }

    public void setImage6(String image6) {
        this.image6 = image6;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getSwipe() {
        return Swipe;
    }

    public void setSwipe(String swipe) {
        Swipe = swipe;
    }


    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getLiving() {
        return living;
    }

    public void setLiving(String living) {
        this.living = living;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getDrinking() {
        return drinking;
    }

    public void setDrinking(String drinking) {
        this.drinking = drinking;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getSexuality() {
        return sexuality;
    }

    public void setSexuality(String sexuality) {
        this.sexuality = sexuality;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }
}
