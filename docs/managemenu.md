# API Overview

### 🍔 OOPS Menu API

These Api calls manage **menus** for OOPS.

---

## Base URL
http://localhost:8080/api



---

## 🔐 Authentication

### Public (no login needed)
- `GET /menus`
- `GET /menus/{menuId}`
- `GET /menus/{menuId}/items`
- `GET /menus/{menuId}/items/{itemId}`

### Requires login (`admin` or `staff`)
- `POST /menus/{menuId}/items`
- `PUT /menus/{menuId}/items/{itemId}`
- `DELETE /menus/{menuId}/items/{itemId}`
- `DELETE /menus/{menuId}`
- `POST /menus/{menuId}/items/{itemId}/image`

**Use Basic Auth:**
Username: admin &
Password: admin123


---

## 📖 Endpoints

### 🔹 Menus

| Method | Endpoint              | Description                                        | Auth Required | Response       |
|--------|-----------------------|----------------------------------------------------|----------------|----------------|
| GET    | `/menus`              | Get all menus (with sections & items)              | ❌ No          | `[Menu]`       |
| GET    | `/menus/{menuId}`     | Get a specific menu by ID                          | ❌ No          | `Menu`         |
| DELETE | `/menus/{menuId}`     | Delete a menu (also deletes its sections & items)  | ✅ Yes         | `204 No Content` |

---

### 🔹 Items

Items belong to a **menu section** inside a menu.

| Method | Endpoint                             | Description                        | Auth Required | Request Body | Response   |
|--------|--------------------------------------|------------------------------------|----------------|---------------|-------------|
| GET    | `/menus/{menuId}/items`              | Get all items for a menu           | ❌ No          | –             | `[Item]`   |
| GET    | `/menus/{menuId}/items/{itemId}`     | Get a specific item by ID          | ❌ No          | –             | `Item`     |
| POST   | `/menus/{menuId}/items`              | Create a new menu item             | ✅ Yes         | `CreateItemRequest` | `Item`     |
| PUT    | `/menus/{menuId}/items/{itemId}`     | Update an existing item            | ✅ Yes         | `CreateItemRequest` | `Item`     |
| DELETE | `/menus/{menuId}/items/{itemId}`     | Delete an item from a menu         | ✅ Yes         | –             | `204 No Content` |
| POST   | `/menus/{menuId}/items/{itemId}/image` | Upload or update an image for an item | ✅ Yes      | `multipart/form-data` (`file`) | `Item` (with image info) |

---

## 📦 Data Models

### Menu
```json
{
  "id": 1,
  "name": "Main Menu",
  "currency": "ISK",
  "sections": [ MenuSection ]
}

```
### MenuSection
```json
{
  "id": 1,
  "name": "Burgers",
  "displayOrder": 1,
  "items": [ Item ]
}
```
### Item
```json
{
  "id": 2,
  "name": "Veggie Burger",
  "description": "Black bean patty with avocado",
  "priceIsk": 1790,
  "available": true,
  "tags": "[\"veg\"]",
  "imageData": ""
}
```
## 🧾 Request Examples

### ➕ Create Item

**POST** `/api/menus/1/items`

```json
{
  "name": "Veisluplatti",
  "description": "Smáborgarar",
  "priceIsk": 4490,
  "available": true,
  "tags": "[\"meat\", \"cheesy\"]",
  "imageData": "",
  "sectionId": 1
}
```

### ✏️ Update Item

**PUT** `/api/menus/1/items/5`

```json
{
  "name": "Classic Cheeseburger",
  "description": "Juicy beef, cheese, and tomato",
  "priceIsk": 2190,
  "available": true,
  "tags": "[\"beef\", \"classic\"]",
  "imageData": "",
  "sectionId": 1
}
```
