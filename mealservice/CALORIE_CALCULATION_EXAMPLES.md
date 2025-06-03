# Calorie Calculation Examples

## Chức năng tính toán Calories đã được cập nhật

### Thay đổi chính:
1. **Food Calories phụ thuộc vào Meal Calories**: Meal calories sẽ tự động được tính từ tổng calories của foods
2. **CalorieStatsDto đơn giản**: Chỉ tập trung vào userId, date, totalCalories
3. **API endpoint mới**: Thêm endpoint query calories trong khoảng tùy chỉnh

---

## 🔄 Auto-calculation của Meal Calories

### Tạo Meal với Foods - Calories tự động tính
**POST** `/api/meals`

```json
{
  "name": "Healthy Breakfast Bowl",
  "description": "Nutritious breakfast with automatic calorie calculation",
  "type": "BREAKFAST",
  "foods": [
    {
      "name": "Rolled Oats",
      "description": "Organic whole grain oats",
      "calories": 150
    },
    {
      "name": "Fresh Blueberries", 
      "description": "Antioxidant-rich berries",
      "calories": 85
    },
    {
      "name": "Almonds",
      "description": "Raw unsalted almonds", 
      "calories": 165
    }
  ]
}
```

**Response**: Meal calories sẽ tự động = 150 + 85 + 165 = 400
```json
{
  "id": 1,
  "name": "Healthy Breakfast Bowl",
  "description": "Nutritious breakfast with automatic calorie calculation",
  "calories": 400,
  "type": "BREAKFAST",
  "foods": [...]
}
```

---

## 📊 Calorie Statistics APIs

### 1. Daily Calories
**GET** `/api/calories/daily/{userId}?date=2024-01-15`

**Response**:
```json
{
  "userId": 1001,
  "date": "2024-01-15", 
  "totalCalories": 1850
}
```

### 2. Weekly Calories  
**GET** `/api/calories/weekly/{userId}?startDate=2024-01-15`

**Response**:
```json
[
  {
    "userId": 1001,
    "date": "2024-01-15",
    "totalCalories": 1850
  },
  {
    "userId": 1001, 
    "date": "2024-01-16",
    "totalCalories": 1920
  },
  // ... 5 more days
]
```

### 3. Monthly Calories
**GET** `/api/calories/monthly/{userId}?year=2024&month=1`

**Response**: Array of daily calorie stats for January 2024

### 4. 🆕 Custom Range Calories
**GET** `/api/calories/range/{userId}?startDate=2024-01-01&endDate=2024-01-31`

**Response**: Array of daily calorie stats for the specified range

---

## 🔧 Testing Calorie Synchronization

### Test Case 1: Create meal without specifying calories
```json
POST /api/meals
{
  "name": "Auto-calculated Meal",
  "description": "Calories will be auto-calculated from foods",
  "type": "LUNCH",
  "foods": [
    {"name": "Chicken Breast", "calories": 250},
    {"name": "Rice", "calories": 200},
    {"name": "Vegetables", "calories": 50}
  ]
}
```
**Result**: Meal calories = 500 (auto-calculated)

### Test Case 2: Update food calories
```json
PUT /api/meals/{mealId}
{
  "name": "Updated Meal",
  "type": "LUNCH", 
  "foods": [
    {"id": 1, "name": "Chicken Breast", "calories": 300}, // Increased
    {"id": 2, "name": "Rice", "calories": 200},
    {"id": 3, "name": "Vegetables", "calories": 50}
  ]
}
```
**Result**: Meal calories = 550 (auto-updated)

### Test Case 3: Add new food to existing meal
```json
PUT /api/meals/{mealId}
{
  "name": "Meal with Extra Food",
  "type": "LUNCH",
  "foods": [
    {"id": 1, "name": "Chicken Breast", "calories": 250},
    {"id": 2, "name": "Rice", "calories": 200},
    {"id": 3, "name": "Vegetables", "calories": 50},
    {"name": "Sauce", "calories": 100} // New food
  ]
}
```
**Result**: Meal calories = 600 (auto-updated)

---

## 📈 Usage Flow Example

1. **Create meals with foods**:
   ```bash
   POST /api/meals
   # Calories auto-calculated from foods
   ```

2. **Create schedule for user**:
   ```bash
   POST /api/meals/schedules
   ```

3. **Schedule meals for specific times**:
   ```bash  
   POST /api/meals/scheduled-meals
   ```

4. **Mark meals as completed**:
   ```bash
   PATCH /api/meals/scheduled-meals/{id}/status/COMPLETED
   ```

5. **Query calorie statistics**:
   ```bash
   # Daily
   GET /api/calories/daily/1001?date=2024-01-15
   
   # Weekly  
   GET /api/calories/weekly/1001?startDate=2024-01-15
   
   # Monthly
   GET /api/calories/monthly/1001?year=2024&month=1
   
   # Custom range
   GET /api/calories/range/1001?startDate=2024-01-01&endDate=2024-01-31
   ```

---

## ✅ Benefits of New Implementation

1. **Data Consistency**: Meal calories luôn chính xác với tổng food calories
2. **Simplified Response**: CalorieStatsDto chỉ trả về thông tin cần thiết
3. **Flexible Querying**: API endpoint cho custom date range
4. **Automatic Sync**: Không cần manual calculation của calories
5. **Validation**: Đảm bảo data integrity ở database level 