package com.example.analystservice.enums;

public enum BmiCategory {
    SEVERELY_UNDERWEIGHT(0, 16, "Severely Underweight", "Thiếu cân nghiêm trọng"),
    UNDERWEIGHT(16, 18.5, "Underweight", "Thiếu cân"),
    NORMAL_WEIGHT(18.5, 25, "Normal Weight", "Bình thường"),
    OVERWEIGHT(25, 30, "Overweight", "Thừa cân"),
    OBESITY_CLASS_I(30, 35, "Obesity Class I", "Béo phì độ I"),
    OBESITY_CLASS_II(35, 40, "Obesity Class II", "Béo phì độ II"),
    OBESITY_CLASS_III(40, Double.MAX_VALUE, "Obesity Class III", "Béo phì độ III");

    private final double minBmi;
    private final double maxBmi;
    private final String displayName;
    private final String vietnameseName;

    BmiCategory(double minBmi, double maxBmi, String displayName, String vietnameseName) {
        this.minBmi = minBmi;
        this.maxBmi = maxBmi;
        this.displayName = displayName;
        this.vietnameseName = vietnameseName;
    }

    public double getMinBmi() { return minBmi; }
    public double getMaxBmi() { return maxBmi; }
    public String getDisplayName() { return displayName; }
    public String getVietnameseName() { return vietnameseName; }

    public static BmiCategory fromBmi(double bmi) {
        for (BmiCategory category : values()) {
            if (bmi >= category.minBmi && bmi < category.maxBmi) {
                return category;
            }
        }
        return OBESITY_CLASS_III; // Default for very high BMI
    }
}