package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;
import lk.fujilanka.scm.core.entity.CustomsFiling;

import java.util.List;

@Local
public interface CustomsFilingServiceLocal {
    CustomsFiling createFiling(Long shipmentId, String declarationDetails, String username);
    CustomsFiling updateFilingStatus(Long filingId, String newStatus, String username);
    List<CustomsFiling> getAllFilings();
    CustomsFiling findById(Long id);
}
