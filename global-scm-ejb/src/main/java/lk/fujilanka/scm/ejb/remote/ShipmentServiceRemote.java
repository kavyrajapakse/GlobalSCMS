package lk.fujilanka.scm.ejb.remote;

import jakarta.ejb.Remote;
import lk.fujilanka.scm.core.entity.Shipment;

import java.util.List;

/**
 * Remote EJB Interface for External Ocean Carrier Lines (Maersk, MSC, COSCO)
 * Allows remote shipping partners to query freight manifests and update transport milestones over RMI.
 */
@Remote
public interface ShipmentServiceRemote {
    Shipment createShipment(Shipment shipment, String username);
    Shipment updateShipmentStatus(Long id, String status, String username);
    List<Shipment> getAllShipments();
    Shipment findById(Long id);
}
