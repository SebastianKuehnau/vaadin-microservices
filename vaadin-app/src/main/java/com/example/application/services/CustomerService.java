package com.example.application.services;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@BrowserCallable
@AnonymousAllowed
public class CustomerService {

    public record User(Long id, String name, String email) {
    }

    public record Order(Long id, Long userId, String product, Double price) {
    }

    public record Customer(User user, List<Order> orders) {
    }

    @Value("${user-service.url}")
    private String userServiceUrl;

    @Value("${order-service.url}")
    private String orderServiceUrl;

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
        RestClient userRestClient = RestClient.builder()
                .baseUrl(userServiceUrl)
                .build();

        return Optional.ofNullable(userRestClient.get()
                    .uri("/users")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<User>>() {}))
                .orElse(Collections.emptyList());
    }

    private List<Order> getOrders(Long userId) {
        RestClient orderRestClient = RestClient.builder()
                .baseUrl(orderServiceUrl)
                .build();

        return Optional.ofNullable(
                orderRestClient.get()
                    .uri("/orders/user/%s".formatted(userId.toString()))
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Order>>() {
                })).orElse(Collections.emptyList());
    }
}
