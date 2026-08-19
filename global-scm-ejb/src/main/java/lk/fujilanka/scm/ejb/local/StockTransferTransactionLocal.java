package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;

@Local
public interface StockTransferTransactionLocal {
    boolean executeStockTransfer(Long sourceItemId, Long targetItemId, int transferQuantity, String username);
}
