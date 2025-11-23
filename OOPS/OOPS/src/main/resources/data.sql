-- ===========================================
-- USERS
-- admin / staff
-- admin123 / staff123
-- ===========================================
INSERT INTO users (username, password, role)
VALUES
('admin', '$2a$10$9GCRJGQbdQqRdNrGvEP9ku5WgVtM/ACdTdWWzn.OqaEqRWqLsEySC', 'SUPERUSER'),
('staff', '$2a$10$iL2gg5M.eFquLz5o1kUK/eoHlSSVhyT6VZH7169375crNMoLD8fYq', 'STAFF')
ON CONFLICT (username) DO UPDATE
SET password = EXCLUDED.password,
    role = EXCLUDED.role;

-- ===========================================
-- MENUS
-- ===========================================
INSERT INTO menus (id, name, currency)
VALUES (1, 'Main Menu', 'ISK')
ON CONFLICT (id) DO NOTHING;

-- ===========================================
-- MENU SECTIONS
-- ===========================================
INSERT INTO menu_sections (menu_id, name, display_order)
VALUES
(1, 'Burgers', 1),
(1, 'Drinks', 2)
ON CONFLICT DO NOTHING;

-- ===========================================
-- ITEMS
-- ===========================================
-- --- Burgers ---
INSERT INTO items (section_id, name, description, price_isk, available, tags)
VALUES
(
  (SELECT id FROM menu_sections WHERE name = 'Burgers' LIMIT 1),
  'Classic Hamburger',
  'Juicy grilled beef patty with lettuce, tomato, onion, and our special sauce.',
  1890,
  TRUE,
  'beef,grilled,classic'
),
(
  (SELECT id FROM menu_sections WHERE name = 'Burgers' LIMIT 1),
  'Cheeseburger',
  'Grilled beef burger topped with cheddar cheese, lettuce, tomato, and ketchup.',
  1990,
  TRUE,
  'beef,cheese,grilled'
),
(
  (SELECT id FROM menu_sections WHERE name = 'Burgers' LIMIT 1),
  'Vegan Burger',
  'Plant-based patty with lettuce, tomato, pickles, and vegan mayo.',
  2090,
  TRUE,
  'vegan,healthy,plant-based'
),
(
  (SELECT id FROM menu_sections WHERE name = 'Burgers' LIMIT 1),
  'BBQ Bacon Burger',
  'Beef patty with crispy bacon, BBQ sauce, and melted cheddar cheese.',
  2290,
  TRUE,
  'beef,bacon,bbq'
),
(
  (SELECT id FROM menu_sections WHERE name = 'Burgers' LIMIT 1),
  'Double Cheeseburger',
  'Two juicy beef patties stacked with double cheddar cheese and onions.',
  2590,
  TRUE,
  'beef,double,cheese'
);

-- --- Drinks ---
INSERT INTO items (section_id, name, description, price_isk, available, tags)
VALUES
(
  (SELECT id FROM menu_sections WHERE name = 'Drinks' LIMIT 1),
  'Coca-Cola',
  'Refreshing soft drink served chilled.',
  390,
  TRUE,
  'drink,soda'
),
(
  (SELECT id FROM menu_sections WHERE name = 'Drinks' LIMIT 1),
  'Sprite',
  'Lemon-lime soda served cold.',
  390,
  TRUE,
  'drink,soda'
),
(
  (SELECT id FROM menu_sections WHERE name = 'Drinks' LIMIT 1),
  'Fanta',
  'Orange-flavored soft drink.',
  390,
  TRUE,
  'drink,soda'
),
(
  (SELECT id FROM menu_sections WHERE name = 'Drinks' LIMIT 1),
  'Bottled Water',
  'Still Icelandic spring water.',
  250,
  TRUE,
  'drink,water'
),
(
  (SELECT id FROM menu_sections WHERE name = 'Drinks' LIMIT 1),
  'Sparkling Water',
  'Carbonated Icelandic mineral water.',
  290,
  TRUE,
  'drink,water,sparkling'
);

-- ===========================================
-- STORE SETTINGS
-- ===========================================
INSERT INTO store_settings (id, queue_minutes, updated_by)
VALUES (1, 20, 'admin')
ON CONFLICT (id) DO NOTHING;
