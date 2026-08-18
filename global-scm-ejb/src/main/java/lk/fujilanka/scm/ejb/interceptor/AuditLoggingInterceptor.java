package lk.fujilanka.scm.ejb.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

public class AuditLoggingInterceptor {

    @AroundInvoke
    public Object logMethodInvocation(InvocationContext context) throws Exception {
        String methodName = context.getMethod().getName();
        String className = context.getTarget().getClass().getSimpleName();
        long startTime = System.currentTimeMillis();

        System.out.println("[EJB INTERCEPTOR] Invoking: " + className + "." + methodName + "()");

        try {
            Object result = context.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("[EJB INTERCEPTOR] Success: " + className + "." + methodName + "() completed in " + elapsedTime + " ms.");
            return result;
        } catch (Exception e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            System.err.println("[EJB INTERCEPTOR EXCEPTION] Failed: " + className + "." + methodName + "() after " + elapsedTime + " ms. Exception: " + e.getMessage());
            throw e;
        }
    }
}