package lk.fujilanka.scm.ejb;

import jakarta.interceptor.InvocationContext;
import lk.fujilanka.scm.core.entity.Vendor;
import lk.fujilanka.scm.core.exception.ScmBusinessException;
import lk.fujilanka.scm.ejb.interceptor.CustomsComplianceInterceptor;
import lk.fujilanka.scm.ejb.interceptor.VendorValidationInterceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterceptorTest {

    @Mock
    private InvocationContext invocationContext;

    @InjectMocks
    private CustomsComplianceInterceptor customsInterceptor;

    @InjectMocks
    private VendorValidationInterceptor vendorInterceptor;

    @Test
    @DisplayName("CustomsComplianceInterceptor should pass compliant declarations")
    void testCustomsCompliancePass() throws Exception {
        Method mockMethod = this.getClass().getDeclaredMethods()[0];
        when(invocationContext.getMethod()).thenReturn(mockMethod);
        when(invocationContext.getParameters()).thenReturn(new Object[]{"Standard tariff declaration cleared under HS Code 8542.31"});
        when(invocationContext.proceed()).thenReturn("PROCEEDED");

        Object result = customsInterceptor.checkCustomsCompliance(invocationContext);

        assertEquals("PROCEEDED", result);
        verify(invocationContext, times(1)).proceed();
    }

    @Test
    @DisplayName("CustomsComplianceInterceptor should reject prohibited hazardous cargo")
    void testCustomsComplianceProhibitedCargo() {
        Method mockMethod = this.getClass().getDeclaredMethods()[0];
        when(invocationContext.getMethod()).thenReturn(mockMethod);
        when(invocationContext.getParameters()).thenReturn(new Object[]{"Contains illegal prohibited contraband narcotics"});

        assertThrows(ScmBusinessException.class, () -> {
            customsInterceptor.checkCustomsCompliance(invocationContext);
        });
    }

    @Test
    @DisplayName("VendorValidationInterceptor should pass valid vendor registration")
    void testVendorValidationPass() throws Exception {
        Method mockMethod = this.getClass().getDeclaredMethods()[0];
        when(invocationContext.getMethod()).thenReturn(mockMethod);
        Vendor vendor = new Vendor("Apex Logistics", "info@apex.sg", "+65 6789 0123", "Singapore", "TAX-SG-4410");

        when(invocationContext.getParameters()).thenReturn(new Object[]{vendor});
        when(invocationContext.proceed()).thenReturn("VALIDATED");

        Object result = vendorInterceptor.validateVendorData(invocationContext);

        assertEquals("VALIDATED", result);
        verify(invocationContext, times(1)).proceed();
    }

    @Test
    @DisplayName("VendorValidationInterceptor should reject vendor without valid Tax ID")
    void testVendorValidationMissingTaxId() {
        Method mockMethod = this.getClass().getDeclaredMethods()[0];
        when(invocationContext.getMethod()).thenReturn(mockMethod);
        Vendor vendor = new Vendor("Unknown Trader", "trader@unknown.com", "+94 77 000 0000", "Sri Lanka", "");

        when(invocationContext.getParameters()).thenReturn(new Object[]{vendor});

        assertThrows(ScmBusinessException.class, () -> {
            vendorInterceptor.validateVendorData(invocationContext);
        });
    }
}
