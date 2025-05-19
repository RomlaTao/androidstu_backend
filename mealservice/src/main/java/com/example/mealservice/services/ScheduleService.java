package com.example.mealservice.services;

import com.example.mealservice.dtos.ScheduleDTO;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleService {
    
    ScheduleDTO createSchedule(ScheduleDTO scheduleDTO);
    
    ScheduleDTO getScheduleById(Long id);
    
    List<ScheduleDTO> getSchedulesByUserId(Long userId);
    
    List<ScheduleDTO> getSchedulesInDateRange(Long userId, LocalDate startDate, LocalDate endDate);
    
    ScheduleDTO updateSchedule(Long id, ScheduleDTO scheduleDTO);
    
    void deleteSchedule(Long id);
} 