package com.example.phoenixtest.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDateUtil {
    @DisplayName("Test fromEpochSecond")
    @Test
    public void testFromEpochSecond() {
        assertEquals(LocalDateTime.MIN, DateUtil.fromEpochSecond(null));
        final LocalDateTime now = LocalDateTime.now().withNano(0);
        assertEquals(now, DateUtil.fromEpochSecond(now.toEpochSecond(ZoneOffset.UTC)));
    }

}