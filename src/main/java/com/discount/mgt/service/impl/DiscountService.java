package com.discount.mgt.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.discount.mgt.dao.IRulesDao;
import com.discount.mgt.dao.db.CustomerDao;
import com.discount.mgt.dao.db.ProductDao;
import com.discount.mgt.domain.Customer;
import com.discount.mgt.dto.ProductDiscount;
import com.discount.mgt.service.IDiscountService;

/**
 * 
 * {@link DiscountService} implementation for {@link IDiscountService}.
 * Used for discount calculations.
 * 
 * @author Nava Krishna
 *
 */
@Service
public class DiscountService implements IDiscountService {

	private ProductDao productDao;
	private CustomerDao customerDao;
	private IRulesDao rulesDao;

	@Autowired
	public DiscountService(ProductDao productDao, CustomerDao customerDao,
			IRulesDao rulesDao) {
		this.productDao = productDao;
		this.customerDao = customerDao;
		this.rulesDao = rulesDao;
	}

	/* (non-Javadoc)
	 * @see com.discount.mgt.service.IDiscountService#getProductDiscount(java.lang.Long, java.util.List)
	 */
	@Override
	public List<ProductDiscount> getProductDiscount(Long customerId, List<Long> productIds) {
		Customer customer = this.customerDao.findById(customerId).orElse(new Customer());

		List<ProductDiscount> productDiscounts = this.productDao.findByProductIdIn(productIds)
				.orElse(this.productDao.findAll()).stream().map(product -> {
					ProductDiscount pDiscount = new ProductDiscount();
					BeanUtils.copyProperties(customer, pDiscount);
					BeanUtils.copyProperties(product, pDiscount);
					pDiscount.setDiscountPrice(pDiscount.getSalesPrice());
					pDiscount.setAppliedDiscounts("");
					
					return pDiscount;
				}).collect(Collectors.toList());
		;

		rulesDao.getProductDiscount(productDiscounts);

		return productDiscounts;
	}

}
