package com.busmate.routeschedule.service;

import com.busmate.routeschedule.dto.request.StopRequest;
import com.busmate.routeschedule.dto.request.StopSearchRequest;
import com.busmate.routeschedule.dto.response.PagedResponse;
import com.busmate.routeschedule.dto.response.StopResponse;
import java.util.UUID;

public interface StopService {
    StopResponse createStop(StopRequest request, String userId);
    StopResponse getStopById(UUID id);
    PagedResponse<StopResponse> getAllStops(StopSearchRequest searchRequest);
    StopResponse updateStop(UUID id, StopRequest request, String userId);
    void deleteStop(UUID id);
}
