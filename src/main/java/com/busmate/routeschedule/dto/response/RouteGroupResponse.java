package com.busmate.routeschedule.dto.response;

import com.busmate.routeschedule.enums.RoadTypeEnum;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class RouteGroupResponse {
    private UUID id;
    private String name;
    private String description;
    private String routeCode;
    private RoadTypeEnum roadType;
    private List<RouteResponse> routes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
