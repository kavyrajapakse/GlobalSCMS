package lk.fujilanka.scm.ejb.singleton;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import lk.fujilanka.scm.ejb.local.GlobalSCMConfigLocal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Enterprise Singleton Session Bean.
 * Automatically initialized at application boot to maintain system-wide operational parameters
 * and maritime port clearance statuses with thread-safe container-managed concurrency.
 */
@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class GlobalSCMConfigBean implements GlobalSCMConfigLocal {

    private static final Logger LOGGER = Logger.getLogger(GlobalSCMConfigBean.class.getName());

    // System-wide operational parameters
    private double maxCarrierBookingThresholdLkr = 15000000.0;
    private int defaultSafetyStockThreshold = 30;
    private String systemEnvironment = "PRODUCTION";

    // In-memory status map of global logistics ports
    private final Map<String, String> portOperatingStatus = new ConcurrentHashMap<>();

    @PostConstruct
    public void initializeSystemConfiguration() {
        LOGGER.info("[GlobalSCMConfigBean]: Initializing enterprise logistics configuration on server startup...");

        portOperatingStatus.put("COLOMBO_PORT", "OPERATIONAL");
        portOperatingStatus.put("HAMBANTOTA_PORT", "OPERATIONAL");
        portOperatingStatus.put("NAGOYA_PORT", "OPERATIONAL");
        portOperatingStatus.put("SINGAPORE_PORT", "OPERATIONAL");

        LOGGER.info("[GlobalSCMConfigBean]: Global logistics parameters loaded successfully. Maximum carrier booking limit: LKR " + maxCarrierBookingThresholdLkr);
    }

    @PreDestroy
    public void cleanupOnShutdown() {
        LOGGER.info("[GlobalSCMConfigBean]: Gracefully shutting down global logistics configuration.");
    }

    @Override
    @Lock(LockType.READ)
    public double getMaxCarrierBookingThresholdLkr() {
        return maxCarrierBookingThresholdLkr;
    }

    @Override
    @Lock(LockType.WRITE)
    public void setMaxCarrierBookingThresholdLkr(double threshold) {
        LOGGER.info("[GlobalSCMConfigBean]: Updating Max Carrier Booking Threshold to LKR " + threshold);
        this.maxCarrierBookingThresholdLkr = threshold;
    }

    @Override
    @Lock(LockType.READ)
    public int getDefaultSafetyStockThreshold() {
        return defaultSafetyStockThreshold;
    }

    @Override
    @Lock(LockType.WRITE)
    public void setDefaultSafetyStockThreshold(int threshold) {
        LOGGER.info("[GlobalSCMConfigBean]: Updating Default Safety Stock Threshold to " + threshold);
        this.defaultSafetyStockThreshold = threshold;
    }

    @Override
    @Lock(LockType.READ)
    public String getSystemEnvironment() {
        return systemEnvironment;
    }

    @Override
    @Lock(LockType.WRITE)
    public void setSystemEnvironment(String env) {
        this.systemEnvironment = env;
    }

    @Override
    @Lock(LockType.READ)
    public Map<String, String> getAllPortStatuses() {
        return new HashMap<>(portOperatingStatus);
    }

    @Override
    @Lock(LockType.WRITE)
    public void updatePortStatus(String portCode, String status) {
        LOGGER.info("[GlobalSCMConfigBean]: Updating port status for " + portCode + " -> " + status);
        portOperatingStatus.put(portCode, status);
    }

    @Override
    @Lock(LockType.READ)
    public Map<String, Object> getSystemConfigSnapshot() {
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("maxCarrierBookingThresholdLkr", maxCarrierBookingThresholdLkr);
        snapshot.put("defaultSafetyStockThreshold", defaultSafetyStockThreshold);
        snapshot.put("systemEnvironment", systemEnvironment);
        snapshot.put("portStatuses", new HashMap<>(portOperatingStatus));
        return snapshot;
    }
}
