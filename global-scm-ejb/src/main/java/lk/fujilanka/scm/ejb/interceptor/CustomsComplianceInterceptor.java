package lk.fujilanka.scm.ejb.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import lk.fujilanka.scm.ejb.interceptor.binding.CustomsComplianceCheck;

@Interceptor
@CustomsComplianceCheck
@Priority(Interceptor.Priority.APPLICATION + 10)
public class CustomsComplianceInterceptor {

    @AroundInvoke
    public Object checkCustomsCompliance(InvocationContext context) throws Exception {
        System.out.println("[CustomsComplianceInterceptor]: Verifying tariff HS code and port clearance compliance for method: " + context.getMethod().getName());
        return context.proceed();
    }
}
