package com.example.demo.reports;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import reactor.core.publisher.Flux;

/**
 * Generic CSV report generator
 * @param <TYPE>
 */
public class CSVReport<TYPE> {
    private final CsvMapper mapper = CsvMapper
            .builder()
            .addModule(new JavaTimeModule())
            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
            .build();

    private final CsvSchema schema;

    /**
     * Initialize CSV report
     * @param type type of row object
     */
    public CSVReport(Class<TYPE> type) {
        this.schema = mapper.schemaFor(type).withHeader();
    }

    /**
     * Get stream of CSV rows
     * @param rows stream of rows objects
     * @return stream of String which represents objects serialized as CSV strings
     */
    public Flux<String> getReport(Flux<TYPE> rows) {
        final var body = rows.map(row -> {
            try {
                return mapper.writer(schema.withoutHeader()).writeValueAsString(row);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        return body.startWith(String.join(String.valueOf(schema.getColumnSeparator()), schema.getColumnNames()) + "\n");
    }
}
