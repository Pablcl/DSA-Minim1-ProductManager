package types;
import java.util.*;

public class Product {

    private String id;
    private String name;
    private int numofsales;
    private double price;

    public Product(String id, String name, int numofsales, double price) {
        this.id = id;
        this.name = name;
        this.numofsales = numofsales;
        this.price = price;
    }

    public void setId (String id){
        this.id = id;
    }
    public void setName (String name) {
        this.name = name;
    }
    public void setNumofsales (int numofsales) {
        this.numofsales = numofsales;
    }
    public void setPrice (double price) {
        this.price = price;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public int getNumofsales(){
        return numofsales;
    }
    public double getPrice(){
        return price;
    }


}
