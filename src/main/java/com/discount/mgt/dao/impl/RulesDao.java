package com.discount.mgt.dao.impl;

import java.util.List;

import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.discount.mgt.dao.IRulesDao;
import com.discount.mgt.dto.ProductDiscount;
import com.discount.mgt.exception.DiscountRuleException;

/**
 * {@link RulesDao} implementation for {@link IRulesDao} used by service to
 * get pojo objects with actions performed by business rules.
 * 
 * @author Nava Krishna
 * @see IRulesDao
 * @since 1.0
 */
@Component
public class RulesDao implements IRulesDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(RulesDao.class);

	@Value("${invalid.data}")
	private String errorMessage;

	private KieSession kieSession;

	@Autowired
	public RulesDao(KieSession kieSession) {
		this.kieSession = kieSession;
	}

	/* (non-Javadoc)
	 * @see com.discount.mgt.dao.IRulesDao#getProductDiscount(java.util.List)
	 */
	@Override
	public List<ProductDiscount> getProductDiscount(List<ProductDiscount> productDiscounts) {
		fireRules(productDiscounts);
		return productDiscounts;
	}

	/**
	 * Adds facts to kiesession and fire the rules.
	 * 
	 * @param productDiscounts
	 */
	private void fireRules(List<ProductDiscount> productDiscounts) {
		StringBuilder builder = new StringBuilder();
		try {
			productDiscounts.stream().forEach(pDiscount -> {
				builder.append(pDiscount.getProductId()).append(" ");
				kieSession.insert(pDiscount);
			});
			kieSession.setGlobal("droolsLogger", LOGGER);
			kieSession.fireAllRules();

		} catch (Exception ex) {
			throw new DiscountRuleException(ex.getMessage(), builder.toString(), errorMessage);
		}

	}

}
