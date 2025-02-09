package com.ps.foodstore.Data;

public class FoodStoreData {
    private String name,size,image,product_id,description;
    private int price;


    public FoodStoreData(String name, int price, String size, String image, String product_id, String description) {
        this.name = name;
        this.price = price;
        this.size = size;
        this.image = image;
        this.product_id = product_id;
        this.description=description;
    }





    public FoodStoreData() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
