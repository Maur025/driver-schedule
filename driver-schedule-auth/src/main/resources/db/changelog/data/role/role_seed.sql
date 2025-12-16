INSERT INTO roles(name, resource, realm_id)
VALUES ('ADMIN', 'driver-schedule',
        (SELECT id FROM realms WHERE realms.name = 'driver-schedule-auth')),
       ('DRIVER', 'driver-schedule',
        (SELECT id FROM realms WHERE realms.name = 'driver-schedule-auth')),
       ('SCHEDULER', 'driver-schedule',
        (SELECT id FROM realms WHERE realms.name = 'driver-schedule-auth')),
       ('APPLICANT', 'driver-schedule',
        (SELECT id FROM realms WHERE realms.name = 'driver-schedule-auth'));