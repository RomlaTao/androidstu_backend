package com.example.workoutservice.services.impl;

import com.example.workoutservice.dtos.ScheduledWorkoutDTO;
import com.example.workoutservice.exceptions.ResourceNotFoundException;
import com.example.workoutservice.mappers.ScheduledWorkoutMapper;
import com.example.workoutservice.entities.ScheduledWorkout;
import com.example.workoutservice.enums.WorkoutStatus;
import com.example.workoutservice.repositories.ScheduleRepository;
import com.example.workoutservice.repositories.ScheduledWorkoutRepository;
import com.example.workoutservice.repositories.WorkoutRepository;
import com.example.workoutservice.services.ScheduledWorkoutService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledWorkoutServiceImpl implements ScheduledWorkoutService {
    
    private final ScheduledWorkoutRepository scheduledWorkoutRepository;
    private final ScheduleRepository scheduleRepository;
    private final WorkoutRepository workoutRepository;
    private final ScheduledWorkoutMapper scheduledWorkoutMapper;
    
    public ScheduledWorkoutServiceImpl(ScheduledWorkoutRepository scheduledWorkoutRepository, 
                                    ScheduleRepository scheduleRepository,
                                    WorkoutRepository workoutRepository,
                                    ScheduledWorkoutMapper scheduledWorkoutMapper) {
        this.scheduledWorkoutRepository = scheduledWorkoutRepository;
        this.scheduleRepository = scheduleRepository;
        this.workoutRepository = workoutRepository;
        this.scheduledWorkoutMapper = scheduledWorkoutMapper;
    }
    
    @Override
    @Transactional
    public ScheduledWorkoutDTO createScheduledWorkout(ScheduledWorkoutDTO scheduledWorkoutDTO) {
        // Validate that schedule exists
        if (scheduledWorkoutDTO.getScheduleId() != null) {
            scheduleRepository.findById(scheduledWorkoutDTO.getScheduleId())
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + scheduledWorkoutDTO.getScheduleId()));
        }
        
        // Validate that workout exists
        workoutRepository.findById(scheduledWorkoutDTO.getWorkoutId())
            .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + scheduledWorkoutDTO.getWorkoutId()));
        
        // Set initial status if not provided
        if (scheduledWorkoutDTO.getStatus() == null) {
            scheduledWorkoutDTO.setStatus(WorkoutStatus.SCHEDULED);
        }
        
        ScheduledWorkout scheduledWorkout = new ScheduledWorkout();
        
        // Set schedule relationship
        if (scheduledWorkoutDTO.getScheduleId() != null) {
            scheduledWorkout.setSchedule(scheduleRepository.findById(scheduledWorkoutDTO.getScheduleId()).get());
        }
        
        // Set workout relationship
        scheduledWorkout.setWorkout(workoutRepository.findById(scheduledWorkoutDTO.getWorkoutId()).get());
        
        // Set other fields
        scheduledWorkout.setScheduledDateTime(scheduledWorkoutDTO.getScheduledDateTime());
        scheduledWorkout.setStatus(scheduledWorkoutDTO.getStatus());
        scheduledWorkout.setNotes(scheduledWorkoutDTO.getNotes());
        
        ScheduledWorkout savedScheduledWorkout = scheduledWorkoutRepository.save(scheduledWorkout);
        return scheduledWorkoutMapper.toDTO(savedScheduledWorkout);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ScheduledWorkoutDTO getScheduledWorkoutById(Long id) {
        ScheduledWorkout scheduledWorkout = scheduledWorkoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheduled workout not found with id: " + id));
        return scheduledWorkoutMapper.toDTO(scheduledWorkout);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ScheduledWorkoutDTO> getScheduledWorkoutsByScheduleId(Long scheduleId) {
        List<ScheduledWorkout> scheduledWorkouts = scheduledWorkoutRepository.findByScheduleId(scheduleId);
        return scheduledWorkoutMapper.toDTOList(scheduledWorkouts);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ScheduledWorkoutDTO> getUserWorkoutsInDateRange(String userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<ScheduledWorkout> scheduledWorkouts = scheduledWorkoutRepository.findUserWorkoutsInDateRange(
                userId, startDateTime, endDateTime);
        return scheduledWorkoutMapper.toDTOList(scheduledWorkouts);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ScheduledWorkoutDTO> getUserWorkoutsInDateRangeWithStatus(String userId, WorkoutStatus status,
                                                          LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<ScheduledWorkout> scheduledWorkouts = scheduledWorkoutRepository.findUserWorkoutsInDateRangeWithStatus(
                userId, status, startDateTime, endDateTime);
        return scheduledWorkoutMapper.toDTOList(scheduledWorkouts);
    }
    
    @Override
    @Transactional
    public ScheduledWorkoutDTO updateScheduledWorkout(Long id, ScheduledWorkoutDTO scheduledWorkoutDTO) {
        ScheduledWorkout scheduledWorkout = scheduledWorkoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheduled workout not found with id: " + id));
        
        if (scheduledWorkoutDTO.getWorkoutId() != null && 
            !scheduledWorkoutDTO.getWorkoutId().equals(scheduledWorkout.getWorkout().getId())) {
            scheduledWorkout.setWorkout(workoutRepository.findById(scheduledWorkoutDTO.getWorkoutId())
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found with id: " + scheduledWorkoutDTO.getWorkoutId())));
        }
        
        scheduledWorkout.setScheduledDateTime(scheduledWorkoutDTO.getScheduledDateTime());
        scheduledWorkout.setStatus(scheduledWorkoutDTO.getStatus());
        scheduledWorkout.setNotes(scheduledWorkoutDTO.getNotes());
        
        ScheduledWorkout updatedScheduledWorkout = scheduledWorkoutRepository.save(scheduledWorkout);
        return scheduledWorkoutMapper.toDTO(updatedScheduledWorkout);
    }
    
    @Override
    @Transactional
    public ScheduledWorkoutDTO updateWorkoutStatus(Long id, WorkoutStatus status) {
        ScheduledWorkout scheduledWorkout = scheduledWorkoutRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheduled workout not found with id: " + id));
        
        scheduledWorkout.setStatus(status);
        ScheduledWorkout updatedScheduledWorkout = scheduledWorkoutRepository.save(scheduledWorkout);
        return scheduledWorkoutMapper.toDTO(updatedScheduledWorkout);
    }
    
    @Override
    @Transactional
    public void deleteScheduledWorkout(Long id) {
        if (!scheduledWorkoutRepository.existsById(id)) {
            throw new ResourceNotFoundException("Scheduled workout not found with id: " + id);
        }
        scheduledWorkoutRepository.deleteById(id);
    }
} 