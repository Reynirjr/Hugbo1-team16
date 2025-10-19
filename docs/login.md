# 🔐 Authentication API

## 📍 Base URL
#### http://localhost:8080/api/auth
---

## 🧾 Login Endpoint

### `POST /api/auth/login`

Authenticate a user and receive a **JWT token** for subsequent API calls.

---

### 📤 Request Body
```json
{
  "username": "admin",
  "password": "admin123"
}
```

```json
{
  "username": "staff",
  "password": "staff123"
}
```
### 📥 Example Successful Response (HTTP 200)

```json
{
  "token": "eyJhbG...",
  "username": "admin"
}
```
