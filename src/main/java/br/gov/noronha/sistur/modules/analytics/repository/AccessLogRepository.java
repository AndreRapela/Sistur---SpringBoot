package br.gov.noronha.sistur.modules.analytics.repository;

import br.gov.noronha.sistur.modules.analytics.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

    interface TargetCount {
        Long getTargetId();
        long getTotal();
    }

    interface ActionCount {
        String getActionType();
        long getTotal();
    }
    
    long countByTargetTypeAndTargetId(String targetType, Long targetId);

    long countByActionType(String actionType);

    long countByTargetTypeAndTargetIdAndActionType(String targetType, Long targetId, String actionType);

    long countByTargetTypeAndTargetIdAndActionTypeIn(String targetType, Long targetId, java.util.Collection<String> actionTypes);

    long countByActionTypeIn(java.util.Collection<String> actionTypes);

    long countByActionTypeAndTimestampAfter(String actionType, LocalDateTime timestamp);

    long countByActionTypeInAndTimestampAfter(java.util.Collection<String> actionTypes, LocalDateTime timestamp);

    List<AccessLog> findByTimestampAfterOrderByTimestampAsc(LocalDateTime timestamp);

    @Query("""
        SELECT a.targetId AS targetId, COUNT(a) AS total
        FROM AccessLog a
        WHERE a.targetType = 'ESTABLISHMENT' AND a.actionType = 'VIEW' AND a.targetId IS NOT NULL
        GROUP BY a.targetId
        """)
    List<TargetCount> countEstablishmentViews();

    @Query("""
        SELECT a.targetId AS targetId, COUNT(a) AS total
        FROM AccessLog a
        WHERE a.targetType = 'ESTABLISHMENT' AND a.actionType IN :actions AND a.targetId IS NOT NULL
        GROUP BY a.targetId
        """)
    List<TargetCount> countEstablishmentConversions(@Param("actions") java.util.Collection<String> actions);

    @Query("""
        SELECT a.actionType AS actionType, COUNT(a) AS total
        FROM AccessLog a
        WHERE a.targetType = :targetType AND a.targetId = :targetId
        GROUP BY a.actionType
        """)
    List<ActionCount> countActionsByTarget(
        @Param("targetType") String targetType,
        @Param("targetId") Long targetId
    );
    
    @Query("SELECT COUNT(DISTINCT a.userId) FROM AccessLog a WHERE a.userId IS NOT NULL AND a.timestamp > :since")
    long countActiveUsers(@Param("since") LocalDateTime since);
}
