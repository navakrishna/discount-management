package com.discount.mgt.dao;

import java.util.List;

import com.discount.mgt.dto.ProductDiscount;
import com.discount.mgt.service.impl.DiscountService;

/**
 * Rules Dao interface {@link IRulesDao} will fire business rules on list of
 * facts(Pojo objects). It {@link IRulesDao} exposes
 * {@link #getProductDiscount(List)} which is consumed by Discout service
 * ({@link DiscountService}) which
 * 
 * 
 * @author Nava Krishna
 *
 */
public interface IRulesDao {

	/**
	 * It accepts list of product discount pojos {@link ProductDiscount}} and adds
	 * them to kiesession. Once pojos's have been added, it fires business rules.
	 * 
	 * @param productDiscounts
	 * @return {@link List<ProductDiscount>}}
	 */
	List<ProductDiscount> getProductDiscount(List<ProductDiscount> productDiscounts);
}
