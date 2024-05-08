package com.itkon.school.repository;

import com.itkon.school.model.ItClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItClassRepository extends JpaRepository<ItClass, Integer> {
}