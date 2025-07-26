package com.busmate.routeschedule.entity;

import com.busmate.routeschedule.enums.RoadTypeEnum;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "route_group")
public class RouteGroup extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false, unique = true)
    private String routeCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoadTypeEnum roadType;

    @OneToMany(mappedBy = "routeGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Route> routes;
}
