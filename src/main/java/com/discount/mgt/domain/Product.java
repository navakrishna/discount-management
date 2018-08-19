package com.discount.mgt.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Nava Krishna
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="Product")
public class Product implements DBColumns{

	@Id
    @NotNull
    @Column(name = PRODUCT_ID_COLUMN)
	private Long productId;
	@Column(name = PRODUCTNAME_COLUMN)
	private String productName;
	@Column(name = PRODUCTDIVISION_COLUMN)
	private String productDivision;
	@Column(name = PRICE_COLUMN)
	private BigDecimal salesPrice = new BigDecimal("0.00");
	@Column(name = PRODUCT_CREATE_DATE)
	private LocalDate salesDate;
	@Column(name = PROMOTION_STATUS_COULUM)
	private Boolean isPromotionalProduct;
	@Column(name=DISCOUNT_MONTH_COLUMN)
	private Integer discountMonth;
}
