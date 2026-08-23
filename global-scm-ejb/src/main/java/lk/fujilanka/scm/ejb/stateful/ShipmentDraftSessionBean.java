package lk.fujilanka.scm.ejb.stateful;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.EJB;
import jakarta.ejb.PostActivate;
import jakarta.ejb.PrePassivate;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import lk.fujilanka.scm.core.dto.DraftCargoItem;
import lk.fujilanka.scm.core.entity.Shipment;
import lk.fujilanka.scm.ejb.local.ShipmentDraftSessionLocal;
import lk.fujilanka.scm.ejb.local.ShipmentServiceLocal;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Enterprise Stateful Session Bean.
 * Demonstrates conversational state management across multi-step freight drafting
 * and implements the complete EJB Component Lifecycle:
 * (@PostConstruct, @PrePassivate, @PostActivate, @Remove, @PreDestroy).
 */
@Stateful
public class ShipmentDraftSessionBean implements ShipmentDraftSessionLocal, Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ShipmentDraftSessionBean.class.getName());

    @EJB
    private ShipmentServiceLocal shipmentService;

    // Conversational state held across client interactions
    private String originPort;
    private String destinationPort;
    private String transportMode;
    private List<DraftCargoItem> draftItems;
    private String draftSessionId;

    @PostConstruct
    public void initializeStatefulSession() {
        this.draftItems = new ArrayList<>();
        this.originPort = "Colombo Port";
        this.destinationPort = "Nagoya Port";
        this.transportMode = "OCEAN";
        this.draftSessionId = "DRAFT-SES-" + (1000 + new SecureRandom().nextInt(9000));

        LOGGER.info("[ShipmentDraftSessionBean]: Initialized new stateful draft session: " + draftSessionId);
    }

    @PrePassivate
    public void handlePassivation() {
        LOGGER.info("[ShipmentDraftSessionBean]: EJB Container passivating draft session " + draftSessionId + " to disk due to memory limits.");
    }

    @PostActivate
    public void handleActivation() {
        LOGGER.info("[ShipmentDraftSessionBean]: EJB Container activating draft session " + draftSessionId + " back from secondary storage.");
    }

    @PreDestroy
    public void handlePreDestroy() {
        LOGGER.info("[ShipmentDraftSessionBean]: Destroying stateful session " + draftSessionId);
    }

    @Override
    public void setRouteDetails(String originPort, String destinationPort, String transportMode) {
        this.originPort = originPort;
        this.destinationPort = destinationPort;
        this.transportMode = transportMode;
    }

    @Override
    public void addCargoItem(DraftCargoItem item) {
        if (item != null) {
            draftItems.add(item);
            LOGGER.info("[ShipmentDraftSessionBean " + draftSessionId + "]: Added draft item: " + item.getSku() + " (" + item.getQuantity() + " units)");
        }
    }

    @Override
    public void removeCargoItem(String sku) {
        if (sku != null) {
            draftItems.removeIf(item -> item.getSku().equalsIgnoreCase(sku));
            LOGGER.info("[ShipmentDraftSessionBean " + draftSessionId + "]: Removed item SKU: " + sku);
        }
    }

    @Override
    public List<DraftCargoItem> getDraftItems() {
        return Collections.unmodifiableList(draftItems);
    }

    @Override
    public int getTotalItemCount() {
        return draftItems.stream().mapToInt(DraftCargoItem::getQuantity).sum();
    }

    @Override
    public double getTotalEstimatedWeightKg() {
        return draftItems.stream().mapToDouble(DraftCargoItem::getWeightKg).sum();
    }

    @Override
    public double getTotalEstimatedCostLkr() {
        return draftItems.stream().mapToDouble(DraftCargoItem::getEstimatedCostLkr).sum();
    }

    @Override
    public String getOriginPort() {
        return originPort;
    }

    @Override
    public String getDestinationPort() {
        return destinationPort;
    }

    @Override
    public String getTransportMode() {
        return transportMode;
    }

    @Override
    public void clearDraft() {
        draftItems.clear();
        LOGGER.info("[ShipmentDraftSessionBean " + draftSessionId + "]: Cleared all draft cargo items.");
    }

    @Override
    @Remove
    public Shipment finalizeAndDispatch(String carrier, String trackingPrefix, String username) {
        LOGGER.info("[ShipmentDraftSessionBean " + draftSessionId + "]: Finalizing conversational draft manifest for carrier " + carrier);

        if (draftItems.isEmpty()) {
            throw new IllegalStateException("Cannot finalize dispatch: Draft cargo manifest is empty.");
        }

        StringBuilder cargoDesc = new StringBuilder();
        for (int i = 0; i < draftItems.size(); i++) {
            DraftCargoItem it = draftItems.get(i);
            cargoDesc.append(it.getQuantity()).append("x ").append(it.getSku());
            if (i < draftItems.size() - 1) {
                cargoDesc.append(", ");
            }
        }

        String tracking = trackingPrefix + "-" + (1000 + new SecureRandom().nextInt(9000));
        double totalWeight = getTotalEstimatedWeightKg();
        double totalCost = getTotalEstimatedCostLkr();

        Shipment shipment = new Shipment(tracking, carrier, originPort, destinationPort, totalWeight, totalCost);
        shipment.setCargoDescription(cargoDesc.toString());
        shipment.setTransportMode(transportMode);
        shipment.setStatus("PENDING");

        // Dispatches through the transactional CMT ShipmentServiceBean
        Shipment created = shipmentService.createShipment(shipment, username);

        LOGGER.info("[ShipmentDraftSessionBean]: Successfully converted draft into active shipment #" + created.getTrackingNumber() + ". Removing session.");
        return created;
    }

    @Override
    @Remove
    public void discardSession() {
        LOGGER.info("[ShipmentDraftSessionBean " + draftSessionId + "]: Discarding draft session via @Remove.");
        draftItems.clear();
    }
}
