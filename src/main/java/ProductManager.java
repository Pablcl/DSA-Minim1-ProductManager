import types.Order;
import types.Product;
import types.User;
import java.util.*;

import java.util.List;


public interface ProductManager {

    public void addProductAlMercado(String id, String name, double price);
    public List<Product> getProductsByPrice(); //ascendentemente
    public List<Product> getProductsBySales(); //descendentemente
    public Order createOrder(String userId, HashMap<String, Integer> productosycantidades);
    public int numOrders();
    public Order deliverOrder(); //servir pedido
    public Product getProduct(String productId); //es bucar un producto (recorrido lista) a partir de su id
    public User getUser(String userId);
    public void addUser(String name, String userId);
    public List<Order> getOrdersUserCompletados(String userId);

}
