package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;

@Local
public interface ShipmentServiceLocal {
    String getSystemStatus();
}