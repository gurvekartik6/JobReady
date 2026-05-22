package com.jobread.app.database.converters;

import androidx.room.TypeConverter;

import com.jobread.app.models.JobStatus;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static JobStatus fromString(String value) {
        return value == null ? JobStatus.APPLIED : JobStatus.valueOf(value);
    }

    @TypeConverter
    public static String jobStatusToString(JobStatus status) {
        return status == null ? null : status.name();
    }
}
