package com.example.phoenixtest.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ServerErrorException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestCompressUtil {
    private static final String TEST_STRING = "this is a test";
    private static final byte[] TEST_BYTES = {31, -117, 8, 0, 0, 0, 0, 0, 0, 0, 43, -55, -56, 44, 86, 0, -94, 68, -123, -110, -44, -30, 18, 0, -22, -25, 30, 13, 14, 0, 0, 0};

    @DisplayName("Test decompress")
    @Test
    public void testDecompress() {
        assertNull(CompressUtil.decompress(null));
        assertNull(CompressUtil.decompress(new byte[0]));
        assertThrows(ServerErrorException.class, () -> CompressUtil.decompress(new byte[]{0, 1, 2, 3}));
        assertEquals(TEST_STRING, CompressUtil.decompress(TEST_BYTES));
    }

    @DisplayName("Test compress")
    @Test
    public void testCompress() {
        assertNull(CompressUtil.compress(null));
        assertEquals(Arrays.toString(TEST_BYTES), Arrays.toString(CompressUtil.compress(TEST_STRING)));
    }

}