UPDATE trip_assignments
SET trip_assignment_state_id = (SELECT tas.id
                                FROM trip_assignment_states tas
                                WHERE tas.code = 'ACTIVE')
WHERE trip_assignment_state_id IS NULL;