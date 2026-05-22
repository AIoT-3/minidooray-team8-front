package com.nhnacademy.front.service;

import com.nhnacademy.front.dto.task.MilestoneCreateRequest;
import com.nhnacademy.front.dto.task.MilestoneDetailDto;
import com.nhnacademy.front.dto.task.MilestoneDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MilestoneApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private MilestoneApiService milestoneApiService;

    @BeforeEach
    void setUp() {
        milestoneApiService = new MilestoneApiService(restTemplate);
    }

    @Test
    void testCreateMilestone() {
        MilestoneCreateRequest request = new MilestoneCreateRequest("M1", LocalDate.now(), LocalDate.now().plusDays(1));
        MilestoneDto response = new MilestoneDto(1L, "M1", LocalDate.now(), LocalDate.now().plusDays(1));
        when(restTemplate.postForEntity(eq("/projects/1/milestones"), eq(request), eq(MilestoneDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        MilestoneDto result = milestoneApiService.createMilestone(1L, request);

        assertEquals("M1", result.name());
        verify(restTemplate).postForEntity("/projects/1/milestones", request, MilestoneDto.class);
    }

    @Test
    void testUpdateMilestone() {
        MilestoneCreateRequest request = new MilestoneCreateRequest("M2", LocalDate.now(), LocalDate.now().plusDays(1));
        MilestoneDto response = new MilestoneDto(2L, "M2", LocalDate.now(), LocalDate.now().plusDays(1));
        when(restTemplate.exchange(eq("/projects/1/milestones/2"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(MilestoneDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        MilestoneDto result = milestoneApiService.updateMilestone(1L, 2L, request);

        assertEquals("M2", result.name());
        verify(restTemplate).exchange(eq("/projects/1/milestones/2"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(MilestoneDto.class));
    }

    @Test
    void testDeleteMilestone() {
        milestoneApiService.deleteMilestone(1L, 2L);
        verify(restTemplate).delete("/projects/1/milestones/2");
    }

    @Test
    void testGetMilestone() {
        MilestoneDetailDto response = new MilestoneDetailDto(1L, "M1", LocalDate.now(), LocalDate.now().plusDays(1), List.of());
        when(restTemplate.getForEntity("/projects/1/milestones/2", MilestoneDetailDto.class))
                .thenReturn(ResponseEntity.ok(response));

        MilestoneDetailDto result = milestoneApiService.getMilestone(1L, 2L);

        assertEquals("M1", result.name());
        verify(restTemplate).getForEntity("/projects/1/milestones/2", MilestoneDetailDto.class);
    }

    @Test
    void testGetMilestones() {
        MilestoneDto[] response = {new MilestoneDto(1L, "M1", LocalDate.now(), LocalDate.now().plusDays(1))};
        when(restTemplate.getForEntity(eq("/projects/1/milestones"), eq(MilestoneDto[].class)))
                .thenReturn(ResponseEntity.ok(response));

        List<MilestoneDto> result = milestoneApiService.getMilestones(1L);

        assertEquals(1, result.size());
        verify(restTemplate).getForEntity("/projects/1/milestones", MilestoneDto[].class);
    }

    @Test
    void testGetMilestones_NullBody() {
        when(restTemplate.getForEntity(anyString(), eq(MilestoneDto[].class)))
                .thenReturn(ResponseEntity.ok(null));

        List<MilestoneDto> result = milestoneApiService.getMilestones(1L);

        assertTrue(result.isEmpty());
    }
}
