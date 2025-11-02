import types.Order;
import types.User;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class ProductManagerTest {
    private static final Logger logger = Logger.getLogger(ProductManagerTest.class);
    ProductManagerImpl pm;

    @Before
    public void setUp(){
        logger.info("iniciando Test");
        pm = ProductManagerImpl.getInstance(); //Pillo la instancia ya creada (solo puede haber una creada ya que es singleton)

        //dar de alta productos
        pm.addProductAlMercado("P001", "Fanta naranja", 2.5);
        pm.addProductAlMercado("P002", "Bacon queso", 6.5);
        pm.addProductAlMercado("P003", "Cheeseburger", 7.5);

        //dar de alta usuarios
        pm.addUser("Juan", "U001");
        pm.addUser("Ana", "U002");
    }

    @After
    public void tearDown() {
        pm = null; //Vaciamos todas las listas, colas y mapas para el siguiente test

    }

    //test realizar un pedido
    @Test
    public void testAddOrder(){
        logger.info("Iniciando testAddorder");
        Assert.assertEquals(0, pm.numOrders()); //Verifico que no hay pedidos

        //creamos el cuerpo del pedido: 2 Fantas y 1 Cheeseburger
        HashMap<String, Integer> productosPedido = new HashMap<>();
        productosPedido.put("P001", 2);
        productosPedido.put("P003", 1);

        //crear pedido para un user
        Order order = pm.createOrder("U001", productosPedido);

        // Verificaciones
        Assert.assertNotNull("El pedido no debería ser nulo si el usuario y los productos existen", order);
        Assert.assertEquals(1, pm.numOrders());
        logger.info("testAddOrder finalizado con éxito.");
    }

    //test servir un pedido
    @Test
    public void testDeliverOrder() {
        logger.info("Iniciando testDeliverOrder");
        //para servir un pedido, primero tenemos que crear uno.
        //reutilizamos la lógica del test anterior.
        testAddOrder();
        Assert.assertEquals(1, pm.numOrders()); //Verifico que hay 1 pedido en la cola

        Order deliveredOrder = pm.deliverOrder();
        Assert.assertNotNull("El pedido servido no debería ser nulo", deliveredOrder);
        Assert.assertEquals(0, pm.numOrders());
        Assert.assertTrue("El estado del pedido debería ser 'servido'", deliveredOrder.getServido());
        logger.info("testDeliverOrder finalizado con éxito.");
    }
}
