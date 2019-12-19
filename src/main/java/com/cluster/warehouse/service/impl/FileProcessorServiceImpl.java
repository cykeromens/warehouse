package com.cluster.warehouse.service.impl;

import com.cluster.warehouse.domain.Deal;
import com.cluster.warehouse.domain.InvalidDeal;
import com.cluster.warehouse.domain.Summary;
import com.cluster.warehouse.service.FileProcessorService;
import com.cluster.warehouse.service.InvalidDealService;
import com.cluster.warehouse.service.SummaryService;
import com.cluster.warehouse.service.dealpool.ForkJoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service Implementation for managing FileLoader.
 */
@Service
@Transactional
public class FileProcessorServiceImpl implements FileProcessorService {

    private final Logger log = LoggerFactory.getLogger(FileProcessorServiceImpl.class);

    @Value("${app.upload.delimiter}")
    private final static String DELIMITER = ";";
    @Value("${app.upload.dir}")
    private String uploadDir = "./";
    @Value("${app.thread.count}")
    private static final int CONCURRENCY_COUNT = 10;

    private final ForkJoinService forkJoinService;
    private InvalidDealService invalidDealService;
    private SummaryService summaryService;
    private final Summary summary = new Summary();

    private final AtomicInteger count = new AtomicInteger();
    private final AtomicInteger invalidCount = new AtomicInteger();

    public FileProcessorServiceImpl(ForkJoinService forkJoinService,
                                    InvalidDealService invalidDealService,
                                    SummaryService summaryService
    ) {
        this.forkJoinService = forkJoinService;
        this.invalidDealService = invalidDealService;
        this.summaryService = summaryService;
    }


//    private synchronized long saveValidDeal(CSVReader reader, String fileName){
//        final String format = FilenameUtils.getExtension(fileName);
//        try {
//            List<String[]> lines = reader.bu();
//
//            for (String line[] : lines) {
//
//                if (length <= 10000) {
//
//                    dealService.flush();
//                }
//            }
//
//        }catch(Exception ex){}
//
//
//        try {
//            while ((nextLine = reader.readNext()) != null) {
//                final String[] data = nextLine[0].split(";");
//                final long datum5 = Long.parseLong(data[5]);
//                final ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(datum5), ZoneOffset.UTC);
//                        deal.tagId(data[0])
//                        .fromIsoCode(data[1])
//                        .fromCountry(data[2])
//                        .toIsoCode(data[3])
//                        .toCountry(data[4])
//                        .time(dateTime)
//                        .amount(new BigDecimal(data[6]))
//                        .source(fileName)
//                        .sourceFormat(format)
//                        .uploadedDate(LocalDate.now());
//                dealService.save(deal);
//                log.debug("Saved deal {}", deal);
//                count.getAndIncrement();
//            }
//            dealService.flush();
//
//        }catch (PersistenceException e){
//            saveInvalidDeal(deal, e.getMessage());
//        }catch (Exception e){
//            log.error(e.getMessage());
//            throw new RuntimeException("Problem encountered reading the CSV file. "+ e.getMessage());
//        }finally {
//            dealService.flush();
//        }
//        return count.get();
//    }

    private synchronized void saveInvalidDeal(Deal deal, String reason) {
        final InvalidDeal invalidDeal = new InvalidDeal(deal, reason);
        invalidDealService.save(invalidDeal);
        log.debug("Saved invalid deal {}", invalidDeal);
        invalidCount.getAndIncrement();
    }
}
