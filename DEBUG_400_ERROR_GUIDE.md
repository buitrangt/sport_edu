# HƯỚNG DẪN FIX LỖI 400 TOURNAMENT API

## VẤN ĐỀ HIỆN TẠI
Frontend gọi `/api/tournaments/with-image` và nhận lỗi 400 Bad Request

## NGUYÊN NHÂN CÓ THỂ
1. ❌ JSON parsing lỗi (datetime format)
2. ❌ Validation lỗi (thiếu required fields)
3. ❌ Authentication/Authorization lỗi
4. ❌ Request format không đúng

## CÁC FIX ĐÃ THỰC HIỆN

### 1. BACKEND IMPROVEMENTS ✅
- Thêm logging chi tiết trong controller
- Thêm validation rõ ràng cho từng field
- Cải thiện error handling và messages
- Fix ObjectMapper configuration cho datetime parsing
- Thêm debug class để test JSON parsing

### 2. ENDPOINT ANALYSIS ✅
```java
@PostMapping("/with-image")
@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ORGANIZER')")
public ResponseEntity<ApiResponse<TournamentCreateResponseDTO>> createTournamentWithImage(
    @RequestParam("tournament") String tournamentJson,
    @RequestParam(value = "image", required = false) MultipartFile imageFile)
```

## CÁCH DEBUG TỪNG BƯỚC

### Step 1: Build và chạy backend
```bash
cd C:\Users\ACER\Desktop\be
.\test_tournament_fix.bat
```

### Step 2: Kiểm tra backend logs
Khi frontend gọi API, backend sẽ in ra:
```
🏆 [TournamentController] Received tournament creation request with image
📋 Tournament JSON: {...}
📷 Image file: filename.jpg (12345 bytes)
✅ Successfully parsed tournament request: Tournament Name
🚀 Creating tournament...
✅ Tournament created successfully with ID: 123
```

### Step 3: Nếu có lỗi JSON parsing
```
❌ JSON parsing error: Cannot deserialize value of type `java.time.LocalDateTime`...
```
→ Fix: datetime format issues

### Step 4: Nếu có lỗi validation
```
❌ Tournament creation failed: Tournament name is required
```
→ Fix: frontend data issues

### Step 5: Nếu có lỗi authorization
```
403 Forbidden
```
→ Fix: user không có role ADMIN hoặc ORGANIZER

## TEST CASES

### Test Case 1: No Image
```javascript
createTournamentWithImage(tournamentData, null)
// Should use /api/tournaments endpoint
```

### Test Case 2: With Image
```javascript
createTournamentWithImage(tournamentData, imageFile)
// Should use /api/tournaments/with-image endpoint
```

### Test Case 3: Manual curl test
```bash
curl -X POST http://localhost:8080/api/tournaments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"name":"Test","description":"Test desc","location":"Test loc","contactInfo":"test@test.com","maxTeams":16,"startDate":"2025-06-02T10:00:00Z","endDate":"2025-06-03T18:00:00Z","registrationDeadline":"2025-06-01T23:59:00Z","sportType":"FOOTBALL"}'
```

## NẾU VẪN LỖI 400

### Kiểm tra Network tab (F12)
1. Request URL: `/api/tournaments/with-image`
2. Request Method: `POST`
3. Request Headers: 
   - Authorization: Bearer token
   - Content-Type: multipart/form-data
4. Request Body:
   - tournament: JSON string
   - image: File (optional)

### Kiểm tra backend console
- Có log "🏆 [TournamentController] Received tournament creation request" không?
- JSON parsing thành công không?
- Validation pass không?

### Common Issues:
1. **Không có token hoặc token expired**
   - Fix: Re-login
2. **User không có đúng role**  
   - Fix: Admin cấp role ORGANIZER
3. **DateTime format sai**
   - Fix: Đảm bảo frontend gửi ISO string
4. **Required fields thiếu**
   - Fix: Kiểm tra name, description, location, contactInfo

## NEXT STEPS
1. Chạy backend với logging
2. Test từ frontend  
3. Xem logs để identify exact error
4. Fix theo hướng dẫn trên
