/* ADMIN */
INSERT INTO role_scopes(role_id, scope_id)
VALUES ((SELECT id FROM roles WHERE name = 'ADMIN'),
        (SELECT id FROM scopes WHERE name = 'user:credential')),
       ((SELECT id FROM roles WHERE name = 'ADMIN'),
        (SELECT id FROM scopes WHERE name = 'user:reset-password')),
       ((SELECT id FROM roles WHERE name = 'ADMIN'),
        (SELECT id FROM scopes WHERE name = 'trip:update')),
       ((SELECT id FROM roles WHERE name = 'ADMIN'),
        (SELECT id FROM scopes WHERE name = 'trip:finish'));

/* DRIVER */
INSERT INTO role_scopes(role_id, scope_id)
VALUES ((SELECT id FROM roles WHERE name = 'DRIVER'),
        (SELECT id FROM scopes WHERE name = 'user:credential')),
       ((SELECT id FROM roles WHERE name = 'DRIVER'),
        (SELECT id FROM scopes WHERE name = 'trip:create')),
       ((SELECT id FROM roles WHERE name = 'DRIVER'),
        (SELECT id FROM scopes WHERE name = 'trip:update')),
       ((SELECT id FROM roles WHERE name = 'DRIVER'),
        (SELECT id FROM scopes WHERE name = 'trip:finish')),
       ((SELECT id FROM roles WHERE name = 'DRIVER'),
        (SELECT id FROM scopes WHERE name = 'trip:emergency'));

/* SCHEDULER */
INSERT INTO role_scopes(role_id, scope_id)
VALUES ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'user:credential')),
       ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'trip:update')),
       ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'trip:finish'));

/* APPLICANT */
INSERT INTO role_scopes(role_id, scope_id)
VALUES ((SELECT id FROM roles WHERE name = 'APPLICANT'),
        (SELECT id FROM scopes WHERE name = 'user:credential'));