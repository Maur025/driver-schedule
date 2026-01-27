UPDATE contacts
SET label_type_id = (SELECT id FROM label_types WHERE code = 'MOBILE')
WHERE label_type_id IS NULL;