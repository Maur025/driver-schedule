package com.kernotec.driverscheduleservice.util;

import com.kernotec.driverscheduleservice.jpa.enums.request.GenerateCodeEnum;
import java.security.SecureRandom;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CodeGeneratorUtil {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int NANO_TARGET = 8;

    public String encodeBase62(long value) {
        StringBuilder sb = new StringBuilder();

        while (value > 0) {
            sb.append(ALPHABET.charAt((int) (value % ALPHABET.length())));
            value /= ALPHABET.length();
        }

        String encoded = sb.reverse()
            .toString();

        if (encoded.length() > NANO_TARGET) {
            return encoded.substring(encoded.length() - NANO_TARGET);
        }

        if (sb.length() < NANO_TARGET) {
            int quantityToAdd = NANO_TARGET - encoded.length();
            return generateCodeSimple(quantityToAdd) + encoded;
        }

        return encoded;
    }

    public String generateCode() {
        var now = LocalDate.now();
        long nano = System.nanoTime();

        int yearValue = Math.max(Math.abs(now.getYear() % ALPHABET.length()), 1);

        char monthChar = ALPHABET.charAt(now.getMonthValue());
        char yearChar = ALPHABET.charAt(yearValue);

        var stringBuilder = new StringBuilder();
        stringBuilder.append(monthChar)
            .append(yearChar);

        String nanoBase62 = encodeBase62(nano);
        String suffix = generateCodeSimple(2);

        return stringBuilder.append(nanoBase62)
            .append(suffix)
            .toString();
    }

    public String generateCodeSimple(int length) {
        StringBuilder suffix = new StringBuilder();

        for (int index = 0; index < length; index++) {
            suffix.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return suffix.toString();
    }


    public String generateCodeApp(GenerateCodeEnum generateCodeEnum) {
        var code = new StringBuilder();

        String prefix = switch (generateCodeEnum) {
            case REQUEST -> "RE";
            case ASSET_PICKUP -> "AP";
            case REQUEST_BY_SCHEDULER -> "RS";
        };

        code.append(prefix);

        String codeGenerate = generateCode();

        return code.append("-")
            .append(codeGenerate)
            .toString();
    }
}
