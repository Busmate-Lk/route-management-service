package com.busmate.routeschedule.repository;

import com.busmate.routeschedule.entity.RouteGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RouteGroupRepository extends JpaRepository<RouteGroup, UUID> {
    boolean existsByName(String name);
    boolean existsByRouteCode(String routeCode);
}
