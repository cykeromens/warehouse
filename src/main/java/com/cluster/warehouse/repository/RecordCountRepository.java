package com.cluster.warehouse.repository;

import com.cluster.warehouse.domain.RecordCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordCountRepository extends JpaRepository<RecordCount, Long> {

}
