import types.Order;
import types.Product;
import types.User;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;


public interface ProductManager {

    public void addProduct(String id, String name, int numofsales, double price);

    public List<Product> getProductsByPrice();
    public List<Product> getProductsBySales();

    public void addOrder(Order order);

    public int numOrders();

    public Order deliverOrder();

    Product getProduct(String c1);

    User getUser(String number);


}
