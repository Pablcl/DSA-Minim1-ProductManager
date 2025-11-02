package edu.upc.dsa.types;
import java.util.*;

public class Order {

    private String id ;
    private String userId;
    private HashMap<String, Integer> products; //Para saber que productos van en cada pedido
    private boolean servido;

    public Order(String id, String userId) {
        this.id = id;
        this.userId = userId;
        this.products = new HashMap<>();
        this.servido = false;
    }

    //digamos que el addProducts y getProducts son como el set y get de un atributo pero es un HashMap
    public void addProductsToOrder(String productName, int quantity){
        this.products.put(productName, quantity);
    }
    public HashMap<String, Integer> getProducts(){ //que me de productos de un pedido
        return products;
    }
    public void setId (String id){
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }
    public void setServido (boolean servido){
        this.servido = servido;
    }
    public boolean getServido(){
        return servido;
    }
}
