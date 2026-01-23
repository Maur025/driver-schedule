package com.kernotec.driverscheduleservice.util;

import java.io.File;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FileUtil {

    public File createTempFile(String prefix, String suffix) {
        try {
            return File.createTempFile(prefix, suffix);
        } catch (IOException ex) {

            throw new RuntimeException(ex);
        }
    }
}
