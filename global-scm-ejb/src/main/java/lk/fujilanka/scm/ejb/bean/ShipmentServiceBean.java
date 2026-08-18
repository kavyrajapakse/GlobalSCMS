package lk.fujilanka.scm.ejb.bean;

import jakarta.ejb.Stateless;
import lk.fujilanka.scm.ejb.local.ShipmentServiceLocal;

@Stateless
public class ShipmentServiceBean implements ShipmentServiceLocal {

    @Override
    public String getSystemStatus() {
        return "Global SCM EJB Business Service is ONLINE and healthy.";
    }
}