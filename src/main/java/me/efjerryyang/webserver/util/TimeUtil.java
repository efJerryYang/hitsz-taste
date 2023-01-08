package me.efjerryyang.webserver.util;

import org.slf4j.Logger;

import java.sql.Timestamp;

public class TimeUtil {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(TimeUtil.class);

    public static Timestamp longToTimestamp(long time) {
        return new Timestamp(time);
    }

    public static String timestampToDatetime(Timestamp timestamp, boolean removeMilli) {
        logger.info("timestampToDatetime: {}", timestamp);
        // "yyyy-mm-dd hh:mm:ss.fffffffff".length() = 29
        // "yyyy-mm-dd hh:mm:ss".length() = 19
        if (removeMilli)
            return timestamp.toString().substring(0, 19);
        else
            return timestamp.toString();
    }

    public static Timestamp datetimeToTimestamp(String datetime) {
        logger.info("datetimeToTimestamp: {}", datetime);
        return Timestamp.valueOf(datetime);
    }
}
