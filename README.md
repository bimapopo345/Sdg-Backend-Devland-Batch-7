# SDG Backend API Documentation

[Previous content remains the same until "Testing dengan Postman" section]

## Testing dengan Postman

1. Setup Environment:

   - Buka Postman
   - Klik icon ⚙️ (Manage Environments)
   - Klik "Add"
   - Nama: "SDG API Local"
   - Tambah variable:
     ```
     token: [kosongkan dulu]
     ```
   - Save dan pilih environment "SDG API Local"

2. Import Collection:

   - File > Import
   - Pilih file: sdg-api.postman_collection.json
   - Semua request sudah diset dengan Authorization type Bearer Token
   - Token akan otomatis diambil dari environment variable

3. Urutan Testing yang Benar:
   a. Register Admin Pertama:

   - Gunakan request "Register User"
   - User pertama otomatis jadi admin
   - Simpan username dan password

   b. Login sebagai Admin:

   - Gunakan request "Login"
   - Token akan otomatis tersimpan
   - Cek di Postman console: "Token JWT berhasil disimpan"

   c. Test Endpoint Admin:

   - Semua request di folder "2. Admin APIs"
   - Pastikan dapat response success
   - Jika forbidden, cek token admin

   d. Register User Biasa:

   - Register user baru
   - Login sebagai user tersebut
   - Test endpoint di folder "3. User APIs"

4. Troubleshooting:
   - Error 403 Forbidden: Cek token dan role
   - Error 401 Unauthorized: Login ulang
   - Error 400 Bad Request: Cek format JSON
   - Error 500 Server: Hubungi backend developer

[Previous "Security Notes" section remains the same]

## Catatan Tambahan

1. Database:

   - PostgreSQL (Supabase)
   - Auto create table saat pertama run
   - Backup secara berkala

2. Environment Variables:

   - PORT: 8091
   - DB_URL: Supabase connection string
   - JWT_SECRET: Rahasia untuk generate token

3. Maintenance:
   - Log rotation setiap hari
   - Token expire dalam 24 jam
   - Password di-hash dengan BCrypt
