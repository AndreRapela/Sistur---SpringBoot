package br.gov.noronha.sistur.modules.analytics.repository;

import br.gov.noronha.sistur.modules.analytics.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    
    long countByTargetTypeAndTargetId(String targetType, Long targetId);

    long countByActionType(String actionType);

    long countByTargetTypeAndTargetIdAndActionType(String targetType, Long targetId, String actionType);

    long countByTargetTypeAndTargetIdAndActionTypeIn(String targetType, Long targetId, java.util.Collection<String> actionTypes);

    long countByActionTypeIn(java.util.Collection<String> actionTypes);

    long countByActionTypeAndTimestampAfter(String actionType, LocalDateTime timestamp);
    
    @Query("SELECT COUNT(DISTINCT a.userId) FROM AccessLog a WHERE a.userId IS NOT NULL AND a.timestamp > :since")
    long countActiveUsers(@Param("since") LocalDateTime since);
}
