package com.shortner.url_shortner.repository;

import com.shortner.url_shortner.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long> {
    Optional<UrlMapping> findByCode(String code);
    List<UrlMapping> findByLongUrl(String longUrl);

    @Query("SELECT COUNT(u) FROM UrlMapping u WHERE LENGTH(u.code) = :length")
    long countByCodeLength(@Param("length") int length);

    // We need to start with length = 5
    @Query("SELECT COALESCE(MAX(LENGTH(u.code)), 5) FROM UrlMapping u")
    int findMaxCodeLength();
}
