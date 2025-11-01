import types.Order;
import types.Product;
import types.User;

import java.util.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class ProductManagerTest {

    ProductManagerImpl pm;

    @Before
    public void setUp(){
        pm = ProductManagerImpl.getInstance(); //Pillo la instancia ya creada (solo puede haber una creada ya que es singleton)
        pm.addProduct("001", "Fanta naranja", 2, 2.5); //Doy de alta al mercado un producto
        pm.addProduct("002", "Bacon queso", 3, 6.5);
        pm.addProduct("003", "Cheeseburger", 5, 7.5);

    }

    @After
    public void tearDown() {
        pm = null; //Vacíar listas, colas etc
    }

    //test realizar un pedido
    @Test
    public void testAddOrder(){
        Assert.assertEquals(0, pm.numOrders()); //Verifico que no hay pedidos
        Order order = new Order("13403932", "3423");
        order.addProducts("Fanta", 3); //Añado producto al pedido
        order.addProducts("Bacon queso", 2);
        pm.addOrder(order);
        Assert.assertEquals(1, pm.numOrders());

    }

    //test servir un pedido
    @Test
    public void testDeliverOrder() {
        Assert.assertEquals(1, pm.numOrders()); //Verifico que no hay pedidos
        pm.deliverOrder();
        Assert.assertEquals(0, pm.numOrders());
        //Assert.assertNull();
    }
}
