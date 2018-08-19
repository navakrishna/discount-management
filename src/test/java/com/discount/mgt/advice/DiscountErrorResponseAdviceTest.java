package com.discount.mgt.advice;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.discount.mgt.dto.DiscountErrorResponse;
import com.discount.mgt.exception.DiscountRuleException;

public class DiscountErrorResponseAdviceTest {


	private DiscountErrorResponseAdvice advice;

	private HttpServletRequest httpServletRequest;
	
	@Before
	public void setUp() throws Exception {
		this.advice = new DiscountErrorResponseAdvice();
		this.httpServletRequest = Mockito.mock(HttpServletRequest.class);
	}

	@Test
	public void testHandleDiscruleException() {
		DiscountRuleException exception = new DiscountRuleException(
				"Exception executing consequence for rule \"5% Special discount on Sparkasse VISA Credit card shopping in the month of august\" in com.rule: java.lang.NullPointerException has been failed",
				"1,2", "Invalid attribute data");

		when(this.httpServletRequest.getRequestURI()).thenReturn("/getDiscount");

		DiscountErrorResponse errorResponse = this.advice.handleDiscruleException(exception, httpServletRequest)
				.getBody();
		
		Assert.assertEquals(new Integer(500), errorResponse.getStatus());
		Assert.assertEquals("/getDiscount", errorResponse.getInstance());
		Assert.assertEquals("Business Rule Failure", errorResponse.getTitle());
		Assert.assertEquals("Eexecution failed for rule 5% Special discount on Sparkasse VISA Credit card shopping in the month of august for either of productIds '1,2' because of Invalid attribute data", errorResponse.getDetail());
		
		verify(this.httpServletRequest, times(1)).getRequestURI();
        verifyNoMoreInteractions(this.httpServletRequest);

	}
	
	@Test
    public void handleMethodArgumentTypeMismatchException() throws ClassNotFoundException {
        when(this.httpServletRequest.getRequestURI()).thenReturn("/getDiscount?customerId=null&productIds=null");
        MethodArgumentTypeMismatchException methodArgumentTypeMismatchException = mock(MethodArgumentTypeMismatchException.class);
        when(methodArgumentTypeMismatchException.getName()).thenReturn("customerId");
        when(methodArgumentTypeMismatchException.getValue()).thenReturn("1");
        MethodParameter methodParameter = mock(MethodParameter.class);
        when(methodParameter.getParameterType()).thenAnswer((Answer<Object>) invocationOnMock -> String.class);
        when(methodArgumentTypeMismatchException.getParameter()).thenReturn(methodParameter);


        DiscountErrorResponse errorResponse = this.advice.handleMethodArgumentTypeMismatch(methodArgumentTypeMismatchException, this.httpServletRequest).getBody();
        Assert.assertEquals(new Integer(422), errorResponse.getStatus());
        Assert.assertEquals("/getDiscount?customerId=null&productIds=null", errorResponse.getInstance());
        Assert.assertEquals("Parameter type mismatch", errorResponse.getTitle());
        Assert.assertNull(errorResponse.getType());
        Assert.assertEquals("Incorrect value '1' for field 'customerId'. Expected value of type 'java.lang.String'", errorResponse.getDetail());

        verify(this.httpServletRequest, times(1)).getRequestURI();
        verifyNoMoreInteractions(this.httpServletRequest);
    }
	
	@Test
    public void testHandleException() throws Exception {

        when(this.httpServletRequest.getRequestURI()).thenReturn("/getDiscount?customerId=1&productIds=1");

        DiscountErrorResponse errorResponse = this.advice.handleAll(new RuntimeException(), this.httpServletRequest).getBody();
        Assert.assertEquals(new Integer(500), errorResponse.getStatus());
        Assert.assertEquals("/getDiscount?customerId=1&productIds=1", errorResponse.getInstance());
        Assert.assertEquals("Internal Error", errorResponse.getTitle());
        Assert.assertEquals("An unexpected error has occurred", errorResponse.getDetail());
        Assert.assertNull(errorResponse.getType());

        verify(this.httpServletRequest, times(1)).getRequestURI();
        verifyNoMoreInteractions(this.httpServletRequest);
    }
}
