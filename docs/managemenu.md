# API Overview

### 🍔 OOPS Menu API

These API calls manage **menus** for OOPS.

---

## Base URL
http://localhost:8080/api


---

## 📖 Endpoints

### 🔹 Menus

| Method | Endpoint              | Description                                  | Request Body | Response       |
|--------|-----------------------|----------------------------------------------|--------------|----------------|
| GET    | `/menus`              | Get all menus                                | –            | `[Menu]`       |
| GET    | `/menus/{menuId}`     | Get a menu by ID (with sections and items)   | –            | `Menu`         |
| DELETE | `/menus/{menuId}`     | Delete a menu (cascade: deletes sections + items) | –            | `204 No Content` |

---

### 🔹 Items

Items belong to a **menu section** inside a menu.

| Method | Endpoint                             | Description                  | Request Body         | Response   |
|--------|--------------------------------------|------------------------------|----------------------|------------|
| GET    | `/menus/{menuId}/items`              | Get all items for a menu     | –                    | `[Item]`   |
| GET    | `/menus/{menuId}/items/{itemId}`     | Get one item from a menu     | –                    | `Item`     |
| POST   | `/menus/{menuId}/items`              | Create new item in a menu    | `CreateItemRequest`  | `Item`     |
| PUT    | `/menus/{menuId}/items/{itemId}`     | Update an item               | `CreateItemRequest`  | `Item`     |
| DELETE | `/menus/{menuId}/items/{itemId}`     | Delete an item from a menu   | –                    | `204 No Content` |
| POST   | `/menus/{menuId}/items/{itemId}/image` | Upload an image for an item | `multipart/form-data` (field: `file`) | `Item` (with `imageUrl`) |

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
  "description": "Black bean patty",
  "priceIsk": 1790,
  "available": true,
  "tags": "[\"veg\"]",
  "imageUrl": "/images/veggie-burger.jpg"
}
```

### CreateItemRequest
```json
{
  "name": "Vegan Burger",
  "description": "Plant-based patty with lettuce and tomato",
  "priceIsk": 2200,
  "available": true,
  "tags": "[\"vegan\", \"burger\"]",
  "sectionId": 1,
  "imageUrl": "/images/vegan-burger.jpg"
}
```