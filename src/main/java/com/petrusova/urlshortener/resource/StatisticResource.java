package com.petrusova.urlshortener.resource;

import com.petrusova.urlshortener.service.StatisticsService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/statistic")
public class StatisticResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticResource.class);
    private final StatisticsService statisticsService;

    @Autowired
    public StatisticResource(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Map<String, Integer>> getStatistics(@PathVariable String accountId) {
        if (Strings.isEmpty(accountId)) {
            return ResponseEntity.badRequest().build();
        }
        LOGGER.info("Getting statistics for account '{}'.", accountId);
        Map<String, Integer> statistics = statisticsService.getStatistics(accountId);
        if (statistics.isEmpty()) {
            LOGGER.warn("Problem getting statistics for account id '{}'.", accountId);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(statistics);
    }
}
