package com.shaishavgandhi.sales;

/**
 * Created by Shaishav on 25-09-2015.
 */
public class Orders {

    private long id;
    private long sold_at;
    private float cost_price;
    private float selling_price;
    private long prod_id;
    private long quantity;

    public long getId(){return id;}

    public void setId(long id){this.id=id;}

    public long getSold_at(){return sold_at;}

    public void setSold_at(long sold_at){this.sold_at=sold_at;}

    public float getCost_price(){return cost_price;}

    public void setCost_price(float cost_price){this.cost_price=cost_price;}

    public float getSelling_price(){return selling_price;}

    public void setSelling_price(float selling_price){this.selling_price=selling_price;}

    public long getProd_id(){return prod_id;}

    public void setProd_id(long prod_id){this.prod_id=prod_id;}

    public long getQuantity(){return quantity;}

    public void setQuantity(long quantity){this.quantity=quantity;}
}
