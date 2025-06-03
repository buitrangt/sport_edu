# HƯỚNG DẪN FIX LỖI TOURNAMENT API

## VẤN ĐỀ
Frontend đang gọi `/api/tournaments/with-image` nhưng backend không có endpoint này.
Lỗi: `Request method 'POST' is not supported`

## NGUYÊN NHÂN
TournamentController chỉ có:
- `/api/tournaments` POST (tạo tournament thường)
- `/api/tournaments/{id}` PUT (update)
- `/api/tournaments/{id}/start` POST (start tournament)

Nhưng KHÔNG có `/api/tournaments/with-image` POST

## GIẢI PHÁP ĐÃ THỰC HIỆN

### 1. BACKEND (ĐÃ FIX)
✅ Thêm endpoint `/api/tournaments/with-image` vào TournamentController
✅ Thêm import cho MultipartFile và ObjectMapper
✅ Method mới xử lý upload ảnh và tạo tournament

### 2. FRONTEND (ĐÃ FIX)
✅ Cải thiện logic trong `createTournamentWithImage`:
  - Nếu có ảnh: dùng `/api/tournaments/with-image`
  - Nếu không có ảnh: dùng `/api/tournaments` (endpoint cũ)

## CÁCH CHẠY LẠI ĐỂ TEST

### Backend:
1. Vào thư mục be: `cd C:\Users\ACER\Desktop\be`
2. Chạy: `.\quick_fix_tournament_api.bat`
   HOẶC:
   ```bash
   gradlew clean build -x test
   gradlew bootRun
   ```

### Frontend:
1. Vào thư mục fe: `cd C:\Users\ACER\Desktop\fe\fe-sport`
2. Chạy: `npm start`

## TEST CASES ĐỂ KIỂM TRA

### Case 1: Tạo tournament KHÔNG có ảnh
- Frontend sẽ gọi `/api/tournaments` (endpoint cũ)
- Nên hoạt động bình thường

### Case 2: Tạo tournament CÓ ảnh  
- Frontend sẽ gọi `/api/tournaments/with-image` (endpoint mới)
- Backend sẽ parse JSON và tạo tournament
- (Ảnh hiện tại chưa được lưu, chỉ parse thành công)

### Case 3: Kiểm tra lỗi authorization
- Đảm bảo user có role ADMIN hoặc ORGANIZER
- Nếu không có role: sẽ trả về 403 Forbidden

## LOGS ĐỂ KIỂM TRA

### Console frontend (F12):
```
🏟️ [TournamentServiceFixed] Creating tournament with image: ...
📤 Sending formData with: ...
✅ [TournamentServiceFixed] Create tournament with image success: ...
```

### Console backend:
```
Hibernate: select count(t1_0.id) from teams t1_0 where t1_0.tournament_id=?
(Không còn lỗi HttpRequestMethodNotSupportedException)
```

## NẾU VẪN LỖI

1. Kiểm tra endpoint đang được gọi trong Network tab (F12)
2. Kiểm tra request headers và body
3. Kiểm tra authentication token
4. Kiểm tra server có restart thành công không

## FILE ĐÃ SỬA
- `C:\Users\ACER\Desktop\be\src\main\java\com\example\checkscam\rest\TournamentController.java`
- `C:\Users\ACER\Desktop\fe\fe-sport\src\services\tournamentServiceFixed.js`
