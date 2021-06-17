package com.petrusova.urlshortener.resource;

import com.petrusova.urlshortener.service.StatisticsBusinessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StatisticResourceUnitTest {

    @Mock
    private StatisticsBusinessService statisticsBusinessService;
    @InjectMocks
    private StatisticResource statisticResource;

    @Test
    public void getStatistics() {
        // Given
        String accountId = "accountId";
        Map<String, Integer> stats = new HashMap<>();
        stats.put("longUrl1", 9);
        when(statisticsBusinessService.getStatistics(accountId)).thenReturn(stats);

        // When
        ResponseEntity<Map<String, Integer>> responseEntity = statisticResource.getStatistics(accountId);

        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Map<String, Integer> statistics = responseEntity.getBody();
        assertThat(statistics).isNotNull().hasSize(1).containsEntry("longUrl1", 9);
    }

    @Test
    public void getStatistics_emptyAccountId() {
        // Given
        // When
        ResponseEntity<Map<String, Integer>> responseEntity = statisticResource.getStatistics("");

        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNull();
    }


    @Test
    public void getStatistics_emptyStatistics() {
        // Given
        String accountId = "accountId";
        when(statisticsBusinessService.getStatistics(accountId)).thenReturn(emptyMap());

        // When
        ResponseEntity<Map<String, Integer>> responseEntity = statisticResource.getStatistics(accountId);

        // Then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isNull();
    }
}