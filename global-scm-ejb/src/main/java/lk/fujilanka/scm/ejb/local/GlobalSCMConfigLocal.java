package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;
import java.util.Map;

@Local
public interface GlobalSCMConfigLocal {
    double getMaxCarrierBookingThresholdLkr();
    void setMaxCarrierBookingThresholdLkr(double threshold);

    int getDefaultSafetyStockThreshold();
    void setDefaultSafetyStockThreshold(int threshold);

    String getSystemEnvironment();
    void setSystemEnvironment(String env);

    Map<String, String> getAllPortStatuses();
    void updatePortStatus(String portCode, String status);

    Map<String, Object> getSystemConfigSnapshot();
}
