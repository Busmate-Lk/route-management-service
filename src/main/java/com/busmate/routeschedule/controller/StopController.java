package com.busmate.routeschedule.controller;

import com.busmate.routeschedule.dto.request.StopRequest;
import com.busmate.routeschedule.dto.request.StopSearchRequest;
import com.busmate.routeschedule.dto.response.PagedResponse;
import com.busmate.routeschedule.dto.response.StopResponse;
import com.busmate.routeschedule.service.StopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/stops")
@RequiredArgsConstructor
@Tag(name = "Bus Stop Management", description = "APIs for managing bus stops with advanced search, filter, and sort capabilities")
public class StopController {
    private final StopService stopService;

    @Operation(summary = "Create a new bus stop", description = "Creates a new bus stop with the provided details. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stop created successfully",
                    content = @Content(schema = @Schema(implementation = StopResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Stop with same name already exists in the city")
    })
    @PostMapping
    public ResponseEntity<StopResponse> createStop(
            @Valid @RequestBody StopRequest request,
            Authentication authentication) {
        String userId = authentication.getName();
        StopResponse response = stopService.createStop(request, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get stop by ID", description = "Retrieves a specific bus stop by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stop found",
                    content = @Content(schema = @Schema(implementation = StopResponse.class))),
            @ApiResponse(responseCode = "404", description = "Stop not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StopResponse> getStopById(
            @Parameter(description = "UUID of the stop to retrieve") @PathVariable UUID id) {
        StopResponse response = stopService.getStopById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Search stops with filters", 
            description = "Retrieves a paginated list of bus stops with advanced filtering, sorting, and pagination. " +
                         "If no parameters provided, returns all stops with default pagination (page=0, size=20, sort=name,asc)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of stops",
                    content = @Content(schema = @Schema(implementation = PagedResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters")
    })
    @GetMapping
    public ResponseEntity<PagedResponse<StopResponse>> getAllStops(
            @Parameter(description = "Search and filter parameters") @Valid StopSearchRequest searchRequest) {
        PagedResponse<StopResponse> responses = stopService.getAllStops(searchRequest);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Update a bus stop", description = "Updates an existing bus stop with new details. Requires authentication.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stop updated successfully",
                    content = @Content(schema = @Schema(implementation = StopResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Stop not found"),
            @ApiResponse(responseCode = "409", description = "Stop with same name already exists in the city")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StopResponse> updateStop(
            @Parameter(description = "UUID of the stop to update") @PathVariable UUID id,
            @Valid @RequestBody StopRequest request,
            Authentication authentication) {
        String userId = authentication.getName();
        StopResponse response = stopService.updateStop(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a bus stop", description = "Deletes a bus stop by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Stop deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Stop not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStop(
            @Parameter(description = "UUID of the stop to delete") @PathVariable UUID id) {
        stopService.deleteStop(id);
        return ResponseEntity.noContent().build();
    }
}
