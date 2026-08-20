package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;
import lk.fujilanka.scm.core.entity.Vendor;

import java.util.List;

@Local
public interface VendorServiceLocal {
    Vendor createVendor(Vendor vendor, String username);
    List<Vendor> getAllVendors();
    Vendor findById(Long id);
    Vendor updateVendor(Vendor vendor, String username);
}
