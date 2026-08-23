package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;
import lk.fujilanka.scm.core.dto.DraftCargoItem;
import lk.fujilanka.scm.core.entity.Shipment;

import java.util.List;

@Local
public interface ShipmentDraftSessionLocal {
    void setRouteDetails(String originPort, String destinationPort, String transportMode);
    void addCargoItem(DraftCargoItem item);
    void removeCargoItem(String sku);
    List<DraftCargoItem> getDraftItems();
    int getTotalItemCount();
    double getTotalEstimatedWeightKg();
    double getTotalEstimatedCostLkr();
    String getOriginPort();
    String getDestinationPort();
    String getTransportMode();
    void clearDraft();
    Shipment finalizeAndDispatch(String carrier, String trackingPrefix, String username);
    void discardSession();
}
