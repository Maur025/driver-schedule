/* TRIP EMERGENCY */
INSERT INTO reasons(reason_type_id, value, code)
VALUES ((SELECT id FROM reason_types rt WHERE rt.code = 'TRIP_EMERGENCY'), 'Falla Mecánica',
        'MECHANICAL_FAILURE'),
       ((SELECT id FROM reason_types rt WHERE rt.code = 'TRIP_EMERGENCY'), 'Neumático pinchado',
        'FLAT_TIRE'),
       ((SELECT id FROM reason_types rt WHERE rt.code = 'TRIP_EMERGENCY'), 'Falta de Combustible',
        'OUT_OF_FUEL'),
       ((SELECT id FROM reason_types rt WHERE rt.code = 'TRIP_EMERGENCY'), 'Vehículo inmovilizado',
        'DISABLED_VEHICLE'),
       ((SELECT id FROM reason_types rt WHERE rt.code = 'TRIP_EMERGENCY'), 'Otro',
        'OTHER_TRIP_EMERGENCY');

/* TRIP EMERGENCY REJECTED */
INSERT INTO reasons(reason_type_id, value, code)
VALUES ((SELECT id FROM reason_types rt WHERE rt.code = 'TRIP_EMERGENCY_REJECTED'),
        'Información insuficiente', 'INSUFFICIENT_INFORMATION'),
       ((SELECT id FROM reason_types rt WHERE rt.code = 'TRIP_EMERGENCY_REJECTED'),
        'Ubicación incorrecta o no disponible', 'INVALID_OR_UNAVAILABLE_LOCATION'),
       ((SELECT id FROM reason_types rt WHERE rt.code = 'TRIP_EMERGENCY_REJECTED'),
        'Solicitud duplicada', 'DUPLICATE_REQUEST'),
       ((SELECT id FROM reason_types rt WHERE rt.code = 'TRIP_EMERGENCY_REJECTED'),
        'Emergencia no válida', 'INVALID_EMERGENCY'),
       ((SELECT id FROM reason_types rt WHERE rt.code = 'TRIP_EMERGENCY_REJECTED'),
        'Cancelado por el usuario', 'CANCELLED_BY_USER'),
       ((SELECT id FROM reason_types rt WHERE rt.code = 'TRIP_EMERGENCY_REJECTED'),
        'Otro', 'OTHER_TRIP_EMERGENCY_REJECTED');