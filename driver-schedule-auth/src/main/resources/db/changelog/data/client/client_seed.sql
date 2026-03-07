INSERT INTO clients(client_id)
VALUES ('booking-web'),
       ('booking-app');

INSERT INTO realm_clients(realm_id, client_id)
VALUES ((SELECT id FROM realms WHERE name = 'driver-schedule-auth'),
        (SELECT id FROM clients WHERE client_id = 'booking-web')),
       ((SELECT id FROM realms WHERE name = 'driver-schedule-auth'),
        (SELECT id FROM clients WHERE client_id = 'booking-app'));