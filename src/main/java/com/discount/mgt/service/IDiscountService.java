package com.discount.mgt.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.discount.mgt.dto.ProductDiscount;

/**
 * Discount service interface {@link IDiscountService} consumed by
 * {@link DiscountService}} which exposes
 * {@code #getProductDiscount(Long, List)} method. This method is consumed by
 * Discount management api{@link DiscountMgtApi}}.
 * 
 * 
 * @author Nava Krishna
 *
 */
@Service
public interface IDiscountService {

	/**
	 * It accepts @param customerid and @param list of productids Fetch consumer and
	 * product details based on consumer and product id's. Perform discount
	 * calculations based on product and consumer data.
	 * 
	 * 
	 * @param customerId
	 * @param products
	 * @return
	 */
	List<ProductDiscount> getProductDiscount(Long customerId, List<Long> products);
}
