package com.example.customerservice;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllUsers() {
        return customerRepository.findAll();
    }

    public Customer getUserById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

}
