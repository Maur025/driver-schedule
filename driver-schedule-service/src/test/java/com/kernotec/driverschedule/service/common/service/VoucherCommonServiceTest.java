package com.kernotec.driverschedule.service.common.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.util.TimeZone;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VoucherCommonServiceTest {

    @BeforeAll
    static void setUp() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Test
    @DisplayName("should return a value of request state")
    void shouldReturnAValueOfRequestState() {
        String[] status = {"REQUESTED", "REJECTED", "CANCELLED", "APPROVED"};
        String[] expectedResults = {"SOLICITADO", "RECHAZADO", "CANCELADO", "APROBADO"};

        for (int index = 0; index < status.length; index++) {
            String stateValueResponse = VoucherCommonService.getVoucherLabelRequestState(
                status[index]);

            assertEquals(expectedResults[index], stateValueResponse);
        }
    }

    @Test
    @DisplayName("should return a value of request state, when estatus is null")
    void shouldReturnAValueOfRequestState_whenStateIsNull() {
        String status = null;
        String expectedResult = "SIN ESTADO";

        String stateValueResponse = VoucherCommonService.getVoucherLabelRequestState(status);

        assertEquals(expectedResult, stateValueResponse);
    }

    @Test
    @DisplayName("should return a date string formated for voucher")
    void shouldReturnADateStringFormatedForVoucher() {
        String formattedDate = VoucherCommonService.formatDateToVoucher(
            Timestamp.valueOf("2026-02-03 21:10:00.000"), "America/La_Paz");

        assertNotNull(formattedDate);
        assertEquals("Feb 03, 2026 - 05:10 PM", formattedDate);
    }

    @Test
    @DisplayName("should return full name with 2 parameters")
    void shouldReturnFullNameWith2Parameters() {
        String name = "John";
        String lastName = "Doe";

        String fullName = VoucherCommonService.getPersonFullName(name, lastName);

        assertNotNull(fullName);
        assertEquals(name + " " + lastName, fullName);
    }

    @Test
    @DisplayName("should return only name when lastname is null")
    void shoulReturnOnlyNameWhenLastNameIsNull() {
        String name = "John";

        String fullName = VoucherCommonService.getPersonFullName(name, null);

        assertNotNull(fullName);
        assertEquals(name, fullName);
    }

    @Test
    @DisplayName("should return type value of Trip type")
    void shouldReturnTypeValueOfTripType() {
        String[] tripTypes = {"ONE_WAY", "ROUND_TRIP"};
        String[] expectedResults = {"Solo Ida", "Ida y Vuelta"};

        for (int index = 0; index < tripTypes.length; index++) {
            String tripTypeResponse = VoucherCommonService.getRequestTripType(tripTypes[index]);

            assertEquals(expectedResults[index], tripTypeResponse);
        }
    }

    @Test
    @DisplayName("should return type value of Trip type, when trip type is null")
    void shouldReturnTypeValueOfTripType_whenTripTypeIsNull() {
        String tripType = null;
        String expectedResult = "N/A";

        String tripTypeResponse = VoucherCommonService.getRequestTripType(tripType);
        assertEquals(expectedResult, tripTypeResponse);
    }

    @Test
    @DisplayName("should return na with invalid parameter")
    void shouldReturnNullWithInvalidParameter() {
        String withAdvance = VoucherCommonService.getRequestWithAdvance(null);

        assertEquals("N/A", withAdvance);
    }

    @Test
    @DisplayName("should return no in request with short notice")
    void shouldReturnNoInRequestWithShortNotice() {
        String withAdvance = VoucherCommonService.getRequestWithAdvance(true);

        assertEquals("No", withAdvance);
    }

    @Test
    @DisplayName("should return yes in request without advance")
    void shouldReturnYesInRequestWithoutAdvance() {
        String withAdvance = VoucherCommonService.getRequestWithAdvance(false);

        assertEquals("Sí", withAdvance);
    }

    @Test
    @DisplayName("should return na with invalid parameter in pickup")
    void shouldReturnNullWithInvalidParameterInPickup() {
        String assetPickup = VoucherCommonService.getRequestAssetPickup(null);

        assertEquals("N/A", assetPickup);
    }

    @Test
    @DisplayName("should return yes in request with asset pickup")
    void shouldReturnNoInRequestWithAssetPickup() {
        String assetPickup = VoucherCommonService.getRequestAssetPickup(true);

        assertEquals("Sí", assetPickup);
    }

    @Test
    @DisplayName("should return yes in request without asset pickup")
    void shouldReturnYesInRequestWithoutAssetPickup() {
        String assetPickup = VoucherCommonService.getRequestAssetPickup(false);

        assertEquals("No", assetPickup);
    }

    @Test
    @DisplayName("should return false when all parameters are null")
    void shouldReturnFalseWhenAllParametersAreNull() {
        boolean showRequestReasonMessage = VoucherCommonService.isShowRequestReasonMessage(
            null, null, null);

        assertFalse(showRequestReasonMessage);
    }

    @Test
    @DisplayName("should return false when state equal to REQUESTED")
    void shouldReturnFalseWhenStateEqualToRequested() {
        boolean showRequestReasonMessage = VoucherCommonService.isShowRequestReasonMessage(
            "REQUESTED", 45L, 20L);

        assertFalse(showRequestReasonMessage);
    }

    @Test
    @DisplayName("should return false when both counts are less than 1")
    void shouldReturnFalseWhenBothCountsAreLessThan1() {
        boolean showRequestReasonMessage = VoucherCommonService.isShowRequestReasonMessage(
            "CANCELLED", 0L, 0L);

        assertFalse(showRequestReasonMessage);
    }

    @Test
    @DisplayName("should return true when any count is greater than 0")
    void shouldReturnTrueWhenAnyCountIsGreaterThan0() {
        boolean showRequestReasonMessage = VoucherCommonService.isShowRequestReasonMessage(
            "CANCELLED", 1L, 0L);

        assertTrue(showRequestReasonMessage);
    }

    @Test
    @DisplayName("should return dateTime combined in America/La_Paz timezone")
    void shouldReturnDateTimeCombinedInAmericaLa_PazTimezone() {
        Timestamp timestampTime = Timestamp.valueOf("2026-02-04 21:10:00.000");

        String dateTimeStr = VoucherCommonService.getDateWithConcatDateAndTime(
            timestampTime, "America/La_Paz");

        assertEquals("Feb 04, 2026 - 05:10 PM", dateTimeStr);
    }

    @Test
    @DisplayName("should return phone numbers of strig array")
    void shouldReturnPhoneNumbersOfStringArray() {
        String stringArray = """
            [{"id" : "64de5a01-401a-4458-a0c3-1a5fc427836f", "value" : "77889997", "label" : "MAIN"}, {"id" : "c1828e60-9ab4-480d-9781-cf39f17383b6", "value" : "558987779", "label" : "OTHER"}, {"id" : "45388e2e-8445-4720-bd05-6172ead04b6c", "value" : "1616", "label" : "WORK"}]
            """;

        String phoneNumbers = VoucherCommonService.getPersonPhoneContacts(stringArray);

        assertEquals("77889997 - 1616", phoneNumbers);
    }

    @Test
    @DisplayName("should return state value of schedule state")
    void shouldReturnStateValueOfScheduleState() {
        String[] status = {"SCHEDULED", "RESCHEDULED", "CANCELLED", "IN_PROGRESS", "FINALIZED",
            "NEEDS_ACTION"};
        String[] expectedResults = {"SOLICITUD APROBADA", "REPROGRAMADO", "CANCELADO",
            "EN PROGRESO", "FINALIZADO", "ACCION REQUERIDA"};

        for (int index = 0; index < status.length; index++) {
            String stateValueResponse = VoucherCommonService.getVoucherLabelScheduleState(
                status[index]);

            assertEquals(expectedResults[index], stateValueResponse);
        }
    }

    @Test
    @DisplayName("should return state value of schedule state, when status is null")
    void shouldReturnStateValueOfScheduleState_whenStatusIsNull() {
        String status = null;
        String expectedResult = "SIN ESTADO";

        String stateValueResponse = VoucherCommonService.getVoucherLabelScheduleState(status);

        assertEquals(expectedResult, stateValueResponse);
    }

    @Test
    @DisplayName("should return false when code is not SCHEDULED")
    void shouldReturnFalseWhenCodeIsNotSCHEDULED() {
        String code = "SCHEDULED";
        boolean showReasonMessage = VoucherCommonService.isShowScheduleReasonMessage(code, 4L, 8L);

        assertFalse(showReasonMessage);
    }

    @Test
    @DisplayName("should return false when counts are 0 and code is not SCHEDULED")
    void shouldReturnFalseWhenCountIs0AndCodeIsNotSCHEDULED() {
        String code = "CANCELLED";

        boolean showReasonMessage = VoucherCommonService.isShowScheduleReasonMessage(code, 0L, 0L);

        assertFalse(showReasonMessage);
    }

    @Test
    @DisplayName("should return true when any count is greater than 0 and code is not SCHEDULED")
    void shouldReturnTrueWhenAnyCountIsGreaterThan0AndCodeIsNotSCHEDULED() {
        String code = "RESCHEDULED";

        boolean showReasonMessage = VoucherCommonService.isShowScheduleReasonMessage(code, 0L, 2L);

        assertTrue(showReasonMessage);
    }

    @Test
    @DisplayName("should return message when parameter is a valid strig array of reasons")
    void shouldReturnMessageWhenParameterIsAValidStrigArrayOfReasons() {
        String rejectReasonArrayStr = """
            [{"reasonLabel" : "Horario no operativo", "reasonCode" : "OUT_OF_OPERATING_HOURS", "otherReason" : null},
            {"reasonLabel" : "Otro motivo", "reasonCode" : "OTHER_REJECT", "otherReason" : "Razon personalizada por el cliente"}]
            """;

        String reasonMessage = VoucherCommonService.getRequestReasonMessages(
            "REJECTED", "[]", rejectReasonArrayStr);

        assertEquals("Horario no operativo, Razon personalizada por el cliente", reasonMessage);
    }

    @Test
    @DisplayName("should return fullname of object string requested by")
    void shouldReturnFullnameOfObjectStringRequestedBy() {
        String requestedByStr = """
            {"id": "fbd9dc82-e4d0-49a9-bbc0-9ba3c2903562", "name": "mauro moya", "roles": ["ROLE_SCHEDULER"], "authTime": 1770388673, "username": "mmoya", "auditUserDataType": "auditUserData.AuthUserData"}
            """;

        String name = VoucherCommonService.getRequestedByFullName(requestedByStr);

        assertEquals("mauro moya", name);
    }

    @Test
    @DisplayName("should return schedule reason when param is a valid string array of reasons")
    void shouldReturnScheduleReasonWhenParamIsAValidStringArrayOfReasons() {
        String rescheduleReasonArrayStr = """
            [{"reasonLabel" : "Retraso por tráfico", "reasonCode" : "TRAFFIC_DELAY", "otherReason" : null},
            {"reasonLabel" : "Otro motivo", "reasonCode" : "OTHER_RESCHEDULED", "otherReason" : "Razon personalizada por el cliente"}]
            """;

        String reasonMessage = VoucherCommonService.getScheduleReasonMessage(
            "RESCHEDULED", "[]", rescheduleReasonArrayStr);
        assertEquals("Retraso por tráfico, Razon personalizada por el cliente", reasonMessage);
    }

    @Test
    @DisplayName("should return reschedule reason more cancel reason when 2 array of reasons")
    void shouldReturnRescheduleReasonMoreCancelReasonWhen2ArrayOfReasons() {
        String rescheduledReasonStr = """
            [{"reasonLabel" : "Retraso por tráfico", "reasonCode" : "TRAFFIC_DELAY", "otherReason" : null}]
            """;

        String cancelledReasonStr = """
            [{"reasonLabel" : "Emergencia operativa", "reasonCode" : "OPERATIONAL_EMERGENCY", "otherReason" : null}]
            """;

        String reasonMessage = VoucherCommonService.getScheduleReasonMessage(
            "RESCHEDULED", cancelledReasonStr, rescheduledReasonStr);

        assertEquals("Retraso por tráfico, Emergencia operativa", reasonMessage);
    }
}