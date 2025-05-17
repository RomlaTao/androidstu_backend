package com.example.workoutservice.services.impl;

import com.example.workoutservice.dtos.ScheduleDTO;
import com.example.workoutservice.exceptions.ResourceNotFoundException;
import com.example.workoutservice.mappers.ScheduleMapper;
import com.example.workoutservice.entities.Schedule;
import com.example.workoutservice.repositories.ScheduleRepository;
import com.example.workoutservice.services.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;
    
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository, ScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleMapper = scheduleMapper;
    }
    
    @Override
    @Transactional
    public ScheduleDTO createSchedule(ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleMapper.toEntity(scheduleDTO);
        Schedule savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDTO(savedSchedule);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ScheduleDTO getScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        return scheduleMapper.toDTO(schedule);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getSchedulesByUserId(Long userId) {
        List<Schedule> schedules = scheduleRepository.findByUserId(userId);
        return scheduleMapper.toDTOList(schedules);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getSchedulesInDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Schedule> schedules = scheduleRepository.findByUserIdAndEndDateGreaterThanEqualAndStartDateLessThanEqual(
                userId, startDate, endDate);
        return scheduleMapper.toDTOList(schedules);
    }
    
    @Override
    @Transactional
    public ScheduleDTO updateSchedule(Long id, ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + id));
        
        scheduleMapper.updateEntityFromDTO(scheduleDTO, schedule);
        Schedule updatedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDTO(updatedSchedule);
    }
    
    @Override
    @Transactional
    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Schedule not found with id: " + id);
        }
        scheduleRepository.deleteById(id);
    }
} 