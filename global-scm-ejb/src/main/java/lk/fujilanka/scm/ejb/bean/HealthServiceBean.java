package lk.fujilanka.scm.ejb.bean;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.fujilanka.scm.ejb.local.HealthServiceLocal;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

@Stateless
public class HealthServiceBean implements HealthServiceLocal {

    @PersistenceContext(unitName = "SCMPU")
    private EntityManager em;

    @Override
    public Map<String, Object> getHealthStatus() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("application", "GlobalTrade SCM Enterprise System");
        health.put("server", "Payara Application Server 6");
        health.put("datasource", "jdbc/SCMDS");

        try {
            Object result = em.createNativeQuery("SELECT 1").getSingleResult();
            if (result != null) {
                health.put("databaseStatus", "CONNECTED (Empirically Verified)");
            } else {
                health.put("databaseStatus", "WARNING - Null Query Result");
            }
        } catch (Exception e) {
            health.put("databaseStatus", "DISCONNECTED - " + e.getMessage());
            health.put("status", "DEGRADED");
        }

        health.put("uptimeMs", ManagementFactory.getRuntimeMXBean().getUptime());
        health.put("timestamp", System.currentTimeMillis());

        return health;
    }
}
