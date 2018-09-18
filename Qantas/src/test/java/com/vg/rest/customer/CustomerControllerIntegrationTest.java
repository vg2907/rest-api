package com.vg.rest.customer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vg.QantasApplication;
import com.vg.model.Address;
import com.vg.model.Customer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = QantasApplication.class)
@AutoConfigureMockMvc
public class CustomerControllerIntegrationTest {

	@Autowired
	private CustomerService customerService;

	@Autowired
    private MockMvc mvc;
	
	Customer customer;
	Customer modifiedCustomer;
	String a;
	
	@Before
    public void setUp() throws Exception {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate date = LocalDate.now();
		LocalDate parsedDate = LocalDate.parse(date.format(formatter), formatter);
		/** fix json issue with date **/
		
		customer = new Customer(Long.valueOf(1l), "Jane", "Doe", null, new Address("homeAdd",
				"offAdd", "mail@dom.com"));
		
		modifiedCustomer = new Customer(Long.valueOf(1l), "John", "Doe", null, new Address("homeAdd",
				"offAdd", "mail@dom.com"));
    }

	@Test
    public void whenValidInput_thenCRUD() throws IOException, Exception {
        mvc.perform(post("/v1/customers") 
				.contentType(MediaType.APPLICATION_JSON_VALUE+ ";charset=UTF-8")
				.content(toJson(customer)))
				.andExpect(status().isCreated()); 
		
        Optional<Customer> customer = customerService.getCustomerById(1l);
        assertEquals(true , customer.isPresent());
        assertEquals("Jane" , customer.get().getFirstName());
        
    	this.mvc.perform(put("/v1/customers") 
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(toJson(modifiedCustomer)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("firstName", is("John")));

		customer = customerService.getCustomerById(1l);
		assertEquals(true, customer.isPresent());
		assertEquals("John", customer.get().getFirstName());
		
		this.mvc.perform(delete("/v1/customers/{customerId}", Long.valueOf(1l)))
				.andExpect(status().isNoContent());
		
		customer = customerService.getCustomerById(1l);
		assertEquals(false, customer.isPresent());
		
		this.mvc.perform(get("/v1/customers/1")
				.accept(MediaType.ALL))
				.andExpect(status().isNotFound())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE + ";charset=UTF-8"))
				.andExpect(jsonPath("$[0].message", is("Customer could not be found with id: 1")));
    }

	static String toJson(Object object) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.writeValueAsString(object);
	}
}
