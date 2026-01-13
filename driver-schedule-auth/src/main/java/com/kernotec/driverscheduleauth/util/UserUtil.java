package com.kernotec.driverscheduleauth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserUtil {

    public String getUsernameSanitized(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }

        return username.strip()
            .toLowerCase();
    }

}
