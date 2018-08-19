package com.discount.mgt.resapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.discount.mgt.dao.db.CustomerDao;
import com.discount.mgt.dao.db.ProductDao;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class DiscountMgtApiWebContextTest {
	

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	
	@Autowired
	private CustomerDao custRepo;
	
	@Autowired
	private ProductDao prodRepo;
	

	@Before
	public void setup() {
		
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@After
	public void tearDown() {
		this.custRepo.deleteAll();
		this.prodRepo.deleteAll();
	}
	
	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:product.sql")
	public void getDiscount_FOR_CUSTOMER_TO_PRODUCTS() throws Exception {
		
		mockMvc.perform(get("/discount/getDiscount?customerId=1&productIds=1").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk())
			   .andExpect(content().string(
						"[{\"productId\":1,\"customerId\":1,\"salesPrice\":30.00,\"discountPrice\":21.60,\"appliedDiscounts\":\", Promotion, Employee discount\"}]"));
	}
	
	
	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:product.sql")
	public void getDiscount_FOR_GUEST_USER() throws Exception {

		mockMvc.perform(get("/discount/getDiscount").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(
						"[{\"productId\":1,\"salesPrice\":30.00,\"discountPrice\":24.00,\"appliedDiscounts\":\", Promotion\"},{\"productId\":2,\"salesPrice\":25.00,\"discountPrice\":23.75,\"appliedDiscounts\":\", Special Privilege\"}]"));
	}
	
	@Test
	public void getDiscount_FOR_BAD_INPUTS() throws Exception {
		mockMvc.perform(get("/discount/getDiscount?customerId=null&productIds=null").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().is(422));
	}
	
	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:product.sql")
	public void getDiscount_FOR_SPECIFIC_PRODUCT_WITHOUT_CUSTOMER_DETAILS() throws Exception {
		mockMvc.perform(get("/discount/getDiscount?productIds=2").contentType(MediaType.APPLICATION_PROBLEM_JSON))
		       .andExpect(status().isOk())
		       .andExpect(content().string("[{\"productId\":2,\"salesPrice\":25.00,\"discountPrice\":23.75,\"appliedDiscounts\":\", Special Privilege\"}]"));
	}
	
	@Test
	@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:product_with_invalid_price.sql")
	public void getDiscount_WITH_NULL_DISCOUNT_PRICE() throws Exception {
		mockMvc.perform(get("/discount/getDiscount?productIds=3").contentType(MediaType.APPLICATION_PROBLEM_JSON))
		       .andExpect(status().is(500))
		       .andExpect(content().string("{\"title\":\"Business Rule Failure\",\"detail\":\"Eexecution failed for rule 5% Special discount on Sparkasse VISA Credit card shopping in the month of august for either of productIds '3 ' because of Invalid attribute data\",\"instance\":\"/discount/getDiscount\",\"status\":500}"));
	}	
	
	
	
}
