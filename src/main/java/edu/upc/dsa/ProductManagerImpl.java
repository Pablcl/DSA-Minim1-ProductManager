package edu.upc.dsa;

import edu.upc.dsa.types.Product;
import edu.upc.dsa.types.User;
import edu.upc.dsa.types.Order;
import java.util.*;
import java.util.Comparator; //interfaz comparator
import org.apache.log4j.Logger;


public class ProductManagerImpl implements ProductManager { //Implementamos una fachada

    private static final Logger logger = Logger.getLogger(ProductManagerImpl.class);

    private static ProductManagerImpl instance; //Única instancia: patrón singleton (variable estática)

    private List<Product> products;
    private Queue<Order> orders;
    private HashMap<String, User> users; //id y el objeto user

    private ProductManagerImpl() {
        this.products = new ArrayList<>();
        this.orders = new LinkedList<>();
        this.users = new HashMap<>();
    }

    public String generarId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public void addProductAlMercado(String id, String name, double price) {//Aquí estoy dando de alta un producto al mercado
        logger.info("Se va a añadir el producto " + name + " identificado cómo: " + id + " y un precio de " + price + "euros");
        // Verificamos si ya existe un producto con el mismo ID
        if (this.getProduct(id) != null) {
            logger.error("No se puede añadir el producto. El ID '" + id + "' ya existe.");
            return; //salimos del metodo
        }
        products.add(new Product(id, name, 0, price));
        logger.info("Se ha añadido el producto " + name + " identificado cómo: " + id + " y un precio de " + price + "euros");
    }

    public List<Product> getProductsByPrice() {
        logger.info("Iniciando lista de prodcutos ordenada por precio");
        List<Product> copiaPrecio = new ArrayList<>(this.products);
        copiaPrecio.sort(Comparator.comparing(product -> product.getPrice()));
        logger.info("Lista de productos ordenada por precio hecha");
        return copiaPrecio;
    }

    public List<Product> getProductsBySales() {
        logger.info("Iniciando lista de prodcutos ordenada por ventas");
        List<Product> copiaVentas= new ArrayList<>(this.products);
        copiaVentas.sort(Comparator.comparing(Product::getNumofsales).reversed()); //Por defecto ordena de menor a mayor, ponemos reversed para ordenar de mayor a menor
        logger.info("Lista de prodcutos ordenada por ventas hecha");
        return copiaVentas;
    }

    public Order createOrder(String userId, HashMap<String, Integer> productosycantidades){ //Añadir una orden a la cola es hacer un push (añadir elemento al final de la cola)
            logger.info("Iniciando creación de pedido para el usuario: " + userId);
            User user = this.users.get(userId); //hashmap
            if (user == null) {
                logger.error("Creación de pedido ha fallado: El usuario con ID '" + userId + "' no existe.");
                return null; //devolvemos null para indicar el fallo.
            }

            String orderId = generarId(); //esto es para generar un ID único para el pedido
            Order nuevoOrder = new Order(orderId, userId);

            for (HashMap.Entry<String, Integer> products : productosycantidades.entrySet()) {
                String productId = products.getKey();
                Integer quantity = products.getValue();

                Product product = getProduct(productId);
                if (product == null) { //si id no existe
                    logger.error("Creación de pedido fallida: El producto con ID '" + productId + "' no existe en el catálogo.");
                    //si un solo producto no existe, cancelamos la creación del pedido completo.
                    return null;
                }

                //si el producto existe, lo añadimos al objeto Order.
                nuevoOrder.addProductsToOrder(productId, quantity);
                logger.info("Producto '" + productId + "' (cantidad: " + quantity + ") añadido al pedido.");
            }

            //pedido a la cola
            this.orders.add(nuevoOrder);
            logger.info("Pedido con ID '" + orderId + "' creado y añadido a la cola. Pedidos pendientes: " + this.orders.size());

            return nuevoOrder;
    }

    public int numOrders(){ //Estos son los pedidos que están pendientes de ser servidos
        logger.info("Consultando número de pedidos pendientes. Total: " + orders.size());
        return orders.size();
    }

    public Order deliverOrder(){ //Sirvo el pedido, lo quito de la cola
        Order orderAEntregar = orders.poll();

        if (orderAEntregar != null) {
            logger.info("Sirviendo pedido: " + orderAEntregar.getId() + " para el usuario: " + orderAEntregar.getUserId());
            orderAEntregar.setServido(true);
            User user = this.users.get(orderAEntregar.getUserId());
            if (user != null){
                user.addOrder(orderAEntregar);
                //actualizamos num ventas, como? se recorre cada enrty del pedido pillo el valor y se lo sumo al numofsales original de ese producto
                for (Map.Entry<String, Integer> entry : orderAEntregar.getProducts().entrySet()) {
                    Product p = this.getProduct(entry.getKey());
                    if (p != null) {
                        p.setNumofsales(p.getNumofsales() + entry.getValue());
                    }
                }
            }
        }
        return orderAEntregar;
    }

    public List<Order> getOrdersUserCompletados(String userId){
        logger.info("Buscando pedidos completados para el usuario: " + userId);
        User user = getUser(userId);
        if (user == null){
            logger.error("Usuario no encontrado: " + userId);
            return new ArrayList<>();
        }
        List<Order> OrdersCompletados = new ArrayList<>();
        for (Order order : user.getOrders()){
            if (order.getServido()){
                OrdersCompletados.add(order);
            }
        }
        logger.info("Se encontraron " + OrdersCompletados.size() + " pedidos completados para el usuario: " + userId);
        return OrdersCompletados;
    }

    public Product getProduct(String productId){
        logger.info("Buscando producto con ID: " + productId);
        for (Product p : products) {
            if (p.getId().equals(productId)) {
                logger.info("Producto encontrado: " + p.getName());
                return p;
            }
        }
        logger.warn("Producto con ID '" + productId + "' no encontrado.");
        return null;
    }

    public void addUser(String name, String userId){
        logger.info("Vamos a intentar añadir a user con ID: " + userId + " y nombre: " + name);
        if (this.users.containsKey(userId)){
            logger.error("No se puede añadir usuario");
            return;
        }
        User nuevouser = new User(userId, name);
        this.users.put(userId, nuevouser);
        logger.info("Usuario " + name + " con Id " + userId + " ha sido añadido");
    }

    public User getUser(String userId){
        logger.info("Buscando usuario con ID: " + userId);
        return this.users.get(userId); //Le paso clave y me da valor simplemente con un get (hashmap)
    }

    public static ProductManagerImpl getInstance(){
        if(instance == null){
            instance = new ProductManagerImpl();
        }
        return instance;
    }
}
