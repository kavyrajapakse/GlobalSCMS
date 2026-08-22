package lk.fujilanka.scm.ejb.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import lk.fujilanka.scm.core.exception.ScmBusinessException;
import lk.fujilanka.scm.ejb.interceptor.binding.CustomsComplianceCheck;

import java.util.logging.Logger;

@Interceptor
@CustomsComplianceCheck
@Priority(Interceptor.Priority.APPLICATION + 10)
public class CustomsComplianceInterceptor {

    private static final Logger LOGGER = Logger.getLogger(CustomsComplianceInterceptor.class.getName());

    @AroundInvoke
    public Object checkCustomsCompliance(InvocationContext context) throws Exception {
        LOGGER.info("[CustomsComplianceInterceptor]: Verifying tariff HS code and port clearance compliance for method: " + context.getMethod().getName());

        Object[] params = context.getParameters();
        if (params != null) {
            for (Object param : params) {
                if (param instanceof String) {
                    String str = (String) param;
                    if (str.toLowerCase().contains("prohibited") || str.toLowerCase().contains("illegal")) {
                        throw new ScmBusinessException("Customs Compliance Interceptor Violation: Declaration contains prohibited trade cargo specification.");
                    }
                }
            }
        }

        return context.proceed();
    }
}
