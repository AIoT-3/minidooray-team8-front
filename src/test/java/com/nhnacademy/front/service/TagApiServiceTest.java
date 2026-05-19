package com.nhnacademy.front.service;

import com.nhnacademy.front.dto.task.TagCreateRequest;
import com.nhnacademy.front.dto.task.TagDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private TagApiService tagApiService;

    @BeforeEach
    void setUp() {
        tagApiService = new TagApiService(restTemplate);
    }

    @Test
    void testCreateTag() {
        TagCreateRequest request = new TagCreateRequest("Tag1");
        TagDto response = new TagDto(1L, "Tag1");
        when(restTemplate.postForEntity(eq("/projects/1/tags"), eq(request), eq(TagDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        TagDto result = tagApiService.createTag(1L, request);

        assertEquals("Tag1", result.name());
        verify(restTemplate).postForEntity("/projects/1/tags", request, TagDto.class);
    }

    @Test
    void testUpdateTag() {
        TagCreateRequest request = new TagCreateRequest("Tag2");
        TagDto response = new TagDto(1L, "Tag2");
        when(restTemplate.exchange(eq("/projects/1/tags/2"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(TagDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        TagDto result = tagApiService.updateTag(1L, 2L, request);

        assertEquals("Tag2", result.name());
        verify(restTemplate).exchange(eq("/projects/1/tags/2"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(TagDto.class));
    }

    @Test
    void testDeleteTag() {
        tagApiService.deleteTag(1L, 2L);
        verify(restTemplate).delete("/projects/1/tags/2");
    }

    @Test
    void testGetTags() {
        TagDto[] response = {new TagDto(1L, "Tag1")};
        when(restTemplate.getForEntity(eq("/projects/1/tags"), eq(TagDto[].class)))
                .thenReturn(ResponseEntity.ok(response));

        List<TagDto> result = tagApiService.getTags(1L);

        assertEquals(1, result.size());
        verify(restTemplate).getForEntity("/projects/1/tags", TagDto[].class);
    }
}
