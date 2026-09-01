package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;

@Local
public interface CargoEventProducerLocal {
    boolean sendCargoEvent(String trackingNumber, String eventType, String details, String initiatedBy);
}
