package com.vg.rest.customer;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vg.model.Customer;

/**
/**
 *  TODO
 *  validate incoming fields - 400 - BAD REQUEST / 405
 *  handle duplicates -  409 CONFLICT based 
 *  Authorization - 401 - UNAUTHORIZED
 *  Add HATEOAS links for Customers and Addresses
 */
@RestController
@RequestMapping(value = "/v1/customers", produces = "application/hal+json")
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable final long customerId) {
        Optional<Customer> optionalCustomer = customerService.getCustomerById(customerId);
        if (!optionalCustomer.isPresent()) {
            throw new CustomerNotFoundException(customerId);
        }

        Customer customer = optionalCustomer.get();
        Link selfLink = linkTo(CustomerController.class).slash(customer.getCustomerId()).withSelfRel();
        customer.add(selfLink);

        return ResponseEntity.ok().cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)).body(customer);
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customerfromRequest) {
        final Customer customer = customerService.createCustomer(customerfromRequest);
        final URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{customerId}").buildAndExpand(customer.getId()).toUri();
        return ResponseEntity.created(uri).cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)).body(customer);
    }

    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customerfromRequest) {
        if (!customerService.getCustomerById(customerfromRequest.getCustomerId()).isPresent()) {
            throw new CustomerNotFoundException(customerfromRequest.getCustomerId());
        }

        final Customer customer = customerService.updateCustomer(customerfromRequest);
        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(uri).cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)).body(customer);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("customerId") Long customerId) {
        if (!customerService.getCustomerById(customerId).isPresent()) {
            throw new CustomerNotFoundException(customerId);
        }

        customerService.deleteById(customerId);
        return ResponseEntity.noContent().build();
    }
}
