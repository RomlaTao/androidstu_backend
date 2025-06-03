package com.example.mealservice.repositories;

import com.example.mealservice.entities.Meal;
import com.example.mealservice.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByType(MealType type);
    List<Meal> findByNameContainingIgnoreCase(String name);
    
    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.foods WHERE m.id = :id")
    Optional<Meal> findByIdWithFoods(@Param("id") Long id);
    
    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.foods")
    List<Meal> findAllWithFoods();
} 