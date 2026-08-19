package lk.fujilanka.scm.ejb.local;

import jakarta.ejb.Local;
import lk.fujilanka.scm.core.entity.AuditLog;
import java.util.List;

@Local
public interface AuditLogServiceLocal {
    List<AuditLog> getRecentAuditLogs(int maxResults);
}
