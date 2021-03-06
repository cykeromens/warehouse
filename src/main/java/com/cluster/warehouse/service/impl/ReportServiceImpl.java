package com.cluster.warehouse.service.impl;

import com.cluster.warehouse.domain.Report;
import com.cluster.warehouse.repository.ReportRepository;
import com.cluster.warehouse.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.cluster.warehouse.config.Constants.*;
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
		GroupOperation sumTotalCityPop = group(_FROM_ISO_CODE, _TO_ISO_CODE)
				.count().as(TOTAL);
		ProjectionOperation projectToMatchModel = project()
				.andExpression(_ID).as(FROM_ISO_CODE)
				.andExpression(_TO_ISO_CODE).as(TO_ISO_CODE)
				.andExpression(TOTAL).as(TOTAL);

		Aggregation aggregation = newAggregation(
				sumTotalCityPop, projectToMatchModel.and(TOTAL));
		AggregationResults<Report> groupResults = mongoTemplate.aggregate(
				aggregation, VALID_DEAL, Report.class);

		return groupResults.getMappedResults();
	}

}
