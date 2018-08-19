package com.discount.mgt.domain;

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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Customer")
public class Customer implements DBColumns {

	@Id
    @NotNull
    @Column(name = CUSTOMER_ID_COLUMN)
	private Long customerId;
	@Column(name = CUSTOMERTYPE_COLUMN)
	private String customerType;
	
}
