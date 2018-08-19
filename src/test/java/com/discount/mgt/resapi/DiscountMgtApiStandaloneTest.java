package com.discount.mgt.resapi;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.discount.mgt.advice.DiscountErrorResponseAdvice;
import com.discount.mgt.dto.ProductDiscount;
import com.discount.mgt.exception.DiscountRuleException;
import com.discount.mgt.restapi.DiscountMgtApi;
import com.discount.mgt.service.impl.DiscountService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DiscountMgtApiStandaloneTest.DiscountMgtApiConfiguration.class)
@WebAppConfiguration
public class DiscountMgtApiStandaloneTest {
	

	private MockMvc mockMvc;
	
	
	@Autowired
	private DiscountService discountService;
	
	
	@Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new DiscountMgtApi(this.discountService))
                .setControllerAdvice(new DiscountErrorResponseAdvice())
                .build();

        Mockito.reset(this.discountService);
    }
	
	@Test
	public void getDiscount_For_Apparel() throws Exception {
		
		ProductDiscount pDiscount = new ProductDiscount();
		BigDecimal actualPrice = new BigDecimal("30.00");
		pDiscount.setSalesPrice(actualPrice);
		BigDecimal discountPrice = new BigDecimal("25.65");
		pDiscount.setDiscountPrice(discountPrice);
		pDiscount.setAppliedDiscounts("Employee discount, Cashback");
		pDiscount.setSalesDate(LocalDate.now());
		pDiscount.setProductDivision("APPAREL");
		pDiscount.setProductId(1L);
		pDiscount.setCustomerId(1L);
        
        List<ProductDiscount> prodDiscList = Stream.of(pDiscount).collect(Collectors.toList());
        List<Long> products = Stream.of(1L).collect(Collectors.toList());
        
        when(this.discountService.getProductDiscount(1L, products)).thenReturn(prodDiscList);
        
        MvcResult mvcResult = mockMvc.perform(get("/discount/getDiscount?customerId=1&productIds=1").contentType(MediaType.APPLICATION_JSON))
	       .andExpect(status().isOk())
		   .andExpect(content().contentType(MediaType.APPLICATION_JSON+";charset=UTF-8")).andReturn();
        
        JsonArray jsonObj = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), JsonArray.class);
        Assert.assertEquals(pDiscount.getDiscountPrice().longValue(), jsonObj.get(0).getAsJsonObject().get("discountPrice").getAsLong());
	}
	
	@Test
	public void getDiscount_For_No_Product_And_Customer_Inputs() throws Exception {
		
		ProductDiscount pDiscount = new ProductDiscount();
		BigDecimal actualPrice = new BigDecimal("30.00");
		pDiscount.setSalesPrice(actualPrice);
		BigDecimal discountPrice = new BigDecimal("25.65");
		pDiscount.setDiscountPrice(discountPrice);
		pDiscount.setAppliedDiscounts("Employee discount, Cashback");
		pDiscount.setSalesDate(LocalDate.now());
		pDiscount.setProductDivision("APPAREL");
		pDiscount.setProductId(1L);
		pDiscount.setCustomerId(1L);
		
		ProductDiscount pftwDiscount = new ProductDiscount();
		pftwDiscount.setSalesPrice(new BigDecimal("30.00"));
		pftwDiscount.setDiscountPrice(new BigDecimal("28.65"));
		pftwDiscount.setAppliedDiscounts("Cashback");
		pftwDiscount.setSalesDate(LocalDate.now());
		pftwDiscount.setProductDivision("FOOTWEAR");
		pftwDiscount.setProductId(2L);
		pftwDiscount.setCustomerId(1L);
        
        List<ProductDiscount> prodDiscList = Stream.of(pDiscount,pftwDiscount).collect(Collectors.toList());
        List<Long> products = Stream.of(0L).collect(Collectors.toList());
        
        when(this.discountService.getProductDiscount(0L, products)).thenReturn(prodDiscList);
        
        MvcResult mvcResult = mockMvc.perform(get("/discount/getDiscount").contentType(MediaType.APPLICATION_JSON))
	       .andExpect(status().isOk())
		   .andExpect(content().contentType(MediaType.APPLICATION_JSON+";charset=UTF-8")).andReturn();
        
        JsonArray jsonObj = new Gson().fromJson(mvcResult.getResponse().getContentAsString(), JsonArray.class);
        Assert.assertEquals(pDiscount.getDiscountPrice().longValue(), jsonObj.get(0).getAsJsonObject().get("discountPrice").getAsLong());
        Assert.assertEquals(pftwDiscount.getDiscountPrice().longValue(), jsonObj.get(1).getAsJsonObject().get("discountPrice").getAsLong());
        
        verify(discountService, times(1)).getProductDiscount(0L, Arrays.asList(0L));
	}
	
	@Test
	public void getDiscount_For_Invalid_Input_data() throws Exception {
		
        List<Long> products = Stream.of(2L).collect(Collectors.toList());
        
		when(this.discountService.getProductDiscount(1L, products))
				.thenThrow(new DiscountRuleException(
						"Exception executing consequence for rule \"5% Special discount on Sparkasse VISA Credit card shopping in the month of august\" in com.rule: java.lang.NullPointerException has been failed",
						"1,2", "Invalid attribute data"));
        
        mockMvc.perform(get("/discount/getDiscount/?customerId=1&productIds=2").contentType(MediaType.APPLICATION_PROBLEM_JSON))
	       .andExpect(status().is(500));
        
        
        verify(discountService, times(1)).getProductDiscount(1L, Arrays.asList(2L));
	}
	
	
	public static class DiscountMgtApiConfiguration {

        @Bean
        @Primary
        public DiscountService discountService() {
            return mock(DiscountService.class);
        }

        
    }
}
