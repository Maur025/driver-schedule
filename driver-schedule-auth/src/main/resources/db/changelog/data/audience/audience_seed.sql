INSERT INTO audiences (name)
VALUES ('driver.schedule.api');

INSERT INTO client_audiences(client_id, audience_id)
VALUES ((SELECT id FROM clients WHERE client_id = 'booking-web'),
        (SELECT id FROM audiences WHERE name = 'driver.schedule.api')),
       ((SELECT id FROM clients WHERE client_id = 'booking-app'),
        (SELECT id FROM audiences WHERE name = 'driver.schedule.api'));