package com.discount.mgt.dao.db;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.discount.mgt.domain.Product;

/**
 * @author Nava Krishna
 * 
 * Inherited from JpaRepository interface .
 * Implements custom methods {@link #findByProductIdIn(List)} along with inherited methods.
 *
 */

@Repository
public interface ProductDao extends JpaRepository<Product, Long> {
	/**
	 * Retrieves Product list instance from productId field of Product.
	 * 
	 * @param productIds
	 * @return 
	 */
	Optional<List<Product>> findByProductIdIn(List<Long> productIds);
}
