package com.ikubinfo.rental.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CommonUtils {

    private CommonUtils() {}

    public static MultipartFile createMultipartFile() {
        return new MultipartFile() {
            @Override
            public String getName() {
                return "some name";
            }

            @Override
            public String getOriginalFilename() {
                return "some name";
            }

            @Override
            public String getContentType() {
                return "image/png";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 10;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[10];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new InputStream() {
                    @Override
                    public int read() throws IOException {
                        return 10;
                    }
                };
            }

            @Override
            public void transferTo(File file) throws IOException, IllegalStateException {

            }
        };
    }
}
