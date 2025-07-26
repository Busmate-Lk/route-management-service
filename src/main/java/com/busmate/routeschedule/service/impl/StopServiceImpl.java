package com.busmate.routeschedule.service.impl;

import com.busmate.routeschedule.dto.request.StopRequest;
import com.busmate.routeschedule.dto.request.StopSearchRequest;
import com.busmate.routeschedule.dto.response.PagedResponse;
import com.busmate.routeschedule.dto.response.StopResponse;
import com.busmate.routeschedule.entity.Stop;
import com.busmate.routeschedule.repository.StopRepository;
import com.busmate.routeschedule.service.StopService;
import com.busmate.routeschedule.exception.ResourceNotFoundException;
import com.busmate.routeschedule.exception.ConflictException;
import com.busmate.routeschedule.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StopServiceImpl implements StopService {
    private final StopRepository stopRepository;
    private final MapperUtils mapperUtils;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final String DEFAULT_SORT_BY = "name";
    private static final String DEFAULT_SORT_DIRECTION = "ASC";
    private static final List<String> VALID_SORT_FIELDS = Arrays.asList(
            "name", "city", "createdAt", "updatedAt", "isAccessible");

    @Override
    public StopResponse createStop(StopRequest request, String userId) {
        if (stopRepository.existsByNameAndLocation_City(request.getName(), request.getLocation().getCity())) {
            throw new ConflictException("Stop with name " + request.getName() + " already exists in city " + request.getLocation().getCity());
        }

        Stop stop = mapperUtils.map(request, Stop.class);
        stop.setCreatedBy(userId);
        stop.setUpdatedBy(userId);
        Stop savedStop = stopRepository.save(stop);
        return mapperUtils.map(savedStop, StopResponse.class);
    }

    @Override
    public StopResponse getStopById(UUID id) {
        Stop stop = stopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stop not found with id: " + id));
        return mapperUtils.map(stop, StopResponse.class);
    }

    @Override
    public PagedResponse<StopResponse> getAllStops(StopSearchRequest searchRequest) {
        // Apply defaults if search request is null
        if (searchRequest == null) {
            searchRequest = new StopSearchRequest();
            searchRequest.setPage(DEFAULT_PAGE);
            searchRequest.setSize(DEFAULT_SIZE);
            searchRequest.setSortBy(DEFAULT_SORT_BY);
            searchRequest.setSortDirection(DEFAULT_SORT_DIRECTION);
        }

        // Validate sort field
        if (searchRequest.getSortBy() == null || !VALID_SORT_FIELDS.contains(searchRequest.getSortBy())) {
            searchRequest.setSortBy(DEFAULT_SORT_BY);
        }

        // Validate sort direction
        if (searchRequest.getSortDirection() == null || 
            !Arrays.asList("ASC", "DESC").contains(searchRequest.getSortDirection().toUpperCase())) {
            searchRequest.setSortDirection(DEFAULT_SORT_DIRECTION);
        }

        // Validate pagination parameters
        int page = searchRequest.getPage() != null ? searchRequest.getPage() : DEFAULT_PAGE;
        int size = searchRequest.getSize() != null ? searchRequest.getSize() : DEFAULT_SIZE;
        if (page < 0) page = DEFAULT_PAGE;
        if (size <= 0 || size > 100) size = DEFAULT_SIZE;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(searchRequest.getSortDirection()), 
                        searchRequest.getSortBy())
        );

        Page<Stop> stopPage = stopRepository.searchStops(
                searchRequest.getSearchTerm(),
                searchRequest.getCity(),
                searchRequest.getAddress(),
                searchRequest.getIsAccessible(),
                pageable
        );

        return new PagedResponse<>(
                stopPage.getContent().stream()
                        .map(stop -> mapperUtils.map(stop, StopResponse.class))
                        .toList(),
                stopPage.getNumber(),
                stopPage.getSize(),
                stopPage.getTotalElements(),
                stopPage.getTotalPages(),
                stopPage.isLast()
        );
    }

    @Override
    public StopResponse updateStop(UUID id, StopRequest request, String userId) {
        Stop stop = stopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stop not found with id: " + id));

        if (!stop.getName().equals(request.getName()) &&
                stopRepository.existsByNameAndLocation_City(request.getName(), request.getLocation().getCity())) {
            throw new ConflictException("Stop with name " + request.getName() + " already exists in city " + request.getLocation().getCity());
        }

        mapperUtils.map(request, stop);
        stop.setUpdatedBy(userId);
        Stop updatedStop = stopRepository.save(stop);
        return mapperUtils.map(updatedStop, StopResponse.class);
    }

    @Override
    public void deleteStop(UUID id) {
        if (!stopRepository.existsById(id)) {
            throw new ResourceNotFoundException("Stop not found with id: " + id);
        }
        stopRepository.deleteById(id);
    }
}
