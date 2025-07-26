package com.busmate.routeschedule.repository;

import com.busmate.routeschedule.entity.Stop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StopRepository extends JpaRepository<Stop, UUID> {
    boolean existsByNameAndLocation_City(String name, String city);

    @Query("SELECT s FROM Stop s WHERE " +
            "(:searchTerm IS NULL OR " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "(:city IS NULL OR s.location.city = :city) AND " +
            "(:address IS NULL OR LOWER(s.location.address) LIKE LOWER(CONCAT('%', :address, '%'))) AND " +
            "(:isAccessible IS NULL OR s.isAccessible = :isAccessible)")
    Page<Stop> searchStops(
            @Param("searchTerm") String searchTerm,
            @Param("city") String city,
            @Param("address") String address,
            @Param("isAccessible") Boolean isAccessible,
            Pageable pageable
    );
}
