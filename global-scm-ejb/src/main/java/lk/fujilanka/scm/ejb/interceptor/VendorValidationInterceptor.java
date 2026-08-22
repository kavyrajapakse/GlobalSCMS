package lk.fujilanka.scm.ejb.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import lk.fujilanka.scm.core.entity.Vendor;
import lk.fujilanka.scm.core.exception.ScmBusinessException;
import lk.fujilanka.scm.ejb.interceptor.binding.VendorDataValidation;

import java.util.logging.Logger;

@Interceptor
@VendorDataValidation
@Priority(Interceptor.Priority.APPLICATION + 30)
public class VendorValidationInterceptor {

    private static final Logger LOGGER = Logger.getLogger(VendorValidationInterceptor.class.getName());

    @AroundInvoke
    public Object validateVendorData(InvocationContext context) throws Exception {
        LOGGER.info("[VendorValidationInterceptor]: Validating supplier credentials and cargo safety parameters for: " + context.getMethod().getName());

        Object[] params = context.getParameters();
        if (params != null) {
            for (Object param : params) {
                if (param instanceof Vendor) {
                    Vendor vendor = (Vendor) param;
                    if (vendor.getCompanyName() == null || vendor.getCompanyName().isBlank()) {
                        throw new ScmBusinessException("Vendor Validation Interceptor Error: Supplier company name cannot be blank.");
                    }
                    if (vendor.getTaxId() == null || vendor.getTaxId().isBlank()) {
                        throw new ScmBusinessException("Vendor Validation Interceptor Error: International Tax ID is required for supplier verification.");
                    }
                }
            }
        }

        return context.proceed();
    }
}
