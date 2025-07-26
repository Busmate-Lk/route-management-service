package com.busmate.routeschedule.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class StopSearchRequest {
    private String searchTerm;
    private String city;
    private String address;
    private Boolean isAccessible;
    
    @Min(value = 0, message = "Page number must be 0 or greater")
    private Integer page;
    
    @Min(value = 1, message = "Page size must be at least 1")
    private Integer size;
    
    private String sortBy;
    private String sortDirection;
}
