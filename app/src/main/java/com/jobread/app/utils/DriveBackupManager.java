package com.jobread.app.utils;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Manages backup and restore of job data to/from Google Drive appdata folder.
 * Requires the user to be signed in with Google and Drive API scope granted.
 */
public class DriveBackupManager {

    private static final String BACKUP_FILE_NAME = "jobread_backup.csv";
    private static final String MIME_TYPE_CSV = "text/csv";
    private static final String APP_DATA_SPACE = "appDataFolder";

    private final Context mContext;

    public DriveBackupManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    /**
     * Returns an authenticated Drive service, or null if not signed in.
     */
    private Drive getDriveService() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mContext);
        if (account == null) return null;

        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(
                mContext, Collections.singleton(DriveScopes.DRIVE_APPDATA));
        credential.setSelectedAccount(account.getAccount());

        return new Drive.Builder(
                new NetHttpTransport(),
                new GsonFactory(),
                credential)
                .setApplicationName("JobRead")
                .build();
    }

    /** Backs up CSV string to Google Drive appdata folder. */
    public Completable backupToDrive(String csvContent) {
        return Completable.fromAction(() -> {
            Drive drive = getDriveService();
            if (drive == null) throw new IllegalStateException("Not signed in to Google");

            String existingFileId = findBackupFileId(drive);
            ByteArrayContent mediaContent = new ByteArrayContent(
                    MIME_TYPE_CSV, csvContent.getBytes("UTF-8"));

            if (existingFileId != null) {
                drive.files().update(existingFileId, null, mediaContent).execute();
            } else {
                File fileMetadata = new File();
                fileMetadata.setName(BACKUP_FILE_NAME);
                fileMetadata.setParents(Collections.singletonList(APP_DATA_SPACE));
                drive.files().create(fileMetadata, mediaContent).setFields("id").execute();
            }
        }).subscribeOn(Schedulers.io());
    }

    /** Restores CSV content string from Google Drive appdata folder. */
    public Single<String> restoreFromDrive() {
        return Single.fromCallable(() -> {
            Drive drive = getDriveService();
            if (drive == null) throw new IllegalStateException("Not signed in to Google");

            String fileId = findBackupFileId(drive);
            if (fileId == null) throw new IOException("No backup file found on Drive");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);
            return outputStream.toString("UTF-8");
        }).subscribeOn(Schedulers.io());
    }

    private String findBackupFileId(Drive drive) throws IOException {
        FileList result = drive.files().list()
                .setSpaces(APP_DATA_SPACE)
                .setQ("name = '" + BACKUP_FILE_NAME + "'")
                .setFields("files(id, name)")
                .execute();

        if (result.getFiles() != null && !result.getFiles().isEmpty()) {
            return result.getFiles().get(0).getId();
        }
        return null;
    }

    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(mContext) != null;
    }
}
