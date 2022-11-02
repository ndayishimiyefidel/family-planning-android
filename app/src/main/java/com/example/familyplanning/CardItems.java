package com.example.familyplanning;

public class CardItems {
    private String advantages;
    private String disadvantages;
    private String category;
    private String period;
    private  String methodname;
    private String usage;
    private String imgurl;

    public CardItems() {
    }

    public CardItems(String advantages, String disadvantages, String category, String period, String methodname, String usage, String imgurl) {
        this.advantages = advantages;
        this.disadvantages = disadvantages;
        this.category = category;
        this.period = period;
        this.methodname = methodname;
        this.usage = usage;
        this.imgurl = imgurl;
    }

    public String getAdvantages() {
        return advantages;
    }


    public String getDisadvantages() {
        return disadvantages;
    }


    public String getCategory() {
        return category;
    }


    public String getPeriod() {
        return period;
    }


    public String getMethodname() {
        return methodname;
    }



    public String getUsage() {
        return usage;
    }


    public String getImgurl() {
        return imgurl;
    }

}

