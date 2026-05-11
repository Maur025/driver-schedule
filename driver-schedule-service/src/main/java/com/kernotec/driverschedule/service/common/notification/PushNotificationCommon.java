package com.kernotec.driverschedule.service.common.notification;

import com.kernotec.core.util.MessageUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@RequiredArgsConstructor
public class PushNotificationCommon {

    private final MessageSource messageSource;

    protected Map<String, String> getMapWithValues(Map<String, String> baseMap, UUID referenceId) {
        Map<String, String> mapWithValues = new HashMap<>();

        for (Map.Entry<String, String> entry : baseMap.entrySet()) {
            if (entry.getKey()
                .equals("screen"))
            {
                mapWithValues.put(entry.getKey(), String.format(entry.getValue(), referenceId));
                continue;
            }

            mapWithValues.put(entry.getKey(), entry.getValue());
        }

        mapWithValues.put("referenceId", String.valueOf(referenceId));
        return mapWithValues;
    }

    protected String getMessage(String key, List<Object> params) {
        return MessageUtil.getMessage(key, params.toArray(), getLocale(), messageSource);
    }

    protected Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}
