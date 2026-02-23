DELETE
FROM role_scopes
WHERE scope_id IN
      ((SELECT id FROM scopes WHERE name = 'read'), (SELECT id FROM scopes WHERE name = 'write'),
       (SELECT id FROM scopes WHERE name = 'read:request'),
       (SELECT id FROM scopes WHERE name = 'write:request'),
       (SELECT id FROM scopes WHERE name = 'read:vehicle'),
       (SELECT id FROM scopes WHERE name = 'write:vehicle'),
       (SELECT id FROM scopes WHERE name = 'read:schedule'),
       (SELECT id FROM scopes WHERE name = 'write:schedule'),
       (SELECT id FROM scopes WHERE name = 'read:person'),
       (SELECT id FROM scopes WHERE name = 'write:person'),
       (SELECT id FROM scopes WHERE name = 'read:trip'),
       (SELECT id FROM scopes WHERE name = 'write:trip'));

DELETE
FROM scopes
WHERE name IN ('read', 'write', 'read:request', 'write:request', 'read:vehicle', 'write:vehicle',
               'read:schedule', 'write:schedule', 'read:person', 'write:person', 'read:trip',
               'write:trip');

INSERT INTO scopes(name)
VALUES ('location:read'),
       ('location:create'),
       ('location:update'),
       ('person:read'),
       ('person:create'),
       ('person:update'),
       ('schedule:read'),
       ('schedule:create'),
       ('schedule:re'),
       ('schedule:cancel'),
       ('request:read'),
       ('request:create'),
       ('request:reject'),
       ('request:cancel'),
       ('vehicle:read'),
       ('vehicle:create'),
       ('vehicle:update'),
       ('trip:read');