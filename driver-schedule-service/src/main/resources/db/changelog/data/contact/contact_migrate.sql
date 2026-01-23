INSERT INTO contacts(label, value, contact_category_id, person_id)
SELECT 'personal', p.phone, cc.id, p.id
FROM person p
         JOIN contact_categories cc ON cc.code = 'PHONE'
WHERE p.phone IS NOT NULL
  AND p.phone <> '';
