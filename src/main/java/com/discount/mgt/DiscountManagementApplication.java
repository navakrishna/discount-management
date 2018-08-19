package com.discount.mgt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 
 * DiscountManagementApplication retrieve different types of discounts
 * applicable on a product.
 * 
 * <p>
 * Main starting point of the application. Spring Data JPA is enabled through
 * annotations on {@link DiscountManagementApplication}} class.
 * <p>
 * 
 * @author Nava Krishna
 * @see EnableJpaRepositories
 *      <p>
 * 
 * @since 1.0
 *
 */
@SpringBootApplication
@EnableJpaRepositories
//@EnableResourceServer
public class DiscountManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscountManagementApplication.class, args);
	}


    


}
