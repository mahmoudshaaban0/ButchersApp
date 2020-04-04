package com.mahmoudshaaban.butchers.pojo;

public class CategoriesModel {

    private int categorie_Image;
    private String categorie_Name;


    public CategoriesModel(int categorie_Image, String categorie_Name) {
        this.categorie_Image = categorie_Image;
        this.categorie_Name = categorie_Name;
    }

    public int getCategorie_Image() {
        return categorie_Image;
    }

    public void setCategorie_Image(int categorie_Image) {
        this.categorie_Image = categorie_Image;
    }

    public String getCategorie_Name() {
        return categorie_Name;
    }

    public void setCategorie_Name(String categorie_Name) {
        this.categorie_Name = categorie_Name;
    }
}
