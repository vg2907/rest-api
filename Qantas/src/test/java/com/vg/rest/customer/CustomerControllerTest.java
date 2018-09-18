package com.vg.rest.customer;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vg.model.Address;
import com.vg.model.Customer;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerService customerService;

    Customer customer;
    Customer modifiedCustomer;

    @Before
    public void setUp() throws Exception {
        customer = new Customer(Long.valueOf(1l), "Jane", "Doe", null, new Address("homeAdd", "offAdd", "mail@dom.com"));

        modifiedCustomer = new Customer(Long.valueOf(1l), "John", "Doe", null, new Address("homeAdd", "offAdd", "mail@dom.com"));
    }

    @Test
    public void test_get_200() throws Exception {
        given(this.customerService.getCustomerById(1)).willReturn(Optional.ofNullable(customer));

        // @formatter:off
		this.mvc.perform(get("/v1/customers/1")
				.accept(MediaType.ALL_VALUE))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
				.andExpect(jsonPath("firstName", is("Jane")));
		// @formatter:on

    }

    @Test
    public void test_get_NOT_FOUND() throws Exception {
        given(this.customerService.getCustomerById(1)).willReturn(Optional.ofNullable(null));

        // @formatter:off
		this.mvc.perform(get("/v1/customers/1")
				.accept(MediaType.ALL_VALUE))
				.andExpect(status().isNotFound())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
				.andExpect(jsonPath("$[0].message", is("Customer could not be found with id: 1")));
		// @formatter:on

    }

    @Test
    public void test_create_201() throws Exception {
        given(this.customerService.createCustomer(customer)).willReturn(customer);

        // @formatter:off
		this.mvc.perform(post("/v1/customers") 
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(toJson(customer)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isCreated()); 
		// @formatter:on

    }

    @Test
    public void test_update_201() throws Exception {
        given(this.customerService.getCustomerById(1)).willReturn(Optional.ofNullable(customer));
        given(this.customerService.updateCustomer(customer)).willReturn(modifiedCustomer);

        // @formatter:off
		this.mvc.perform(put("/v1/customers") 
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(toJson(customer)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("firstName", is("John")));; 
		// @formatter:on

    }

    @Test
    public void test_delete_204_NO_CONTENT() throws Exception {
        given(this.customerService.getCustomerById(1)).willReturn(Optional.ofNullable(customer));

        // @formatter:off
		this.mvc.perform(delete("/v1/customers/{customerId}", Long.valueOf(1l)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isNoContent());
		// @formatter:on

    }

    @Test
    public void test_delete_NOT_FOUND() throws Exception {
        given(this.customerService.getCustomerById(1)).willReturn(Optional.ofNullable(customer));

        // @formatter:off
		this.mvc.perform(delete("/v1/customers/{customerId}", Long.valueOf(2l)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$[0].message", is("Customer could not be found with id: 2")));
		// @formatter:on

    }

    static String toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.writeValueAsString(object);
    }

}
