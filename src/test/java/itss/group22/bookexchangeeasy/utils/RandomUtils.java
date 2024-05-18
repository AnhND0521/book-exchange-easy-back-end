package itss.group22.bookexchangeeasy.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

public class RandomUtils {
    public static LocalDateTime randomPastTime() {
        long minEpoch = LocalDateTime.now().minusYears(1).toEpochSecond(ZoneOffset.UTC) * 1000;  // 1 year in the past
        long maxEpoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000;  // Current time
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(new Random().nextLong(minEpoch, maxEpoch)), ZoneOffset.UTC);
    }

    public static LocalDateTime randomPastTime(int daysBefore) {
        long minEpoch = LocalDateTime.now().minusDays(daysBefore).toEpochSecond(ZoneOffset.UTC) * 1000;
        long maxEpoch = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) * 1000;  // Current time
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(new Random().nextLong(minEpoch, maxEpoch)), ZoneOffset.UTC);
    }
}
