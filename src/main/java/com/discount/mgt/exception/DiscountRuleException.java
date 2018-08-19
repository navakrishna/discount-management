package com.discount.mgt.exception;

/**
 * {@link DiscountRuleException} has to be thrown by dao layer whenever rule
 * execution failed
 * 
 * @author Nava Krishna
 *
 */
public class DiscountRuleException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5125065653826131984L;
	private String resourceType;
	private String resourceId;

	public DiscountRuleException(String message, String resourceId, String resourceType) {
		super(message);
		this.resourceId = resourceId;
		this.resourceType = resourceType;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
}
