package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;
import lk.fujilanka.scm.core.entity.Shipment;
import java.util.List;

@Local
public interface ShipmentServiceLocal {
    Shipment createShipment(Shipment shipment, String username);
    Shipment updateShipmentStatus(Long id, String status, String username);
    List<Shipment> getAllShipments();
    List<Shipment> getShipmentsByVendor(Long vendorId);
    Shipment findById(Long id);
}