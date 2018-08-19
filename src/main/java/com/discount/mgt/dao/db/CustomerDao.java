package com.discount.mgt.dao.db;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.discount.mgt.domain.Customer;

/**
 * @author Nava Krishna
 * 
 * Inherited from JpaRepository interface .
 *
 */
@Repository
public interface CustomerDao extends JpaRepository<Customer, Long> {
    
}
