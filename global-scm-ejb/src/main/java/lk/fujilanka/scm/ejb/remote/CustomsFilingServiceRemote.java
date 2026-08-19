package lk.fujilanka.scm.ejb.remote;

import jakarta.ejb.Remote;
import lk.fujilanka.scm.core.entity.CustomsFiling;

import java.util.List;

/**
 * Remote EJB Interface for National Port Customs Authority Gateway (Hambantota/Colombo Port Authority)
 * Allows remote customs gateways to pull declared manifests and issue electronic clearance approvals over RMI.
 */
@Remote
public interface CustomsFilingServiceRemote {
    CustomsFiling createFiling(Long shipmentId, String declarationDetails, String username);
    CustomsFiling updateFilingStatus(Long filingId, String newStatus, String username);
    List<CustomsFiling> getAllFilings();
    CustomsFiling findById(Long id);
}
