package com.example.phoenixtest.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.Arrays;
import org.springframework.web.server.ServerErrorException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CompressUtil {
    private static final String ERROR_MSG_DECOMPRESS = "Error decompressing byte array.";
    private static final String ERROR_MSG_COMPRESSING = "Error compressing %s";

    public static String decompress(byte[] compressed) {
        if (Arrays.isNullOrEmpty(compressed)) {
            return null;
        }
        try (InputStream byteStream = new ByteArrayInputStream(compressed);
             GZIPInputStream gzipStream = new GZIPInputStream(byteStream)) {
            return new String(gzipStream.readAllBytes());
        } catch (IOException e) {
            log.error(ERROR_MSG_DECOMPRESS, e);
            throw new ServerErrorException(ERROR_MSG_DECOMPRESS, e);
        }
    }

    public static byte[] compress(String json) {
        if (json == null) {
            return null;
        }
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteStream)) {
                gzipStream.write(json.getBytes(StandardCharsets.UTF_8));
            }
            byteStream.close();
            return byteStream.toByteArray();
        } catch (IOException e) {
            log.error(String.format(ERROR_MSG_COMPRESSING, json), e);
            throw new ServerErrorException(String.format(ERROR_MSG_COMPRESSING, json), e);
        }
    }
}
