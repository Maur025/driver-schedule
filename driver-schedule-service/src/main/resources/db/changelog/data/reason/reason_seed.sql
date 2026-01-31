/* REJECT REASONS */
INSERT INTO reasons(value, code, reason_type_id)
VALUES ('Vehiculos no disponibles', 'NO_VEHICLES_AVAILABLE',
        (SELECT id FROM reason_types WHERE code = 'REQUEST_REJECTED')),
       ('Conductores no disponibles', 'NO_DRIVERS_AVAILABLE',
        (SELECT id FROM reason_types WHERE code = 'REQUEST_REJECTED')),
       ('Horario no operativo', 'OUT_OF_OPERATING_HOURS',
        (SELECT id FROM reason_types WHERE code = 'REQUEST_REJECTED')),
       ('Otro motivo', 'OTHER_REJECT',
        (SELECT id FROM reason_types WHERE code = 'REQUEST_REJECTED'));

/* SCHEDULE CANCEL REASONS */
INSERT INTO reasons(value, code, reason_type_id)
VALUES ('Vehiculo en mantenimiento', 'VEHICLE_MAINTENANCE',
        (SELECT id FROM reason_types WHERE code = 'SCHEDULE_CANCELLED')),
       ('Falta de combustible', 'LOW_FUEL',
        (SELECT id FROM reason_types WHERE code = 'SCHEDULE_CANCELLED')),
       ('Problemas en la vía', 'ROAD_INCIDENT',
        (SELECT id FROM reason_types WHERE code = 'SCHEDULE_CANCELLED')),
       ('Emergencia operativa', 'OPERATIONAL_EMERGENCY',
        (SELECT id FROM reason_types WHERE code = 'SCHEDULE_CANCELLED')),
       ('Vehiculo no disponible', 'VEHICLE_NOT_AVAILABLE',
        (SELECT id FROM reason_types WHERE code = 'SCHEDULE_CANCELLED')),
       ('Conductor no disponible', 'DRIVER_NOT_AVAILABLE',
        (SELECT id FROM reason_types WHERE code = 'SCHEDULE_CANCELLED')),
       ('Otro motivo', 'OTHER_SCHEDULE_CANCELLED',
        (SELECT id FROM reason_types WHERE code = 'SCHEDULE_CANCELLED'));

/* RESCHEDULE REASONS */
INSERT INTO reasons(value, code, reason_type_id)
VALUES ('Conflicto de horario', 'SCHEDULE_CONFLICT',
        (SELECT id FROM reason_types WHERE code = 'RESCHEDULED')),
       ('Cambio de prioridad', 'PRIORITY_CHANGE',
        (SELECT id FROM reason_types WHERE code = 'RESCHEDULED')),
       ('Retraso por tráfico', 'TRAFFIC_DELAY',
        (SELECT id FROM reason_types WHERE code = 'RESCHEDULED')),
       ('Ajuste de planificación', 'PLANNING_ADJUSTMENT',
        (SELECT id FROM reason_types WHERE code = 'RESCHEDULED')),
       ('Vehiculo temporalmente no disponible', 'VEHICLE_TEMPORARILY_UNAVAILABLE',
        (SELECT id FROM reason_types WHERE code = 'RESCHEDULED')),
       ('Conductor temporalmente no disponible', 'DRIVER_TEMPORARILY_UNAVAILABLE',
        (SELECT id FROM reason_types WHERE code = 'RESCHEDULED')),
       ('Otro motivo', 'OTHER_RESCHEDULED',
        (SELECT id FROM reason_types WHERE code = 'RESCHEDULED'));

/* REQUEST CANCELLED REASONS */
INSERT INTO reasons(value, code, reason_type_id)
VALUES ('Cambio de planes', 'USER_PLAN_CHANGE',
        (SELECT id FROM reason_types WHERE code = 'REQUEST_CANCELLED')),
       ('Error en la fecha u horario solicitado', 'DATE_TIME_REQUESTED_ERROR',
        (SELECT id FROM reason_types WHERE code = 'REQUEST_CANCELLED')),
       ('Ya no se requiere el servicio', 'SERVICE_NOT_NEEDED',
        (SELECT id FROM reason_types WHERE code = 'REQUEST_CANCELLED')),
       ('Reprogramación interna', 'INTERNAL_RESCHEDULING',
        (SELECT id FROM reason_types WHERE code = 'REQUEST_CANCELLED')),
       ('Solicitud duplicada', 'DUPLICATE_REQUEST',
        (SELECT id FROM reason_types WHERE code = 'REQUEST_CANCELLED')),
       ('Actividad cancelada', 'ACTIVITY_CANCELLED',
        (SELECT id FROM reason_types WHERE code = 'REQUEST_CANCELLED')),
       ('Otro motivo', 'OTHER_REQUEST_CANCELLED',
        (SELECT id FROM reason_types WHERE code = 'REQUEST_CANCELLED'))
