package com.cluster.warehouse.repository;

import com.cluster.warehouse.domain.Summary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data MongoDB repository for the Summary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SummaryRepository extends MongoRepository<Summary, String> {

}
