# API Migration Guide - Calorie Calculation Update

## Overview
This guide documents the migration from the old nutritional tracking system (carb, protein, lipid) to the new simplified calorie-focused system with automatic calculation.

## Breaking Changes

### 1. Removed Fields
The following fields have been **removed** from all entities and DTOs:
- `carb` (carbohydrates)
- `protein`
- `lipid` (fats)

### 2. Updated Fields
- **Meal.calories**: Now auto-calculated from sum of food calories
- **Food.calories**: Remains manual input but triggers meal recalculation

### 3. New Endpoints
Added new calorie statistics endpoints:
```
GET /calories/daily/{userId}?date={date}
GET /calories/weekly/{userId}?startDate={date}  
GET /calories/monthly/{userId}?year={year}&month={month}
GET /calories/range/{userId}?startDate={date}&endDate={date}
```

## API Changes

### Before (Old API)
```json
POST /meals
{
  "name": "Grilled Chicken Salad",
  "description": "Healthy meal",
  "carb": 15,
  "protein": 35, 
  "lipid": 8,
  "type": "LUNCH",
  "foods": [
    {
      "name": "Chicken",
      "carb": 0,
      "protein": 25,
      "lipid": 3
    }
  ]
}
```

### After (New API)
```json
POST /meals
{
  "name": "Grilled Chicken Salad", 
  "description": "Healthy meal",
  "type": "LUNCH",
  "foods": [
    {
      "name": "Chicken",
      "calories": 150
    }
  ]
}
// Response: meal.calories = 150 (auto-calculated)
```

## Migration Steps

### 1. Update Client Code
Remove references to:
- `meal.carb`
- `meal.protein` 
- `meal.lipid`
- `food.carb`
- `food.protein`
- `food.lipid`

### 2. Update Data Models
```typescript
// Old Model
interface Meal {
  id: number;
  name: string;
  carb: number;
  protein: number;
  lipid: number;
  calories?: number; // Optional/calculated
}

// New Model  
interface Meal {
  id: number;
  name: string;
  calories: number; // Auto-calculated from foods
}
```

### 3. Update API Calls
```typescript
// Old API call
const meal = {
  name: "Breakfast",
  carb: 50,
  protein: 20,
  lipid: 10,
  foods: [...]
};

// New API call
const meal = {
  name: "Breakfast", 
  foods: [
    { name: "Oats", calories: 150 },
    { name: "Milk", calories: 100 }
  ]
  // calories will be auto-calculated as 250
};
```

### 4. Update Calorie Tracking
```typescript
// Old - Manual calculation
const totalCalories = (carb * 4) + (protein * 4) + (lipid * 9);

// New - Direct from API
const dailyStats = await fetch(`/calories/daily/${userId}?date=${date}`);
const totalCalories = dailyStats.totalCalories;
```

## New Features

### 1. Automatic Calorie Synchronization
- Meal calories = Sum of food calories
- Real-time updates when foods change
- Database-level consistency validation

### 2. Enhanced Statistics API
```typescript
// Get daily calories
GET /calories/daily/123?date=2024-01-15
Response: { userId: 123, date: "2024-01-15", totalCalories: 1850 }

// Get weekly calories  
GET /calories/weekly/123?startDate=2024-01-15
Response: [
  { userId: 123, date: "2024-01-15", totalCalories: 1850 },
  { userId: 123, date: "2024-01-16", totalCalories: 1920 },
  // ... 5 more days
]

// Custom date range
GET /calories/range/123?startDate=2024-01-01&endDate=2024-01-31
```

### 3. Simplified Data Model
- Focus on calories only
- Reduced complexity 
- Better performance
- Easier to understand

## Benefits

### 1. Data Consistency
- No more manual calorie calculations
- Automatic synchronization prevents errors
- Database-level validation

### 2. Simplified Development
- Single nutrition metric (calories)
- Less fields to manage
- Reduced complexity

### 3. Better User Experience
- Automatic calculations
- Real-time updates
- Accurate tracking

### 4. Performance Improvements
- Simplified queries
- Reduced data transfer
- Faster API responses

## Testing the Migration

### 1. Test Meal Creation
```bash
# Test auto-calculation
curl -X POST /meals \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Meal",
    "type": "LUNCH", 
    "foods": [
      {"name": "Food 1", "calories": 100},
      {"name": "Food 2", "calories": 150}
    ]
  }'

# Verify response has calories: 250
```

### 2. Test Calorie Statistics
```bash
# Test daily stats
curl "/calories/daily/123?date=2024-01-15"

# Test weekly stats  
curl "/calories/weekly/123?startDate=2024-01-15"

# Test custom range
curl "/calories/range/123?startDate=2024-01-01&endDate=2024-01-31"
```

### 3. Test Food Updates
```bash
# Update food calories and verify meal auto-updates
curl -X PUT /meals/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Meal",
    "type": "LUNCH",
    "foods": [
      {"id": 1, "name": "Food 1", "calories": 200}, // Increased
      {"id": 2, "name": "Food 2", "calories": 150}
    ]
  }'

# Verify meal.calories = 350 (auto-updated)
```

## Troubleshooting

### Common Issues

1. **Calories not updating**: Check that foods have valid calorie values
2. **Validation errors**: Ensure calories >= 0
3. **Missing statistics**: Verify meals are marked as COMPLETED status

### Support
For migration support, check:
- `CALORIE_CALCULATION_EXAMPLES.md` - Usage examples
- `README.md` - Complete API documentation
- API Gateway logs for request/response details 