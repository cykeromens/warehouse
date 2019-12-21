package com.cluster.warehouse.service.impl;

import com.cluster.warehouse.domain.Report;
import com.cluster.warehouse.repository.ReportRepository;
import com.cluster.warehouse.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Service Implementation for managing Report.
 */
@Service
public class ReportServiceImpl implements ReportService {

	private final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

	private final ReportRepository reportRepository;
	private final MongoTemplate mongoTemplate;

	public ReportServiceImpl(ReportRepository reportRepository, MongoTemplate mongoTemplate) {
		this.reportRepository = reportRepository;
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * Save a report.
	 *
	 * @param report the entity to save
	 * @return the persisted entity
	 */
	@Override
	public Report save(Report report) {
		log.debug("Request to save Report : {}", report);
		return reportRepository.save(report);
	}

	/**
	 * Get all the reports.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	public Page<Report> findAll(Pageable pageable) {
		log.debug("Request to get all Reports");
		return reportRepository.findAll(pageable);
	}


	/**
	 * Get one report by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	public Optional<Report> findOne(String id) {
		log.debug("Request to get Report : {}", id);
		return reportRepository.findById(id);
	}

	@Override
	public List<Report> generateReport() {
		log.debug("Request to get Report by page");

		Aggregation aggregation = newAggregation(group("fromIsoCode", "toIsoCode")
						.sum("fromIsoCode").as("total"),
				sort(Sort.Direction.ASC, previousOperation(), "fromIsoCode"));

		AggregationResults<Report> groupResults = mongoTemplate.aggregate(
				aggregation, "deal", Report.class);

		List<Report> salesReport = groupResults.getMappedResults();


//
//        //show the use of $or operator
//        Query query = new Query();
//        query.addCriteria(Criteria
//                .where("fromIsoCode").exists(true).and("toIsoCode").exists(true)
//                .orOperator(Criteria.where("fromIsoCode").is("appleD"),
//                        Criteria.where("name").is("appleE")));
//        Update update = new Update();
//
//        //update age to 11
//        update.set("fromIsoCode", 11);
//
//        //remove the createdDate field
//        update.unset("createdDate");

		return salesReport;
	}

}
