package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;
import java.util.Map;

@Local
public interface HealthServiceLocal {
    Map<String, Object> getHealthStatus();
}
