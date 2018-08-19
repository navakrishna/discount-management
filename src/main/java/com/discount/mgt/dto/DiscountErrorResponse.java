package com.discount.mgt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(description = "Problem Detail response")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscountErrorResponse {

	@ApiModelProperty(value = "Problem Detail Title", required = true, example = "Resource not found")
    private String title;
    @ApiModelProperty(value = "Field is identifier and as such it MAY be used to denote additional error codes", example = "ERR_00001")
    private String type;
    @ApiModelProperty(value = "Further detail of the Problem or a user understandable message", example = "Missing authentication credentials")
    private String detail;
    @ApiModelProperty(value = "The URI of the resource in question", example = "/user/abc")
    private String instance;
    @ApiModelProperty(value = "HTTP Status Code", example = "404")
    private Integer status;
	public DiscountErrorResponse() {
		
	}
	
	public DiscountErrorResponse(Integer status, String type, String title, String detail, String instance) {
		super();
        this.status = status;
        this.type = type;
        this.title = title;
        this.instance = instance;
        this.detail = detail;
	}

}
