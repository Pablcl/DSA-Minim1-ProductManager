package types;
import java.util.*;

public class User {

    private String id;
    private String name;

    private List<Order> orders;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.orders = new LinkedList<>();
    }
    public void setId (String id){
        this.id = id;
    }
    public void setName (String name) {
        this.name = name;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public void addOrder (Order order){
        this.orders.add(order);

    }
}
