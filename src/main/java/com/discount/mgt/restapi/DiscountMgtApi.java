package com.discount.mgt.restapi;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.discount.mgt.dto.DiscountErrorResponse;
import com.discount.mgt.dto.ProductDiscount;
import com.discount.mgt.service.IDiscountService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * DiscountMgtApi exposes api for providing discount on products.
 * 
 * 
 * @author Nava Krishna
 *
 */
@RestController
@RequestMapping("/discount")
public class DiscountMgtApi {

	private  IDiscountService discountService;

	@Autowired
	public DiscountMgtApi(IDiscountService discountService) {
		this.discountService = discountService;
	}
	
	@ApiOperation(
            value = "Retrieve product discounts",
            notes = "Make a GET request to retrieve product discounts",
            response = List.class,
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved", response = ProductDiscount.class),
            @ApiResponse(code = 422, message = "Wrong inputs",
                    response = DiscountErrorResponse.class),
            @ApiResponse(code = 500, message = "Unexpected Internal Error", response = DiscountErrorResponse.class)})
	@RequestMapping(value = "/getDiscount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public HttpEntity<List<ProductDiscount>> getDiscount(
			@RequestParam(value="customerId", defaultValue="0")  Long customerId, @RequestParam(value="productIds", defaultValue="0") List<Long> products ) {
		
		return ResponseEntity.ok(discountService.getProductDiscount(customerId, products));
	}
}
