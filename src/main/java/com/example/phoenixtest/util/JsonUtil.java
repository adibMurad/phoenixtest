package com.example.phoenixtest.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerErrorException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtil {
    public static String toString(byte[] gzippedJSON) {
        try (InputStream byteStream = new ByteArrayInputStream(gzippedJSON);
             GZIPInputStream gzipStream = new GZIPInputStream(byteStream)) {
            return new String(gzipStream.readAllBytes());
        } catch (IOException e) {
            log.error("Error decompressing JSON.", e);
            throw new ServerErrorException("Error decompressing JSON.", e);
        }
    }
}
