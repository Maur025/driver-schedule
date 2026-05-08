package com.kernotec.driverschedule.service.util;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.kernotec.core.test.UnitTest;
import com.kernotec.driverschedule.service.request.jpa.enums.GenerateCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

@Slf4j
class CodeGeneratorUtilTest extends UnitTest {

    @InjectMocks
    private CodeGeneratorUtil codeGeneratorUtil;

    @Test
    @DisplayName("should return a code")
    void shouldReturnACode() {
        String generateCode = codeGeneratorUtil.generateCode();

        log.info("Generated Code: {}", generateCode);
        assertNotNull(generateCode);
    }

    @Test
    @DisplayName("should return a different code each time")
    void shouldReturnADifferentCodeEachTime() {
        String code1 = codeGeneratorUtil.generateCode();
        String code2 = codeGeneratorUtil.generateCode();

        assertNotNull(code1);
        assertNotNull(code2);
        assertNotEquals(code1, code2);
    }

    @Test
    @DisplayName("should return codes with AP prefix")
    void shouldReturnCodesWithAPPrefix() {
        String code = codeGeneratorUtil.generateCodeApp(GenerateCodeEnum.ASSET_PICKUP);

        log.info("Generated Code with AP Prefix: {}", code);
        assertNotNull(code);
        assertTrue(code.contains("AP"));
    }

    @Test
    @DisplayName("should return codes with RE prefix")
    void shouldReturnCodesWithREPrefix() {
        String code = codeGeneratorUtil.generateCodeApp(GenerateCodeEnum.REQUEST);
        log.info("Generated Code with RE Prefix: {}", code);

        assertNotNull(code);
        assertTrue(code.contains("RE"));
    }

    @Test
    @DisplayName("should return codes with RS prefix")
    void shouldReturnCodesWithRSPrefix() {
        String code = codeGeneratorUtil.generateCodeApp(GenerateCodeEnum.REQUEST_BY_SCHEDULER);
        log.info("Generated Code with RS Prefix: {}", code);

        assertNotNull(code);
        assertTrue(code.contains("RS"));
    }
}