package com.cluster.warehouse.service.impl;

import com.cluster.warehouse.config.ApplicationProperties;
import com.cluster.warehouse.domain.Deal;
import com.cluster.warehouse.repository.DealRepository;
import com.cluster.warehouse.service.DealService;
import com.cluster.warehouse.service.pool.ForkJoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Service Implementation for managing Deal.
 */
@Service
public class DealServiceImpl implements DealService {

    private final Logger log = LoggerFactory.getLogger(DealServiceImpl.class);

    private final String UPLOAD_DIR;

    private final DealRepository dealRepository;
    private final ForkJoinService forkJoinService;

    public DealServiceImpl(DealRepository dealRepository,
						   ForkJoinService forkJoinService,
						   ApplicationProperties properties,
						   MongoTemplate mongoTemplate
    ) {
        this.dealRepository = dealRepository;
        this.forkJoinService = forkJoinService;
        this.UPLOAD_DIR = properties.getBatch().getUpload().getDir();
    }

    /**
     * Get all the deals.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    public Page<Deal> findAll(Pageable pageable) {
        log.debug("Request to get all Deals");
        return dealRepository.findAll(pageable);
    }


    /**
     * Get one deal by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<Deal> findOne(String id) {
        log.debug("Request to get Deal : {}", id);
        return dealRepository.findById(id);
    }

    /**
     * Save a file.
     *
     * @param file the entity to save
     */
    public Map<String, Integer> uploadFile(MultipartFile file) {
        log.debug("Request to upload file to path: {}", UPLOAD_DIR);
        Map<String, Integer> result = new HashMap<>();
        String fileName = file.getOriginalFilename();
        File dir = new File(UPLOAD_DIR);
        try {
			boolean dirExists = dir.exists() || dir.mkdirs();
            Path location = Paths.get(UPLOAD_DIR, File.separator, StringUtils.cleanPath(
                    Objects.requireNonNull(fileName)));
            Files.copy(file.getInputStream(), location, StandardCopyOption.REPLACE_EXISTING);
            return forkJoinService.beginProcess(file, location);
        } catch (Exception e) {
			result.put("path", 500);
        }
        return result;

    }

    @Override
    public boolean exists(String fileName) {
        Path location = Paths.get(UPLOAD_DIR + File.separator + StringUtils.cleanPath(
                Objects.requireNonNull(fileName)));
        return location.toFile().exists();
    }

    /**
     * Search for the Deal corresponding to the query.
     *
     * @param query    the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    public Page<Deal> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Deals for source file {}", query);
        return dealRepository.findBySourceContaining(query, pageable);
    }

}
