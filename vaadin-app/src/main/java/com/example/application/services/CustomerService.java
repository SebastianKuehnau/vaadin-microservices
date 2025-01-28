package com.example.application.services;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class CustomerService {

    public record User(Long id, String name, String email) {
    }

    public record Order(Long id, Long userId, String product, Double price) {
    }

    public record Customer(User user, List<Order> orders) {
    }

    private final WebClient.Builder webClientBuilder;

    @Value("${user-service.url}")
    private String userServiceUrl;

    @Value("${order-service.url}")
    private String orderServiceUrl;

    public CustomerService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }


    public List<Customer> getCustomers() {
        // Call user service to get all users
        var users = getUsers();
        return users.stream()
                .map(user -> {
                    // Call order service to get all orders for each user
                    var orders = getOrders(user.id());
                    return new Customer(user, orders);
                })
                .toList();
    }

    private List<User> getUsers() {
        WebClient userClient = webClientBuilder.baseUrl(userServiceUrl).build();
        var users = userClient.get()
                .uri("/users")
                .retrieve()
                .bodyToFlux(User.class)
                .collectList()
                .block();

        return users;
    }

    private List<Order> getOrders(Long userId) {
        WebClient orderClient = webClientBuilder.baseUrl(orderServiceUrl).build();
        var orders = orderClient.get()
                .uri("/orders/user/" + userId)
                .retrieve()
                .bodyToFlux(Order.class)
                .collectList()
                .block();

        return orders;
    }
}
