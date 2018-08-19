package com.discount.mgt.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.discount.mgt.dao.db.CustomerDao;
import com.discount.mgt.dao.db.ProductDao;
import com.discount.mgt.dao.impl.RulesDao;
import com.discount.mgt.domain.Customer;
import com.discount.mgt.domain.Product;
import com.discount.mgt.dto.ProductDiscount;
import com.discount.mgt.exception.DiscountRuleException;
import com.discount.mgt.service.impl.DiscountService;

@RunWith(SpringJUnit4ClassRunner.class)
public class DiscountServiceTest {

	private DiscountService discountService;

	@MockBean
	private CustomerDao custDao;

	@MockBean
	private ProductDao prodDao;

	@MockBean
	private RulesDao rulesDao;

	@Before
	public void setUp() {
		this.discountService = new DiscountService(prodDao, custDao, rulesDao);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetProductDiscountWithCustomerAndProductId() {
		Customer customer = Customer.builder().customerId(1L).customerType("Employee").build();

		Product productApp = Product.builder().productId(1L).discountMonth(8).productDivision("APPAREL")
				.isPromotionalProduct(true).salesDate(LocalDate.now().minusDays(10)).productName("Shirt")
				.salesPrice(new BigDecimal(30.00)).build();

		Product productFtw = Product.builder().productId(1L).discountMonth(8).productDivision("Footwear")
				.isPromotionalProduct(false).salesDate(LocalDate.now().minusDays(10)).productName("Shoe")
				.salesPrice(new BigDecimal(25.00)).build();

		when(custDao.findById(Mockito.any(Long.class))).thenReturn(Optional.of(customer));
		when(prodDao.findByProductIdIn(any()))
				.thenReturn(Optional.of(Stream.of(productApp, productFtw).collect(Collectors.toList())));

		when(rulesDao.getProductDiscount(any())).thenReturn(any());

		List<ProductDiscount> productDiscounts = discountService.getProductDiscount(1L,
				Stream.of(1L, 2L).collect(Collectors.toList()));

		assertThat(productDiscounts, contains(hasProperty("discountPrice", is(new BigDecimal(30.00))),
				hasProperty("discountPrice", is(new BigDecimal(25.00)))));

		verify(custDao, times(1)).findById(Mockito.any(Long.class));
		verify(prodDao, times(1)).findByProductIdIn(Mockito.any());
		verify(rulesDao, times(1)).getProductDiscount(Mockito.any());

		verifyNoMoreInteractions(custDao);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetProductDiscountIfCustomerProductDetailsHaveDefaultValues() {
		Customer customer = Customer.builder().build();

		Product productApp = Product.builder().productId(1L).discountMonth(8).productDivision("APPAREL")
				.isPromotionalProduct(true).salesDate(LocalDate.now().minusDays(10)).productName("Shirt")
				.salesPrice(new BigDecimal(30.00)).build();

		Product productFtw = Product.builder().productId(2L).discountMonth(8).productDivision("Footwear")
				.isPromotionalProduct(false).salesDate(LocalDate.now().minusDays(10)).productName("Shoe")
				.salesPrice(new BigDecimal(25.00)).build();

		Product productAccess = Product.builder().productId(3L).discountMonth(8).productDivision("Accessories")
				.isPromotionalProduct(false).salesDate(LocalDate.now().minusDays(10)).productName("Watch")
				.salesPrice(new BigDecimal(40.00)).build();

		when(custDao.findById(0L)).thenReturn(Optional.of(customer));
		when(prodDao.findByProductIdIn(Arrays.asList(0L)))
				.thenReturn(Optional.of(Stream.of(productApp, productFtw, productAccess).collect(Collectors.toList())));

		when(rulesDao.getProductDiscount(any())).thenReturn(any());

		List<ProductDiscount> productDiscounts = discountService.getProductDiscount(0L,
				Stream.of(0L).collect(Collectors.toList()));

		assertThat(productDiscounts,
				contains(hasProperty("discountPrice", is(new BigDecimal(30.00))),
						hasProperty("discountPrice", is(new BigDecimal(25.00))),
						hasProperty("discountPrice", is(new BigDecimal(40.00)))));

		verify(custDao, times(1)).findById(Mockito.any(Long.class));
		verify(prodDao, times(1)).findByProductIdIn(Mockito.any());
		verify(rulesDao, times(1)).getProductDiscount(Mockito.any());

		verifyNoMoreInteractions(custDao);

	}

	@Test(expected=DiscountRuleException.class)
	public void testGetProductDiscountIfExceptionThrownFromRules() {
		Customer customer = Customer.builder().build();

		Product productApp = Product.builder().productId(1L).discountMonth(8).productDivision("APPAREL")
				.isPromotionalProduct(true).salesDate(LocalDate.now().minusDays(10)).productName("Shirt")
				.salesPrice(new BigDecimal(30.00)).build();

		Product productFtw = Product.builder().productId(2L).discountMonth(8).productDivision("Footwear")
				.isPromotionalProduct(false).salesDate(LocalDate.now().minusDays(10)).productName("Shoe")
				.salesPrice(new BigDecimal(25.00)).build();

		Product productAccess = Product.builder().productId(3L).discountMonth(8).productDivision("Accessories")
				.isPromotionalProduct(false).salesDate(LocalDate.now().minusDays(10)).productName("Watch")
				.salesPrice(new BigDecimal(40.00)).build();

		when(custDao.findById(0L)).thenReturn(Optional.of(customer));
		when(prodDao.findByProductIdIn(Arrays.asList(0L)))
				.thenReturn(Optional.of(Stream.of(productApp, productFtw, productAccess).collect(Collectors.toList())));

		when(rulesDao.getProductDiscount(any())).thenThrow(new DiscountRuleException(
				"Exception executing consequence for rule \"5% Special discount on Sparkasse VISA Credit card shopping in the month of august\" in com.rule: java.lang.NullPointerException has been failed",
				"1,2", "Invalid attribute data"));

		 discountService.getProductDiscount(0L,
				Stream.of(0L).collect(Collectors.toList()));
		

	}

}
