UPDATE vehicles
SET vehicle_type_id = (SELECT id FROM vehicle_types WHERE code = 'PICKUP')
WHERE vehicle_type_id IS NULL;