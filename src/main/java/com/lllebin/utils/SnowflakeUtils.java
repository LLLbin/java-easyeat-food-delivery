package com.lllebin.utils;

import org.springframework.stereotype.Component;

/**
 * ClassName: Snowflake
 * Package: com.lllebin.minitwitter.util
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Component
public class SnowflakeUtils {

    // TIMESTAMP, DATACENTER(region), MACHINE, SEQUENCE
    private final static long SEQUENCE_BIT = 12;
    private final static long DATACENTER_BIT = 5;
    private final static long MACHINE_BIT = 5;
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = MACHINE_BIT + MACHINE_LEFT;
    private final static long TIMESTAMP_LEFT = DATACENTER_BIT + DATACENTER_LEFT;
    private final static long MAX_SEQUENCE_NUM = -1L ^ (-1L << SEQUENCE_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long START_TIMESTAMP = 1687748413809L;

    private long machineId;
    private long datacenterId;
    private long lastTimeStamp = -1;

    private long sequence = 0;

    public SnowflakeUtils() {

    }

    public SnowflakeUtils(long machineId, long datacenterId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("Invalid Datacenter ID");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("Invalid Machine ID");
        }

        this.machineId = machineId;
        this.datacenterId = datacenterId;
    }

    public long nextId() {
        long currentTimeStamp = getCurrentTimeStamp();
        if (lastTimeStamp == currentTimeStamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE_NUM;
            if(sequence == 0L) {
                currentTimeStamp = getNextMill();
            }
        } else {
            sequence = 0;
        }

        lastTimeStamp = currentTimeStamp;
        return (currentTimeStamp - START_TIMESTAMP) << TIMESTAMP_LEFT
                | datacenterId << DATACENTER_LEFT
                | machineId << MACHINE_LEFT
                | sequence;
    }

    private long getNextMill() {
        long mill = getCurrentTimeStamp();
        while(mill <= lastTimeStamp) {
            mill = getNextMill();
        }
        return mill;
    }

    private long getCurrentTimeStamp() {
        return System.currentTimeMillis();
    }
}
