INSERT INTO scopes(name)
VALUES ('openid'),
       ('profile'),
       ('read'),
       ('write'),
       ('read:request'),
       ('write:request'),
       ('read:vehicle'),
       ('write:vehicle'),
       ('read:schedule'),
       ('write:schedule'),
       ('read:person'),
       ('write:person'),
       ('read:trip'),
       ('write:trip');

/* ADMIN SCOPES */
INSERT INTO role_scopes(role_id, scope_id)
VALUES ((SELECT id FROM roles WHERE name = 'ADMIN'), (SELECT id FROM scopes WHERE name = 'openid')),
       ((SELECT id FROM roles WHERE name = 'ADMIN'),
        (SELECT id FROM scopes WHERE name = 'profile')),
       ((SELECT id FROM roles WHERE name = 'ADMIN'), (SELECT id FROM scopes WHERE name = 'read')),
       ((SELECT id FROM roles WHERE name = 'ADMIN'), (SELECT id FROM scopes WHERE name = 'write'));

/* DRIVER SCOPES */
INSERT INTO role_scopes(role_id, scope_id)
VALUES ((SELECT id FROM roles WHERE name = 'DRIVER'),
        (SELECT id FROM scopes WHERE name = 'openid')),
       ((SELECT id FROM roles WHERE name = 'DRIVER'),
        (SELECT id FROM scopes WHERE name = 'profile')),
       ((SELECT id FROM roles WHERE name = 'DRIVER'),
        (SELECT id FROM scopes WHERE name = 'read:vehicle')),
       ((SELECT id FROM roles WHERE name = 'DRIVER'),
        (SELECT id FROM scopes WHERE name = 'read:schedule')),
       ((SELECT id FROM roles WHERE name = 'DRIVER'),
        (SELECT id FROM scopes WHERE name = 'write:schedule')),
       ((SELECT id FROM roles WHERE name = 'DRIVER'),
        (SELECT id FROM scopes WHERE name = 'read:person')),
       ((SELECT id FROM roles WHERE name = 'DRIVER'),
        (SELECT id FROM scopes WHERE name = 'read:trip')),
       ((SELECT id FROM roles WHERE name = 'DRIVER'),
        (SELECT id FROM scopes WHERE name = 'write:trip'));

/* SCHEDULER SCOPES */
INSERT INTO role_scopes(role_id, scope_id)
VALUES ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'openid')),
       ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'profile')),
       ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'read:schedule')),
       ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'write:schedule')),
       ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'read:request')),
       ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'write:request')),
       ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'read:vehicle')),
       ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'write:vehicle')),
       ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'read:person')),
       ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'write:person')),
       ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'read:trip')),
       ((SELECT id FROM roles WHERE name = 'SCHEDULER'),
        (SELECT id FROM scopes WHERE name = 'write:trip'));

/* APPLICANT SCOPES */
INSERT INTO role_scopes(role_id, scope_id)
VALUES ((SELECT id FROM roles WHERE name = 'APPLICANT'),
        (SELECT id FROM scopes WHERE name = 'openid')),
       ((SELECT id FROM roles WHERE name = 'APPLICANT'),
        (SELECT id FROM scopes WHERE name = 'profile')),
       ((SELECT id FROM roles WHERE name = 'APPLICANT'),
        (SELECT id FROM scopes WHERE name = 'read:request')),
       ((SELECT id FROM roles WHERE name = 'APPLICANT'),
        (SELECT id FROM scopes WHERE name = 'write:request')),
       ((SELECT id FROM roles WHERE name = 'APPLICANT'),
        (SELECT id FROM scopes WHERE name = 'read:schedule')),
       ((SELECT id FROM roles WHERE name = 'APPLICANT'),
        (SELECT id FROM scopes WHERE name = 'read:vehicle')),
       ((SELECT id FROM roles WHERE name = 'APPLICANT'),
        (SELECT id FROM scopes WHERE name = 'read:person')),
       ((SELECT id FROM roles WHERE name = 'APPLICANT'),
        (SELECT id FROM scopes WHERE name = 'read:trip'));