package com.cluster.warehouse.service.dealpool;

import liquibase.util.file.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JoiningComponent {

    private final Logger log = LoggerFactory.getLogger(JoiningComponent.class);

    @Value("${app.upload.delimiter}")
    private final String DELIMITER = ";";
    private static final String DEAL_SQL_INSERT
            = "INSERT INTO " +
            "deal (tag_id, from_iso_code, from_country, to_iso_code, to_country, deal_time," +
            " amount, source, source_format, uploaded_date )" +
            " VALUES (?,?,?,?,?,?,?,?,?,?)";

    private static final String INVALID_SQL_INSERT
            = "INSERT INTO " +
            "invalid_deal (tag_id, from_iso_code, from_country, to_iso_code, to_country, deal_time," +
            " amount, source, source_format, uploaded_date, reason )" +
            " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

    private static final String UPDATE_RECORD_COUNT
            = "UPDATE INTO " +
            "record_count (tag_id, from_iso_code, from_country, to_iso_code, to_country, deal_time," +
            " amount, source, source_format, uploaded_date, reason )" +
            " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

    private List<String> failedTransaction = new ArrayList<>();
    private final JdbcTemplate jdbcTemplate;

    public JoiningComponent(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public synchronized Map<String, Integer> executeBatch(List<String> deals, Path path) {
        int failedCount = 0, affectedCount = 0;
        Map<String, Integer> result = new HashMap<>();
        try {
            int[] affectedRows = jdbcTemplate.batchUpdate(DEAL_SQL_INSERT, saveValidDeal(deals, path));
            log.info("Checking if all rows were inserted successfully...");
            for (int affectedRow : affectedRows) {
                if (affectedRow == Statement.EXECUTE_FAILED) {
                    failedCount++;
                } else {
                    affectedCount++;
                }
            }
        } catch (DataAccessException e) {
            Throwable rootCause = e.getRootCause();

            log.error("Batch update failed: ", e);
            if (rootCause instanceof BatchUpdateException) {
                BatchUpdateException bue = (BatchUpdateException) rootCause;
                int lastSuccessfullRow = bue.getUpdateCounts().length;
                int failurePoint = lastSuccessfullRow + 1;
                int continuePoint = lastSuccessfullRow + 2;

                affectedCount += lastSuccessfullRow;
                failedCount++;

                log.info("Last successful row: " + lastSuccessfullRow);
                log.info("Failed row: " + failurePoint);
                log.info("continue point: " + continuePoint);
            }
        } catch (Exception exp) {
            log.error("Exception occured", exp);
            throw new RuntimeException(exp);
        } finally {
            result.put("valid", affectedCount);
            result.put("invalid", failedCount);
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {DataAccessException.class})
    public void saveInvalidDeal(String line, String reason, Path path) {
        if (!line.isEmpty()) {
            String data[] = line.split(DELIMITER);
            final long datum5 = Long.parseLong(data[5]);
            final LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(datum5), ZoneOffset.UTC);
            jdbcTemplate.update(INVALID_SQL_INSERT, data[0], data[2], data[3], data[4],
                    Timestamp.valueOf(dateTime), Double.parseDouble(data[6]), path.toString(),
                    FilenameUtils.getExtension(path.toString()), Timestamp.valueOf(LocalDateTime.now()),
                    reason);

        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = {DataAccessException.class})
    public BatchPreparedStatementSetter saveValidDeal(List<String> deals, Path path) {
        return new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement pStmt, int i) throws SQLException {
                String line = deals.get(i);
                if (!line.isEmpty()) {
                    String data[] = line.split(DELIMITER);
                    final long datum5 = Long.parseLong(data[5]);
                    final LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(datum5), ZoneOffset.UTC);

                    pStmt.setString(1, data[0]);
                    pStmt.setString(2, data[1]);
                    pStmt.setString(3, data[2]);
                    pStmt.setString(4, data[3]);
                    pStmt.setString(5, data[4]);
                    pStmt.setTimestamp(6, Timestamp.valueOf(dateTime));
                    pStmt.setDouble(7, Double.parseDouble(data[6]));
                    pStmt.setString(8, path.toString());
                    pStmt.setString(9, FilenameUtils.getExtension(path.toString()));
                    pStmt.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                }
            }

            @Override
            public int getBatchSize() {
                return deals.size();
            }
        };
    }


    public void updateRecordCount() {

    }

}
