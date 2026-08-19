package lk.fujilanka.scm.ejb.interceptor;

import jakarta.annotation.Priority;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import lk.fujilanka.scm.ejb.interceptor.binding.ExecutionPerformanceAudit;

@Interceptor
@ExecutionPerformanceAudit
@Priority(Interceptor.Priority.APPLICATION + 20)
public class PerformanceAuditInterceptor {

    @AroundInvoke
    public Object auditPerformance(InvocationContext context) throws Exception {
        long startTime = System.currentTimeMillis();
        try {
            return context.proceed();
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            System.out.println("[PerformanceAuditInterceptor]: Method " + context.getMethod().getName() + " executed in " + duration + " ms.");
        }
    }
}
