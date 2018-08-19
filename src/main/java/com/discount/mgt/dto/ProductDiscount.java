package com.discount.mgt.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.kie.api.definition.type.PropertyReactive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nava Krishna
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PropertyReactive
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ProductDiscount {
	
	private Long productId;
	private Long customerId;
	private BigDecimal salesPrice;
	private BigDecimal discountPrice;
	private String appliedDiscounts;
	@JsonIgnore
	private String customerType;
	@JsonIgnore
	private String productDivision;
	@JsonIgnore
	private LocalDate salesDate;
	@JsonIgnore
	private String memebrShipType;
	@JsonIgnore
	private Boolean isPromotionalProduct;
	@JsonIgnore
	private BigDecimal orderPrice;
	@JsonIgnore
	private LocalDate orderCreatedDate;
	@JsonIgnore
	private int discountMonth;
	
	
}
