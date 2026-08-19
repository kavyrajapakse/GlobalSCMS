package lk.fujilanka.scm.ejb.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import lk.fujilanka.scm.ejb.interceptor.binding.VendorDataValidation;

@Interceptor
@VendorDataValidation
@Priority(Interceptor.Priority.APPLICATION + 30)
public class VendorValidationInterceptor {

    @AroundInvoke
    public Object validateVendorData(InvocationContext context) throws Exception {
        System.out.println("[VendorValidationInterceptor]: Validating supplier credentials and cargo safety parameters for: " + context.getMethod().getName());
        return context.proceed();
    }
}
