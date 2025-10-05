package io.mosip.authentication.service.repository;

import io.mosip.authentication.service.entity.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditEventRepository extends JpaRepository<AuditEvent, String> {
}