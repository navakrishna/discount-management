package com.discount.mgt.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.discount.mgt.dao.impl.RulesDao;
import com.discount.mgt.dto.ProductDiscount;
import com.discount.mgt.exception.DiscountRuleException;

@RunWith(SpringJUnit4ClassRunner.class)
public class RulesDaoTest {

	private RulesDao rulesDao;

	@MockBean
	private KieSession kieSession;

	@Before
	public void setUp() {
		this.rulesDao = new RulesDao(kieSession);
	}

	@Test
	public void testFireRulesIfProductDiscountListIsEmpty() {
		when(kieSession.fireAllRules()).thenReturn(1);
		assertThat(rulesDao.getProductDiscount(new ArrayList<ProductDiscount>()).isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testFireRulesIfProductDiscountListIsNotEmpty() {

		ProductDiscount discountOnAppProduct = ProductDiscount.builder()
				.appliedDiscounts("Employee discount, Special Priveliges").customerId(1L).customerType("Employee")
				.discountMonth(8).isPromotionalProduct(true).productDivision("APPAREL")
				.salesPrice(new BigDecimal(30.00)).discountPrice(new BigDecimal(30.00)).build();

		ProductDiscount discountOnFtwProduct = ProductDiscount.builder()
				.appliedDiscounts("Employee discount, Special Priveliges").customerId(2L).customerType("Employee")
				.discountMonth(8).isPromotionalProduct(true).productDivision("FOOTWEAR")
				.salesPrice(new BigDecimal(25.00)).discountPrice(new BigDecimal(25.00)).build();

		when(kieSession.fireAllRules()).thenReturn(1);

		List<ProductDiscount> listOfdiscountsOnProducts = rulesDao
				.getProductDiscount(Stream.of(discountOnAppProduct, discountOnFtwProduct).collect(Collectors.toList()));

		assertThat(listOfdiscountsOnProducts, contains(hasProperty("discountPrice", is(new BigDecimal(30.00))),
				hasProperty("discountPrice", is(new BigDecimal(25.00)))));

	}

	@Test(expected = DiscountRuleException.class)
	public void testFireRulesIfExceptionIsThrown() {

		ProductDiscount discountOnAppProduct = ProductDiscount.builder()
				.appliedDiscounts("Employee discount, Special Priveliges").customerId(1L).customerType("Employee")
				.discountMonth(8).isPromotionalProduct(true).productDivision("APPAREL")
				.salesPrice(new BigDecimal(30.00)).discountPrice(new BigDecimal(30.00)).build();

		ProductDiscount discountOnFtwProduct = ProductDiscount.builder()
				.appliedDiscounts("Employee discount, Special Priveliges").customerId(2L).customerType("Employee")
				.discountMonth(8).isPromotionalProduct(true).productDivision("FOOTWEAR")
				.salesPrice(new BigDecimal(25.00)).discountPrice(new BigDecimal(25.00)).build();

		when(kieSession.fireAllRules()).thenThrow(new DiscountRuleException(
				"Exception executing consequence for rule \"5% Special discount on Sparkasse VISA Credit card shopping in the month of august\" in com.rule: java.lang.NullPointerException has been failed",
				"1,2", "Invalid attribute data"));

		rulesDao.getProductDiscount(Stream.of(discountOnAppProduct, discountOnFtwProduct).collect(Collectors.toList()));

		verify(kieSession, times(1)).fireAllRules();

	}

}
