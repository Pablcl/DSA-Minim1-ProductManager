import types.Product;
import types.User;
import types.Order;
import java.util.*;
import java.util.Comparator;
import org.apache.log4j.Logger;


public class ProductManagerImpl implements ProductManager { //Implementamos una fachada

    private static final Logger logger = Logger.getLogger(ProductManagerImpl.class);

    private static ProductManagerImpl instance; //Única instancia: patrón singleton (variable estática)

    private List<Product> products;
    private Queue<Order> orders;
    private HashMap<String, User> users;

    private ProductManagerImpl() {
        this.products = new ArrayList<>();
        this.orders = new LinkedList<>();
        this.users = new HashMap<>();
    }

    public void addProduct(String id, String name, int numofsales, double price) {
        logger.info("Se va a añadir el producto" + name + "identificado cómo:" + id + "con" + numofsales + "ventas y un precion de" + price + "euros");
        products.add(new Product(id, name, numofsales, price));
    }

    public List<Product> getProductsByPrice() {
        products.sort(Comparator.comparing(product -> product.getPrice()));
        return this.products;
    }

    public List<Product> getProductsBySales() {
        products.sort(Comparator.comparing(Product::getNumofsales).reversed()); //Por defecto ordena de menor a mayor, ponemos reversed para ordenar de mayor a menor
        return this.products;
    }

    public void addOrder(Order order){ //Añadir una orden a la cola es hacer un push (añadir elemento al final de la cola)
        orders.add(order);
    }

    public int numOrders(){
        return orders.size();
    }

    public Order deliverOrder(){
        return orders.poll();
    }

    public Product getProduct(String name){
        for (Product p : products) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    public User getUser(String userId){
        return this.users.get(userId); //Le paso clave y me da valor simplemente con un get (hashmap)
    }

    public static ProductManagerImpl getInstance(){
        if(instance == null){
            instance = new ProductManagerImpl();
        }
        return instance;
    }



}

