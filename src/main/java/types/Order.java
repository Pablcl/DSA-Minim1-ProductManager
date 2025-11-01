package types;
import java.util.*;

public class Order {

    private String id ;
    private String userId;
    private HashMap<String, Integer> products; //Para saber que productos van en cada pedido

    public Order(String id, String userId) {
        this.id = id;
        this.userId = userId;
        this.products = new HashMap();
    }

    //digamos que el addProducts y getProducts son como el set y get de un atributo pero es un HashMap
    public void addProducts(String ProductName, int quantity){
        this.products.put(ProductName, quantity);
    }
    public HashMap<String, Integer> getProducts(){
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
}
