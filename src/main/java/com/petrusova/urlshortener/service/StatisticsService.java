package com.petrusova.urlshortener.service;

import java.util.Map;

public interface StatisticsService {

    Map<String, Integer> getStatistics(String accountId);
}
