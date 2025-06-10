# Panduan Penggunaan Postman Collection

## 1. Setup Awal

### Import Collection

1. Download file `sdg-api.postman_collection.json`
2. Buka Postman
3. Klik "Import" (pojok kiri atas)
4. Drag & drop file collection atau klik "Upload Files"
5. Klik "Import"

### Setup Environment

1. Klik icon ⚙️ (Manage Environments)
2. Klik "Add" untuk buat environment baru
3. Isi informasi:
   ```
   Environment name: SDG API Local
   Variable:
   - token: [kosongkan]
   ```
4. Klik "Save"
5. Di pojok kanan atas, pilih environment "SDG API Local"

## 2. Alur Testing yang Benar

### Register Admin Pertama

1. Buka folder "1. Public APIs"
2. Pilih request "Register User"
3. Body sudah terisi contoh, sesuaikan data:
   ```json
   {
     "name": "Super Admin",
     "phone": "081234567890",
     "username": "admin",
     "password": "admin123"
   }
   ```
4. Send request
5. Pastikan response success dan role "ROLE_ADMIN"

### Login sebagai Admin

1. Pilih request "Login"
2. Isi body dengan credentials admin:
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```
3. Send request
4. Token akan otomatis tersimpan (ada di Postman console)
5. Cek variable token di environment sudah terisi

### Test Endpoint Admin

1. Buka folder "2. Admin APIs"
2. Test endpoint secara berurutan:
   - Get All Users
   - Create Admin
   - Update Admin
   - dll
3. Jika error 403:
   - Cek token di environment
   - Login ulang sebagai admin
   - Pastikan environment aktif

### Test User Flow

1. Register user baru
2. Login sebagai user tersebut
3. Test endpoint di folder "3. User APIs"
4. Pastikan dapat error 403 saat akses endpoint admin

## 3. Postman Collection Features

### Auto-save Token

- Script di request "Login" akan otomatis simpan token
- Tidak perlu copy-paste token manual
- Token digunakan di semua request

### Environment Variables

- {{token}}: JWT token untuk autentikasi
- Collection menggunakan bearer token automatik
- Token bisa diganti manual di environment

### Request Examples

Setiap request sudah include:

- Method yang benar
- URL lengkap
- Headers yang diperlukan
- Body contoh (untuk POST/PUT)
- Deskripsi endpoint

### Response Examples

Di documentation tab setiap request ada:

- Success response
- Error response
- Status codes
- Response headers

## 4. Troubleshooting

### Error 401 Unauthorized

- Token expired/invalid
- Login ulang untuk dapat token baru
- Cek environment active

### Error 403 Forbidden

- Role tidak sesuai
- Cek login sebagai user/admin
- Pastikan endpoint sesuai role

### Error 400 Bad Request

- Cek format JSON
- Validasi semua field required
- Lihat error message detail

### Error 500 Server

- Screenshot full error
- Catat request yang digunakan
- Hubungi backend developer

## 5. Tips

### Development

- Gunakan environment terpisah untuk development
- Test semua error cases
- Validasi format response

### Production

- Ganti base URL ke production
- Jangan simpan password di collection
- Backup environment settings

### Security

- Jangan share token
- Update password berkala
- Clear environment setelah testing

## 6. Integrasi dengan Frontend

### Axios Example

```javascript
const config = {
  headers: {
    Authorization: `Bearer ${token}`,
    "Content-Type": "application/json",
  },
};

// GET request
axios
  .get("http://localhost:8091/api/user/profile", config)
  .then((response) => console.log(response.data));

// POST request
axios
  .post(
    "http://localhost:8091/api/auth/login",
    {
      username: "admin",
      password: "admin123",
    },
    config
  )
  .then((response) => {
    const token = response.data.token;
    // Save token
  });
```

### Fetch Example

```javascript
const headers = {
  Authorization: `Bearer ${token}`,
  "Content-Type": "application/json",
};

// GET request
fetch("http://localhost:8091/api/user/profile", { headers })
  .then((res) => res.json())
  .then((data) => console.log(data));

// POST request
fetch("http://localhost:8091/api/auth/login", {
  method: "POST",
  headers,
  body: JSON.stringify({
    username: "admin",
    password: "admin123",
  }),
})
  .then((res) => res.json())
  .then((data) => {
    const token = data.token;
    // Save token
  });
```
