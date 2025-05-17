package com.example.workoutservice.controllers;

import com.example.workoutservice.dtos.ScheduleDTO;
import com.example.workoutservice.services.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    
    private final ScheduleService scheduleService;
    
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }
    
    @PostMapping
    public ResponseEntity<ScheduleDTO> createSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO) {
        return new ResponseEntity<>(scheduleService.createSchedule(scheduleDTO), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(scheduleService.getSchedulesByUserId(userId));
    }
    
    @GetMapping("/user/{userId}/date-range")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesInDateRange(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.getSchedulesInDateRange(userId, startDate, endDate));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleDTO> updateSchedule(
            @PathVariable Long id, 
            @Valid @RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, scheduleDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }
}
