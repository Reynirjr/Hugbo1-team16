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
    tags TEXT
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
