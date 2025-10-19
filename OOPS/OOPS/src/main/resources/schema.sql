CREATE TABLE IF NOT EXISTS menus (
        id SERIAL PRIMARY KEY,
        name TEXT NOT NULL,
        currency VARCHAR(3) NOT NULL DEFAULT 'ISK'
    );

CREATE TABLE IF NOT EXISTS menu_sections (
        id SERIAL PRIMARY KEY,
        menu_id INT NOT NULL REFERENCES menus(id) ON DELETE CASCADE,
        name TEXT NOT NULL,
        display_order INT NOT NULL DEFAULT 0
    );

CREATE TABLE IF NOT EXISTS items (
    id SERIAL PRIMARY KEY,
    section_id INT NOT NULL REFERENCES menu_sections(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    description TEXT NOT NULL DEFAULT '',
    price_isk INT NOT NULL CHECK (price_isk >= 0),
    available BOOLEAN NOT NULL DEFAULT TRUE,
    tags TEXT,
    image_data BYTEA
);

CREATE TABLE IF NOT EXISTS baskets (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS basket_items (
    id SERIAL PRIMARY KEY,
    basket_id UUID NOT NULL REFERENCES baskets(id) ON DELETE CASCADE,
    item_id INT NOT NULL REFERENCES items(id) ON DELETE RESTRICT,
    quantity INT NOT NULL CHECK (quantity >= 0)
    );

CREATE TABLE IF NOT EXISTS store_settings (
  id           SMALLINT PRIMARY KEY DEFAULT 1,
  queue_minutes INT NOT NULL DEFAULT 20,
  updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_by    TEXT
);

CREATE TABLE IF NOT EXISTS queue_time_changes (
  id           BIGSERIAL PRIMARY KEY,
  old_minutes  INT NOT NULL,
  new_minutes  INT NOT NULL,
  changed_by   TEXT,
  changed_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS orders (
  id SERIAL PRIMARY KEY,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  basket_id UUID,
  customer_phone TEXT,
  status TEXT NOT NULL DEFAULT 'RECEIVED',
  total_isk INT NOT NULL,
  estimated_ready_at TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS order_items (
  id SERIAL PRIMARY KEY,
  order_id INT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
  item_id INT NOT NULL REFERENCES items(id) ON DELETE RESTRICT,
  item_name TEXT NOT NULL,
  price_isk INT NOT NULL,
  quantity INT NOT NULL CHECK (quantity > 0)
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

INSERT INTO users (username, password, role)
VALUES
('admin', '$2a$10$9GCRJGQbdQqRdNrGvEP9ku5WgVtM/ACdTdWWzn.OqaEqRWqLsEySC', 'SUPERUSER'),
('staff', '$2a$10$iL2gg5M.eFquLz5o1kUK/eoHlSSVhyT6VZH7169375crNMoLD8fYq', 'STAFF')
ON CONFLICT (username) DO UPDATE
SET password = EXCLUDED.password,
    role = EXCLUDED.role;


CREATE TABLE IF NOT EXISTS opening_hours (
    id SERIAL PRIMARY KEY,
    weekday VARCHAR(20),
    open_time VARCHAR(10),
    close_time VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS opening_exceptions (
    id SERIAL PRIMARY KEY,
    date DATE,
    open_time VARCHAR(10),
    close_time VARCHAR(10),
    closed BOOLEAN
);
ALTER TABLE orders
    ADD CONSTRAINT orders_status_check
        CHECK (status IN ('RECEIVED','PREPARING','READY','PICKED_UP'));
