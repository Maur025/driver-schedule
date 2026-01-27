INSERT INTO label_types(name, code, contact_category_id)
VALUES ('Móvil', 'MOBILE', (SELECT id FROM contact_categories WHERE code = 'PHONE')),
       ('Trabajo', 'WORK', (SELECT id FROM contact_categories WHERE code = 'PHONE')),
       ('Hogar', 'Home', (SELECT id FROM contact_categories WHERE code = 'PHONE')),
       ('Principal', 'MAIN', (SELECT id FROM contact_categories WHERE code = 'PHONE')),
       ('Otro', 'OTHER', (SELECT id FROM contact_categories WHERE code = 'PHONE'));