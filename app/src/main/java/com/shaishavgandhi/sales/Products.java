package com.shaishavgandhi.sales;

/**
 * Created by Shaishav on 22-05-2015.
 */
public class Products {


    private long id;
    private String name;
    private boolean sold;
    private String description;
    private float cost_price;
    private float selling_price;
    private float discount_price;
    private String category;
    private String objectId,image;
    private long created_at,sold_at;
    private long quantity;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setSold(boolean sold){
        this.sold=sold;
    }
    public boolean getSold(){
        return sold;
    }

    public String getDescription(){return description;}

    public void setDescription(String description){this.description=description;}

    public void setCost_price(float cost_price){
        this.cost_price=cost_price;
    }
    public float getCost_price(){
        return cost_price;
    }
    public float getSelling_price(){
        return selling_price;
    }
    public void setSelling_price(float selling_price){
        this.selling_price=selling_price;
    }

    public void setCategory(String category){
        this.category=category;
    }

    public String getCategory(){
        return category;
    }

    public float getDiscount_price(){
        return discount_price;
    }
    public void setDiscount_price(float discount_price){
        this.discount_price=discount_price;
    }

    public void setObjectId(String objectId){
        this.objectId = objectId;
    }

    public String getObjectId(){
        return objectId;
    }

    public long getCreatedAt(){return created_at;}

    public void setCreated_at(long created_at){this.created_at=created_at;}

    public long getSold_at(){return sold_at;}

    public void setSold_at(long sold_at){this.sold_at=sold_at;}

    public void setImage(String image){this.image=image;}

    public String getImage(){return image;}

    public long getQuantity(){return quantity;}

    public void setQuantity(long quantity){this.quantity=quantity;}

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return name;
    }
}

