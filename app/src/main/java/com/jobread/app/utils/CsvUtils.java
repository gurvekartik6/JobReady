package com.jobread.app.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.JobStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CsvUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

    private static final String[] CSV_HEADERS = {
        "id", "company", "role", "date_applied", "status",
        "notes", "contact_person", "contact_phone", "contact_email",
        "salary_range", "is_archived"
    };

    public static Uri exportToCsv(Context context, List<JobEntity> jobs) throws IOException {
        String fileName = "jobread_export_" + System.currentTimeMillis() + ".csv";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Downloads.DISPLAY_NAME, fileName);
            values.put(MediaStore.Downloads.MIME_TYPE, "text/csv");
            values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = context.getContentResolver().insert(
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

            if (uri != null) {
                try (OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                     OutputStreamWriter writer = new OutputStreamWriter(outputStream)) {
                    writeCsvContent(writer, jobs);
                }
            }
            return uri;
        } else {
            File downloadsDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS);
            if (!downloadsDir.exists()) downloadsDir.mkdirs();

            File file = new File(downloadsDir, fileName);
            try (FileOutputStream fos = new FileOutputStream(file);
                 OutputStreamWriter writer = new OutputStreamWriter(fos)) {
                writeCsvContent(writer, jobs);
            }
            return Uri.fromFile(file);
        }
    }

    private static void writeCsvContent(OutputStreamWriter writer,
                                         List<JobEntity> jobs) throws IOException {
        // Header
        writer.write(String.join(",", CSV_HEADERS));
        writer.write("\n");

        // Rows
        for (JobEntity job : jobs) {
            StringBuilder sb = new StringBuilder();
            sb.append(escapeCsv(job.getId())).append(",");
            sb.append(escapeCsv(job.getCompany())).append(",");
            sb.append(escapeCsv(job.getRole())).append(",");
            sb.append(job.getDateApplied() != null ?
                    sDateFormat.format(job.getDateApplied()) : "").append(",");
            sb.append(job.getStatus() != null ? job.getStatus().name() : "APPLIED").append(",");
            sb.append(escapeCsv(job.getNotes())).append(",");
            sb.append(escapeCsv(job.getContactPerson())).append(",");
            sb.append(escapeCsv(job.getContactPhone())).append(",");
            sb.append(escapeCsv(job.getContactEmail())).append(",");
            sb.append(escapeCsv(job.getSalaryRange())).append(",");
            sb.append(job.isArchived() ? "1" : "0");
            writer.write(sb.toString());
            writer.write("\n");
        }
        writer.flush();
    }

    public static List<JobEntity> importFromCsv(Context context, Uri uri,
                                                  String userId) throws IOException {
        List<JobEntity> jobs = new ArrayList<>();
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                JobEntity job = parseCsvLine(line, userId);
                if (job != null) {
                    jobs.add(job);
                }
            }
        }
        return jobs;
    }

    private static JobEntity parseCsvLine(String line, String userId) {
        String[] parts = parseCsvRow(line);
        if (parts.length < 4) return null;

        try {
            JobEntity job = new JobEntity();
            job.setId(parts[0].isEmpty() ? UUID.randomUUID().toString() : parts[0]);
            job.setCompany(parts[1]);
            job.setRole(parts[2]);

            try {
                job.setDateApplied(parts[3].isEmpty() ? new Date() : sDateFormat.parse(parts[3]));
            } catch (ParseException e) {
                job.setDateApplied(new Date());
            }

            try {
                job.setStatus(parts.length > 4 && !parts[4].isEmpty() ?
                        JobStatus.valueOf(parts[4]) : JobStatus.APPLIED);
            } catch (IllegalArgumentException e) {
                job.setStatus(JobStatus.APPLIED);
            }

            if (parts.length > 5) job.setNotes(parts[5]);
            if (parts.length > 6) job.setContactPerson(parts[6]);
            if (parts.length > 7) job.setContactPhone(parts[7]);
            if (parts.length > 8) job.setContactEmail(parts[8]);
            if (parts.length > 9) job.setSalaryRange(parts[9]);
            if (parts.length > 10) job.setArchived("1".equals(parts[10]));

            job.setUserId(userId);
            job.setCreatedAt(new Date());
            job.setUpdatedAt(new Date());

            return job;
        } catch (Exception e) {
            return null;
        }
    }

    private static String[] parseCsvRow(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString().trim());
        return result.toArray(new String[0]);
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
