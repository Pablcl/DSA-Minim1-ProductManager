package edu.upc.dsa.services;

import edu.upc.dsa.ProductManager;
import edu.upc.dsa.ProductManagerImpl;
//import edu.upc.dsa.transfer.OrderRequest;
import edu.upc.dsa.types.Order;
import edu.upc.dsa.types.Product;
import edu.upc.dsa.types.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/tienda", description = "Endpoint to Shop Service")
@Path("/tienda")
public class ProductService {

    private final ProductManager pm;

    public ProductService() {
        this.pm = ProductManagerImpl.getInstance();

        //datos de ejemplo para que la API no empiece vacía
        if (this.pm.getProduct("p1") == null) {
            this.pm.addProductAlMercado("p1", "Bocata de Jamón", 3.50);
            this.pm.addProductAlMercado("p2", "Café con Leche", 1.20);
            this.pm.addProductAlMercado("p3", "Croissant", 1.00);

            this.pm.addUser("Juan", "user1");
            this.pm.addUser("Anna", "user2");
        }
    }

    @GET
    @ApiOperation(value = "get all products sorted by price", notes = "Returns a list of products sorted ascending by price")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Product.class, responseContainer="List"),
    })
    @Path("/productos/ordenados_por_precio")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsByPrice() {
        List<Product> products = this.pm.getProductsByPrice();
        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
        return Response.status(200).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "get all products sorted by sales", notes = "Returns a list of products sorted descending by sales")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Product.class, responseContainer="List"),
    })
    @Path("/productos/ordenados_por_ventas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsBySales() {
        List<Product> products = this.pm.getProductsBySales();
        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
        return Response.status(200).entity(entity).build();
    }

    @POST
    @ApiOperation(value = "create a new Order", notes = "Creates a new order for a user")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=Order.class),
            @ApiResponse(code = 404, message = "User or Product not found"),
            @ApiResponse(code = 500, message = "Validation Error")
    })
    @Path("/pedidos/nuevo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrder(Order order) {
        if (order.getUserId() == null || order.getProducts() == null) {
            return Response.status(500).entity("Request is not valid").build();
        }
        Order pedidocreado = pm.createOrder(order.getUserId(), order.getProducts());
        if (pedidocreado == null) {
            return Response.status(404).entity("User or Product not found").build();
        }
        return Response.status(201).entity(pedidocreado).build();
    }

    @PUT
    @ApiOperation(value = "deliver an Order", notes = "Serves the first order in the queue")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Order.class),
            @ApiResponse(code = 404, message = "No pending orders")
    })
    @Path("/orders/servir")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deliverOrder() {
        Order order = this.pm.deliverOrder();
        if (order == null) {
            return Response.status(404).entity("No pending orders").build();
        }
        return Response.status(200).entity(order).build();
    }

    @GET
    @ApiOperation(value = "get completed orders by user", notes = "Returns the list of completed orders for a given user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful", response = Order.class, responseContainer="List"),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/usuarios/{userId}/tuspedidos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompletedOrdersByUser(@PathParam("userId") String userId) {
        List<Order> orders = this.pm.getOrdersUserCompletados(userId);
        // El metodo original devuelve una lista vacía si el usuario no existe, así que no podemos devolver 404 fácilmente.
        // Lo correcto sería que el manager devolviera null o una excepción. Por ahora, siempre será 200.
        GenericEntity<List<Order>> entity = new GenericEntity<List<Order>>(orders) {};
        return Response.status(200).entity(entity).build();
    }

}
