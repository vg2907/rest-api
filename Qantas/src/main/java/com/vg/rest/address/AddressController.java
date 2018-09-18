package com.vg.rest.address;

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

import com.vg.model.Address;
import com.vg.model.Customer;

/**
 * 
 * @author varsha.gopal
 *
 */
@RestController
@RequestMapping(value = "v1/customers/{customerId}/addresses", produces = "application/hal+json")
public class AddressController {

    @GetMapping("/{addressId}")
    public ResponseEntity<Customer> get(@PathVariable final long addressId) {
        // TODO
        return null;
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Customer> create(@RequestBody Address address) {
        // TODO
        return null;
    }

    @PutMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<Customer> update(@RequestBody Customer customerfromRequest) {
        // TODO
        return null;
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<?> delete(@PathVariable("addressId") Long addressId) {
        // TODO
        return null;
    }
}
