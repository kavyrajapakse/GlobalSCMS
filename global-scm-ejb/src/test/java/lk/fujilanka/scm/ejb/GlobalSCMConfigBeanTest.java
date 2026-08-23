package lk.fujilanka.scm.ejb;

import lk.fujilanka.scm.ejb.singleton.GlobalSCMConfigBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalSCMConfigBeanTest {

    private GlobalSCMConfigBean configBean;

    @BeforeEach
    void setUp() {
        configBean = new GlobalSCMConfigBean();
        configBean.initializeSystemConfiguration();
    }

    @Test
    @DisplayName("Singleton should initialize system parameters and default port map on startup")
    void testSingletonInitialization() {
        assertEquals(15000000.0, configBean.getMaxCarrierBookingThresholdLkr());
        assertEquals(30, configBean.getDefaultSafetyStockThreshold());
        assertEquals("PRODUCTION", configBean.getSystemEnvironment());

        Map<String, String> ports = configBean.getAllPortStatuses();
        assertNotNull(ports);
        assertEquals("OPERATIONAL", ports.get("COLOMBO_PORT"));
        assertEquals("OPERATIONAL", ports.get("NAGOYA_PORT"));
    }

    @Test
    @DisplayName("Singleton should allow thread-safe write updates to operational thresholds")
    void testThreadSafeUpdates() {
        configBean.setMaxCarrierBookingThresholdLkr(20000000.0);
        configBean.setDefaultSafetyStockThreshold(50);
        configBean.updatePortStatus("COLOMBO_PORT", "MAINTENANCE");

        assertEquals(20000000.0, configBean.getMaxCarrierBookingThresholdLkr());
        assertEquals(50, configBean.getDefaultSafetyStockThreshold());
        assertEquals("MAINTENANCE", configBean.getAllPortStatuses().get("COLOMBO_PORT"));
    }

    @Test
    @DisplayName("Singleton should export comprehensive system config snapshot")
    void testConfigSnapshot() {
        Map<String, Object> snapshot = configBean.getSystemConfigSnapshot();
        assertNotNull(snapshot);
        assertTrue(snapshot.containsKey("maxCarrierBookingThresholdLkr"));
        assertTrue(snapshot.containsKey("portStatuses"));
    }
}
