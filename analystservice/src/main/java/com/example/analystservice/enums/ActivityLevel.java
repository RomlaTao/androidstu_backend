package com.example.analystservice.enums;

public enum ActivityLevel {
    SEDENTARY(1.2, "Sedentary", "Little or no exercise"),
    LIGHTLY_ACTIVE(1.375, "Lightly Active", "Light exercise/sports 1-3 days/week"),
    MODERATELY_ACTIVE(1.55, "Moderately Active", "Moderate exercise/sports 3-5 days/week"),
    VERY_ACTIVE(1.725, "Very Active", "Hard exercise/sports 6-7 days a week"),
    EXTRA_ACTIVE(1.9, "Extra Active", "Very hard exercise/sports & physical job or 2x training");

    private final double factor;
    private final String displayName;
    private final String description;

    ActivityLevel(double factor, String displayName, String description) {
        this.factor = factor;
        this.displayName = displayName;
        this.description = description;
    }

    public double getFactor() { return factor; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
}