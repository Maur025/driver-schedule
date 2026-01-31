UPDATE locations
SET place_category_id = (SELECT id FROM place_categories WHERE code = 'OFFICE')
WHERE place_category_id IS NULL;