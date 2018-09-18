package com.vg.rest.customer;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.vg.model.Customer;

@Service
public class CustomerService {

    // temporary datastore
    private Map<Long, Customer> customerRepo = new ConcurrentHashMap<>();

    public Optional<Customer> getCustomerById(final long customerId) {
        return Optional.ofNullable(customerRepo.get(customerId));
    }

    public Customer createCustomer(final Customer customer) {
        // Use test id for now
        customerRepo.putIfAbsent(customer.getCustomerId(), customer);
        return customerRepo.get(customer.getCustomerId());
    }

    public Customer updateCustomer(final Customer customer) {
        // blindly updating for now
        customerRepo.put(customer.getCustomerId(), customer);
        return customerRepo.get(customer.getCustomerId());
    }

    public void deleteById(Long id) {
        customerRepo.remove(id);
    }
}
