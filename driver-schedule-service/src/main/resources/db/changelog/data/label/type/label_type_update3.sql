DELETE
FROM contacts
WHERE label_type_id IN ((SELECT id FROM label_types WHERE code = 'MOBILE'),
                        (SELECT id FROM label_types WHERE code = 'HOME'));


DELETE
FROM label_types
WHERE code IN ('MOBILE', 'HOME');