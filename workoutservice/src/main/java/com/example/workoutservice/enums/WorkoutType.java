package com.example.workoutservice.enums;

public enum WorkoutType {
    CARDIO(7.0),
    STRENGTH(4.5),
    FLEXIBILITY(2.5),
    HIIT(9.0),
    YOGA(2.8),
    PILATES(3.5),
    CROSSFIT(9.0);

    private final Double metValue;

    WorkoutType(Double metValue) {
        this.metValue = metValue;
    }

    public Double getMetValue() {
        return metValue;
    }
} 