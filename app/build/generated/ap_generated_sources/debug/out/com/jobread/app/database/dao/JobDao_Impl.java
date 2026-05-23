package com.jobread.app.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.rxjava3.EmptyResultSetException;
import androidx.room.rxjava3.RxRoom;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.jobread.app.database.converters.Converters;
import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.JobStatus;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class JobDao_Impl implements JobDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<JobEntity> __insertionAdapterOfJobEntity;

  private final EntityDeletionOrUpdateAdapter<JobEntity> __deletionAdapterOfJobEntity;

  private final EntityDeletionOrUpdateAdapter<JobEntity> __updateAdapterOfJobEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllForUser;

  private final SharedSQLiteStatement __preparedStmtOfSetArchived;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  public JobDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfJobEntity = new EntityInsertionAdapter<JobEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `jobs` (`id`,`company`,`role`,`date_applied`,`status`,`notes`,`contact_person`,`contact_phone`,`contact_email`,`salary_range`,`is_archived`,`user_id`,`created_at`,`updated_at`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final JobEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getCompany() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCompany());
        }
        if (entity.getRole() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getRole());
        }
        final Long _tmp = Converters.dateToTimestamp(entity.getDateApplied());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp);
        }
        final String _tmp_1 = Converters.jobStatusToString(entity.getStatus());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        if (entity.getNotes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getNotes());
        }
        if (entity.getContactPerson() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getContactPerson());
        }
        if (entity.getContactPhone() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getContactPhone());
        }
        if (entity.getContactEmail() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getContactEmail());
        }
        if (entity.getSalaryRange() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getSalaryRange());
        }
        final int _tmp_2 = entity.isArchived() ? 1 : 0;
        statement.bindLong(11, _tmp_2);
        if (entity.getUserId() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getUserId());
        }
        final Long _tmp_3 = Converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_3 == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, _tmp_3);
        }
        final Long _tmp_4 = Converters.dateToTimestamp(entity.getUpdatedAt());
        if (_tmp_4 == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, _tmp_4);
        }
      }
    };
    this.__deletionAdapterOfJobEntity = new EntityDeletionOrUpdateAdapter<JobEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `jobs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final JobEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfJobEntity = new EntityDeletionOrUpdateAdapter<JobEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `jobs` SET `id` = ?,`company` = ?,`role` = ?,`date_applied` = ?,`status` = ?,`notes` = ?,`contact_person` = ?,`contact_phone` = ?,`contact_email` = ?,`salary_range` = ?,`is_archived` = ?,`user_id` = ?,`created_at` = ?,`updated_at` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final JobEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getCompany() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCompany());
        }
        if (entity.getRole() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getRole());
        }
        final Long _tmp = Converters.dateToTimestamp(entity.getDateApplied());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp);
        }
        final String _tmp_1 = Converters.jobStatusToString(entity.getStatus());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        if (entity.getNotes() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getNotes());
        }
        if (entity.getContactPerson() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getContactPerson());
        }
        if (entity.getContactPhone() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getContactPhone());
        }
        if (entity.getContactEmail() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getContactEmail());
        }
        if (entity.getSalaryRange() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getSalaryRange());
        }
        final int _tmp_2 = entity.isArchived() ? 1 : 0;
        statement.bindLong(11, _tmp_2);
        if (entity.getUserId() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getUserId());
        }
        final Long _tmp_3 = Converters.dateToTimestamp(entity.getCreatedAt());
        if (_tmp_3 == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, _tmp_3);
        }
        final Long _tmp_4 = Converters.dateToTimestamp(entity.getUpdatedAt());
        if (_tmp_4 == null) {
          statement.bindNull(14);
        } else {
          statement.bindLong(14, _tmp_4);
        }
        if (entity.getId() == null) {
          statement.bindNull(15);
        } else {
          statement.bindString(15, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteAllForUser = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM jobs WHERE user_id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetArchived = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE jobs SET is_archived = ?, updated_at = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE jobs SET status = ?, updated_at = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Completable insert(final JobEntity job) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      @Nullable
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfJobEntity.insert(job);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable insertAll(final List<JobEntity> jobs) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      @Nullable
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfJobEntity.insert(jobs);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable delete(final JobEntity job) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      @Nullable
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfJobEntity.handle(job);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable update(final JobEntity job) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      @Nullable
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfJobEntity.handle(job);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable deleteAllForUser(final String userId) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      @Nullable
      public Void call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllForUser.acquire();
        int _argIndex = 1;
        if (userId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, userId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return null;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllForUser.release(_stmt);
        }
      }
    });
  }

  @Override
  public Completable setArchived(final String jobId, final boolean archived, final Date updatedAt) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      @Nullable
      public Void call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetArchived.acquire();
        int _argIndex = 1;
        final int _tmp = archived ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        final Long _tmp_1 = Converters.dateToTimestamp(updatedAt);
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp_1);
        }
        _argIndex = 3;
        if (jobId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, jobId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return null;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetArchived.release(_stmt);
        }
      }
    });
  }

  @Override
  public Completable updateStatus(final String jobId, final JobStatus status,
      final Date updatedAt) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      @Nullable
      public Void call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
        int _argIndex = 1;
        final String _tmp = Converters.jobStatusToString(status);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _tmp);
        }
        _argIndex = 2;
        final Long _tmp_1 = Converters.dateToTimestamp(updatedAt);
        if (_tmp_1 == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, _tmp_1);
        }
        _argIndex = 3;
        if (jobId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, jobId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return null;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateStatus.release(_stmt);
        }
      }
    });
  }

  @Override
  public Flowable<List<JobEntity>> getAllActiveJobs(final String userId) {
    final String _sql = "SELECT * FROM jobs WHERE user_id = ? AND is_archived = 0 ORDER BY date_applied DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    return RxRoom.createFlowable(__db, false, new String[] {"jobs"}, new Callable<List<JobEntity>>() {
      @Override
      public List<JobEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfMRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfMDateApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "date_applied");
          final int _cursorIndexOfMStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfMContactPerson = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_person");
          final int _cursorIndexOfMContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_phone");
          final int _cursorIndexOfMContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_email");
          final int _cursorIndexOfMSalaryRange = CursorUtil.getColumnIndexOrThrow(_cursor, "salary_range");
          final int _cursorIndexOfMArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfMUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfMCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfMUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<JobEntity> _result = new ArrayList<JobEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JobEntity _item;
            _item = new JobEntity();
            final String _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getString(_cursorIndexOfMId);
            }
            _item.setId(_tmpMId);
            final String _tmpMCompany;
            if (_cursor.isNull(_cursorIndexOfMCompany)) {
              _tmpMCompany = null;
            } else {
              _tmpMCompany = _cursor.getString(_cursorIndexOfMCompany);
            }
            _item.setCompany(_tmpMCompany);
            final String _tmpMRole;
            if (_cursor.isNull(_cursorIndexOfMRole)) {
              _tmpMRole = null;
            } else {
              _tmpMRole = _cursor.getString(_cursorIndexOfMRole);
            }
            _item.setRole(_tmpMRole);
            final Date _tmpMDateApplied;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfMDateApplied)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfMDateApplied);
            }
            _tmpMDateApplied = Converters.fromTimestamp(_tmp);
            _item.setDateApplied(_tmpMDateApplied);
            final JobStatus _tmpMStatus;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMStatus)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfMStatus);
            }
            _tmpMStatus = Converters.fromString(_tmp_1);
            _item.setStatus(_tmpMStatus);
            final String _tmpMNotes;
            if (_cursor.isNull(_cursorIndexOfMNotes)) {
              _tmpMNotes = null;
            } else {
              _tmpMNotes = _cursor.getString(_cursorIndexOfMNotes);
            }
            _item.setNotes(_tmpMNotes);
            final String _tmpMContactPerson;
            if (_cursor.isNull(_cursorIndexOfMContactPerson)) {
              _tmpMContactPerson = null;
            } else {
              _tmpMContactPerson = _cursor.getString(_cursorIndexOfMContactPerson);
            }
            _item.setContactPerson(_tmpMContactPerson);
            final String _tmpMContactPhone;
            if (_cursor.isNull(_cursorIndexOfMContactPhone)) {
              _tmpMContactPhone = null;
            } else {
              _tmpMContactPhone = _cursor.getString(_cursorIndexOfMContactPhone);
            }
            _item.setContactPhone(_tmpMContactPhone);
            final String _tmpMContactEmail;
            if (_cursor.isNull(_cursorIndexOfMContactEmail)) {
              _tmpMContactEmail = null;
            } else {
              _tmpMContactEmail = _cursor.getString(_cursorIndexOfMContactEmail);
            }
            _item.setContactEmail(_tmpMContactEmail);
            final String _tmpMSalaryRange;
            if (_cursor.isNull(_cursorIndexOfMSalaryRange)) {
              _tmpMSalaryRange = null;
            } else {
              _tmpMSalaryRange = _cursor.getString(_cursorIndexOfMSalaryRange);
            }
            _item.setSalaryRange(_tmpMSalaryRange);
            final boolean _tmpMArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfMArchived);
            _tmpMArchived = _tmp_2 != 0;
            _item.setArchived(_tmpMArchived);
            final String _tmpMUserId;
            if (_cursor.isNull(_cursorIndexOfMUserId)) {
              _tmpMUserId = null;
            } else {
              _tmpMUserId = _cursor.getString(_cursorIndexOfMUserId);
            }
            _item.setUserId(_tmpMUserId);
            final Date _tmpMCreatedAt;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfMCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfMCreatedAt);
            }
            _tmpMCreatedAt = Converters.fromTimestamp(_tmp_3);
            _item.setCreatedAt(_tmpMCreatedAt);
            final Date _tmpMUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfMUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfMUpdatedAt);
            }
            _tmpMUpdatedAt = Converters.fromTimestamp(_tmp_4);
            _item.setUpdatedAt(_tmpMUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flowable<List<JobEntity>> getArchivedJobs(final String userId) {
    final String _sql = "SELECT * FROM jobs WHERE user_id = ? AND is_archived = 1 ORDER BY date_applied DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    return RxRoom.createFlowable(__db, false, new String[] {"jobs"}, new Callable<List<JobEntity>>() {
      @Override
      public List<JobEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfMRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfMDateApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "date_applied");
          final int _cursorIndexOfMStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfMContactPerson = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_person");
          final int _cursorIndexOfMContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_phone");
          final int _cursorIndexOfMContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_email");
          final int _cursorIndexOfMSalaryRange = CursorUtil.getColumnIndexOrThrow(_cursor, "salary_range");
          final int _cursorIndexOfMArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfMUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfMCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfMUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<JobEntity> _result = new ArrayList<JobEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JobEntity _item;
            _item = new JobEntity();
            final String _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getString(_cursorIndexOfMId);
            }
            _item.setId(_tmpMId);
            final String _tmpMCompany;
            if (_cursor.isNull(_cursorIndexOfMCompany)) {
              _tmpMCompany = null;
            } else {
              _tmpMCompany = _cursor.getString(_cursorIndexOfMCompany);
            }
            _item.setCompany(_tmpMCompany);
            final String _tmpMRole;
            if (_cursor.isNull(_cursorIndexOfMRole)) {
              _tmpMRole = null;
            } else {
              _tmpMRole = _cursor.getString(_cursorIndexOfMRole);
            }
            _item.setRole(_tmpMRole);
            final Date _tmpMDateApplied;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfMDateApplied)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfMDateApplied);
            }
            _tmpMDateApplied = Converters.fromTimestamp(_tmp);
            _item.setDateApplied(_tmpMDateApplied);
            final JobStatus _tmpMStatus;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMStatus)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfMStatus);
            }
            _tmpMStatus = Converters.fromString(_tmp_1);
            _item.setStatus(_tmpMStatus);
            final String _tmpMNotes;
            if (_cursor.isNull(_cursorIndexOfMNotes)) {
              _tmpMNotes = null;
            } else {
              _tmpMNotes = _cursor.getString(_cursorIndexOfMNotes);
            }
            _item.setNotes(_tmpMNotes);
            final String _tmpMContactPerson;
            if (_cursor.isNull(_cursorIndexOfMContactPerson)) {
              _tmpMContactPerson = null;
            } else {
              _tmpMContactPerson = _cursor.getString(_cursorIndexOfMContactPerson);
            }
            _item.setContactPerson(_tmpMContactPerson);
            final String _tmpMContactPhone;
            if (_cursor.isNull(_cursorIndexOfMContactPhone)) {
              _tmpMContactPhone = null;
            } else {
              _tmpMContactPhone = _cursor.getString(_cursorIndexOfMContactPhone);
            }
            _item.setContactPhone(_tmpMContactPhone);
            final String _tmpMContactEmail;
            if (_cursor.isNull(_cursorIndexOfMContactEmail)) {
              _tmpMContactEmail = null;
            } else {
              _tmpMContactEmail = _cursor.getString(_cursorIndexOfMContactEmail);
            }
            _item.setContactEmail(_tmpMContactEmail);
            final String _tmpMSalaryRange;
            if (_cursor.isNull(_cursorIndexOfMSalaryRange)) {
              _tmpMSalaryRange = null;
            } else {
              _tmpMSalaryRange = _cursor.getString(_cursorIndexOfMSalaryRange);
            }
            _item.setSalaryRange(_tmpMSalaryRange);
            final boolean _tmpMArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfMArchived);
            _tmpMArchived = _tmp_2 != 0;
            _item.setArchived(_tmpMArchived);
            final String _tmpMUserId;
            if (_cursor.isNull(_cursorIndexOfMUserId)) {
              _tmpMUserId = null;
            } else {
              _tmpMUserId = _cursor.getString(_cursorIndexOfMUserId);
            }
            _item.setUserId(_tmpMUserId);
            final Date _tmpMCreatedAt;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfMCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfMCreatedAt);
            }
            _tmpMCreatedAt = Converters.fromTimestamp(_tmp_3);
            _item.setCreatedAt(_tmpMCreatedAt);
            final Date _tmpMUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfMUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfMUpdatedAt);
            }
            _tmpMUpdatedAt = Converters.fromTimestamp(_tmp_4);
            _item.setUpdatedAt(_tmpMUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Single<JobEntity> getJobById(final String jobId) {
    final String _sql = "SELECT * FROM jobs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (jobId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, jobId);
    }
    return RxRoom.createSingle(new Callable<JobEntity>() {
      @Override
      @Nullable
      public JobEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfMRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfMDateApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "date_applied");
          final int _cursorIndexOfMStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfMContactPerson = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_person");
          final int _cursorIndexOfMContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_phone");
          final int _cursorIndexOfMContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_email");
          final int _cursorIndexOfMSalaryRange = CursorUtil.getColumnIndexOrThrow(_cursor, "salary_range");
          final int _cursorIndexOfMArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfMUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfMCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfMUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final JobEntity _result;
          if (_cursor.moveToFirst()) {
            _result = new JobEntity();
            final String _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getString(_cursorIndexOfMId);
            }
            _result.setId(_tmpMId);
            final String _tmpMCompany;
            if (_cursor.isNull(_cursorIndexOfMCompany)) {
              _tmpMCompany = null;
            } else {
              _tmpMCompany = _cursor.getString(_cursorIndexOfMCompany);
            }
            _result.setCompany(_tmpMCompany);
            final String _tmpMRole;
            if (_cursor.isNull(_cursorIndexOfMRole)) {
              _tmpMRole = null;
            } else {
              _tmpMRole = _cursor.getString(_cursorIndexOfMRole);
            }
            _result.setRole(_tmpMRole);
            final Date _tmpMDateApplied;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfMDateApplied)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfMDateApplied);
            }
            _tmpMDateApplied = Converters.fromTimestamp(_tmp);
            _result.setDateApplied(_tmpMDateApplied);
            final JobStatus _tmpMStatus;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMStatus)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfMStatus);
            }
            _tmpMStatus = Converters.fromString(_tmp_1);
            _result.setStatus(_tmpMStatus);
            final String _tmpMNotes;
            if (_cursor.isNull(_cursorIndexOfMNotes)) {
              _tmpMNotes = null;
            } else {
              _tmpMNotes = _cursor.getString(_cursorIndexOfMNotes);
            }
            _result.setNotes(_tmpMNotes);
            final String _tmpMContactPerson;
            if (_cursor.isNull(_cursorIndexOfMContactPerson)) {
              _tmpMContactPerson = null;
            } else {
              _tmpMContactPerson = _cursor.getString(_cursorIndexOfMContactPerson);
            }
            _result.setContactPerson(_tmpMContactPerson);
            final String _tmpMContactPhone;
            if (_cursor.isNull(_cursorIndexOfMContactPhone)) {
              _tmpMContactPhone = null;
            } else {
              _tmpMContactPhone = _cursor.getString(_cursorIndexOfMContactPhone);
            }
            _result.setContactPhone(_tmpMContactPhone);
            final String _tmpMContactEmail;
            if (_cursor.isNull(_cursorIndexOfMContactEmail)) {
              _tmpMContactEmail = null;
            } else {
              _tmpMContactEmail = _cursor.getString(_cursorIndexOfMContactEmail);
            }
            _result.setContactEmail(_tmpMContactEmail);
            final String _tmpMSalaryRange;
            if (_cursor.isNull(_cursorIndexOfMSalaryRange)) {
              _tmpMSalaryRange = null;
            } else {
              _tmpMSalaryRange = _cursor.getString(_cursorIndexOfMSalaryRange);
            }
            _result.setSalaryRange(_tmpMSalaryRange);
            final boolean _tmpMArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfMArchived);
            _tmpMArchived = _tmp_2 != 0;
            _result.setArchived(_tmpMArchived);
            final String _tmpMUserId;
            if (_cursor.isNull(_cursorIndexOfMUserId)) {
              _tmpMUserId = null;
            } else {
              _tmpMUserId = _cursor.getString(_cursorIndexOfMUserId);
            }
            _result.setUserId(_tmpMUserId);
            final Date _tmpMCreatedAt;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfMCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfMCreatedAt);
            }
            _tmpMCreatedAt = Converters.fromTimestamp(_tmp_3);
            _result.setCreatedAt(_tmpMCreatedAt);
            final Date _tmpMUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfMUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfMUpdatedAt);
            }
            _tmpMUpdatedAt = Converters.fromTimestamp(_tmp_4);
            _result.setUpdatedAt(_tmpMUpdatedAt);
          } else {
            _result = null;
          }
          if (_result == null) {
            throw new EmptyResultSetException("Query returned empty result set: " + _statement.getSql());
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flowable<List<JobEntity>> getJobsByStatus(final String userId, final JobStatus status) {
    final String _sql = "SELECT * FROM jobs WHERE user_id = ? AND status = ? AND is_archived = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    _argIndex = 2;
    final String _tmp = Converters.jobStatusToString(status);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    return RxRoom.createFlowable(__db, false, new String[] {"jobs"}, new Callable<List<JobEntity>>() {
      @Override
      public List<JobEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfMRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfMDateApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "date_applied");
          final int _cursorIndexOfMStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfMContactPerson = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_person");
          final int _cursorIndexOfMContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_phone");
          final int _cursorIndexOfMContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_email");
          final int _cursorIndexOfMSalaryRange = CursorUtil.getColumnIndexOrThrow(_cursor, "salary_range");
          final int _cursorIndexOfMArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfMUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfMCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfMUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<JobEntity> _result = new ArrayList<JobEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JobEntity _item;
            _item = new JobEntity();
            final String _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getString(_cursorIndexOfMId);
            }
            _item.setId(_tmpMId);
            final String _tmpMCompany;
            if (_cursor.isNull(_cursorIndexOfMCompany)) {
              _tmpMCompany = null;
            } else {
              _tmpMCompany = _cursor.getString(_cursorIndexOfMCompany);
            }
            _item.setCompany(_tmpMCompany);
            final String _tmpMRole;
            if (_cursor.isNull(_cursorIndexOfMRole)) {
              _tmpMRole = null;
            } else {
              _tmpMRole = _cursor.getString(_cursorIndexOfMRole);
            }
            _item.setRole(_tmpMRole);
            final Date _tmpMDateApplied;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMDateApplied)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfMDateApplied);
            }
            _tmpMDateApplied = Converters.fromTimestamp(_tmp_1);
            _item.setDateApplied(_tmpMDateApplied);
            final JobStatus _tmpMStatus;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfMStatus)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfMStatus);
            }
            _tmpMStatus = Converters.fromString(_tmp_2);
            _item.setStatus(_tmpMStatus);
            final String _tmpMNotes;
            if (_cursor.isNull(_cursorIndexOfMNotes)) {
              _tmpMNotes = null;
            } else {
              _tmpMNotes = _cursor.getString(_cursorIndexOfMNotes);
            }
            _item.setNotes(_tmpMNotes);
            final String _tmpMContactPerson;
            if (_cursor.isNull(_cursorIndexOfMContactPerson)) {
              _tmpMContactPerson = null;
            } else {
              _tmpMContactPerson = _cursor.getString(_cursorIndexOfMContactPerson);
            }
            _item.setContactPerson(_tmpMContactPerson);
            final String _tmpMContactPhone;
            if (_cursor.isNull(_cursorIndexOfMContactPhone)) {
              _tmpMContactPhone = null;
            } else {
              _tmpMContactPhone = _cursor.getString(_cursorIndexOfMContactPhone);
            }
            _item.setContactPhone(_tmpMContactPhone);
            final String _tmpMContactEmail;
            if (_cursor.isNull(_cursorIndexOfMContactEmail)) {
              _tmpMContactEmail = null;
            } else {
              _tmpMContactEmail = _cursor.getString(_cursorIndexOfMContactEmail);
            }
            _item.setContactEmail(_tmpMContactEmail);
            final String _tmpMSalaryRange;
            if (_cursor.isNull(_cursorIndexOfMSalaryRange)) {
              _tmpMSalaryRange = null;
            } else {
              _tmpMSalaryRange = _cursor.getString(_cursorIndexOfMSalaryRange);
            }
            _item.setSalaryRange(_tmpMSalaryRange);
            final boolean _tmpMArchived;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfMArchived);
            _tmpMArchived = _tmp_3 != 0;
            _item.setArchived(_tmpMArchived);
            final String _tmpMUserId;
            if (_cursor.isNull(_cursorIndexOfMUserId)) {
              _tmpMUserId = null;
            } else {
              _tmpMUserId = _cursor.getString(_cursorIndexOfMUserId);
            }
            _item.setUserId(_tmpMUserId);
            final Date _tmpMCreatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfMCreatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfMCreatedAt);
            }
            _tmpMCreatedAt = Converters.fromTimestamp(_tmp_4);
            _item.setCreatedAt(_tmpMCreatedAt);
            final Date _tmpMUpdatedAt;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfMUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfMUpdatedAt);
            }
            _tmpMUpdatedAt = Converters.fromTimestamp(_tmp_5);
            _item.setUpdatedAt(_tmpMUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flowable<List<JobEntity>> searchJobs(final String userId, final String query) {
    final String _sql = "SELECT * FROM jobs WHERE user_id = ? AND (company LIKE '%' || ? || '%' OR role LIKE '%' || ? || '%') AND is_archived = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    _argIndex = 2;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    _argIndex = 3;
    if (query == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, query);
    }
    return RxRoom.createFlowable(__db, false, new String[] {"jobs"}, new Callable<List<JobEntity>>() {
      @Override
      public List<JobEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfMRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfMDateApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "date_applied");
          final int _cursorIndexOfMStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfMContactPerson = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_person");
          final int _cursorIndexOfMContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_phone");
          final int _cursorIndexOfMContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_email");
          final int _cursorIndexOfMSalaryRange = CursorUtil.getColumnIndexOrThrow(_cursor, "salary_range");
          final int _cursorIndexOfMArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfMUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfMCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfMUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<JobEntity> _result = new ArrayList<JobEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JobEntity _item;
            _item = new JobEntity();
            final String _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getString(_cursorIndexOfMId);
            }
            _item.setId(_tmpMId);
            final String _tmpMCompany;
            if (_cursor.isNull(_cursorIndexOfMCompany)) {
              _tmpMCompany = null;
            } else {
              _tmpMCompany = _cursor.getString(_cursorIndexOfMCompany);
            }
            _item.setCompany(_tmpMCompany);
            final String _tmpMRole;
            if (_cursor.isNull(_cursorIndexOfMRole)) {
              _tmpMRole = null;
            } else {
              _tmpMRole = _cursor.getString(_cursorIndexOfMRole);
            }
            _item.setRole(_tmpMRole);
            final Date _tmpMDateApplied;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfMDateApplied)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfMDateApplied);
            }
            _tmpMDateApplied = Converters.fromTimestamp(_tmp);
            _item.setDateApplied(_tmpMDateApplied);
            final JobStatus _tmpMStatus;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMStatus)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfMStatus);
            }
            _tmpMStatus = Converters.fromString(_tmp_1);
            _item.setStatus(_tmpMStatus);
            final String _tmpMNotes;
            if (_cursor.isNull(_cursorIndexOfMNotes)) {
              _tmpMNotes = null;
            } else {
              _tmpMNotes = _cursor.getString(_cursorIndexOfMNotes);
            }
            _item.setNotes(_tmpMNotes);
            final String _tmpMContactPerson;
            if (_cursor.isNull(_cursorIndexOfMContactPerson)) {
              _tmpMContactPerson = null;
            } else {
              _tmpMContactPerson = _cursor.getString(_cursorIndexOfMContactPerson);
            }
            _item.setContactPerson(_tmpMContactPerson);
            final String _tmpMContactPhone;
            if (_cursor.isNull(_cursorIndexOfMContactPhone)) {
              _tmpMContactPhone = null;
            } else {
              _tmpMContactPhone = _cursor.getString(_cursorIndexOfMContactPhone);
            }
            _item.setContactPhone(_tmpMContactPhone);
            final String _tmpMContactEmail;
            if (_cursor.isNull(_cursorIndexOfMContactEmail)) {
              _tmpMContactEmail = null;
            } else {
              _tmpMContactEmail = _cursor.getString(_cursorIndexOfMContactEmail);
            }
            _item.setContactEmail(_tmpMContactEmail);
            final String _tmpMSalaryRange;
            if (_cursor.isNull(_cursorIndexOfMSalaryRange)) {
              _tmpMSalaryRange = null;
            } else {
              _tmpMSalaryRange = _cursor.getString(_cursorIndexOfMSalaryRange);
            }
            _item.setSalaryRange(_tmpMSalaryRange);
            final boolean _tmpMArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfMArchived);
            _tmpMArchived = _tmp_2 != 0;
            _item.setArchived(_tmpMArchived);
            final String _tmpMUserId;
            if (_cursor.isNull(_cursorIndexOfMUserId)) {
              _tmpMUserId = null;
            } else {
              _tmpMUserId = _cursor.getString(_cursorIndexOfMUserId);
            }
            _item.setUserId(_tmpMUserId);
            final Date _tmpMCreatedAt;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfMCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfMCreatedAt);
            }
            _tmpMCreatedAt = Converters.fromTimestamp(_tmp_3);
            _item.setCreatedAt(_tmpMCreatedAt);
            final Date _tmpMUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfMUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfMUpdatedAt);
            }
            _tmpMUpdatedAt = Converters.fromTimestamp(_tmp_4);
            _item.setUpdatedAt(_tmpMUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Single<Integer> getTotalCount(final String userId) {
    final String _sql = "SELECT COUNT(*) FROM jobs WHERE user_id = ? AND is_archived = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    return RxRoom.createSingle(new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          if (_result == null) {
            throw new EmptyResultSetException("Query returned empty result set: " + _statement.getSql());
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Single<Integer> getCountByStatus(final String userId, final JobStatus status) {
    final String _sql = "SELECT COUNT(*) FROM jobs WHERE user_id = ? AND status = ? AND is_archived = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    _argIndex = 2;
    final String _tmp = Converters.jobStatusToString(status);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    return RxRoom.createSingle(new Callable<Integer>() {
      @Override
      @Nullable
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp_1;
            if (_cursor.isNull(0)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getInt(0);
            }
            _result = _tmp_1;
          } else {
            _result = null;
          }
          if (_result == null) {
            throw new EmptyResultSetException("Query returned empty result set: " + _statement.getSql());
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Single<List<JobEntity>> getStalePendingJobs(final String userId, final Date cutoffDate) {
    final String _sql = "SELECT * FROM jobs WHERE user_id = ? AND status = 'APPLIED' AND date_applied < ? AND is_archived = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    _argIndex = 2;
    final Long _tmp = Converters.dateToTimestamp(cutoffDate);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    return RxRoom.createSingle(new Callable<List<JobEntity>>() {
      @Override
      @Nullable
      public List<JobEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfMRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfMDateApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "date_applied");
          final int _cursorIndexOfMStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfMContactPerson = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_person");
          final int _cursorIndexOfMContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_phone");
          final int _cursorIndexOfMContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_email");
          final int _cursorIndexOfMSalaryRange = CursorUtil.getColumnIndexOrThrow(_cursor, "salary_range");
          final int _cursorIndexOfMArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfMUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfMCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfMUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<JobEntity> _result = new ArrayList<JobEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JobEntity _item;
            _item = new JobEntity();
            final String _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getString(_cursorIndexOfMId);
            }
            _item.setId(_tmpMId);
            final String _tmpMCompany;
            if (_cursor.isNull(_cursorIndexOfMCompany)) {
              _tmpMCompany = null;
            } else {
              _tmpMCompany = _cursor.getString(_cursorIndexOfMCompany);
            }
            _item.setCompany(_tmpMCompany);
            final String _tmpMRole;
            if (_cursor.isNull(_cursorIndexOfMRole)) {
              _tmpMRole = null;
            } else {
              _tmpMRole = _cursor.getString(_cursorIndexOfMRole);
            }
            _item.setRole(_tmpMRole);
            final Date _tmpMDateApplied;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMDateApplied)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfMDateApplied);
            }
            _tmpMDateApplied = Converters.fromTimestamp(_tmp_1);
            _item.setDateApplied(_tmpMDateApplied);
            final JobStatus _tmpMStatus;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfMStatus)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfMStatus);
            }
            _tmpMStatus = Converters.fromString(_tmp_2);
            _item.setStatus(_tmpMStatus);
            final String _tmpMNotes;
            if (_cursor.isNull(_cursorIndexOfMNotes)) {
              _tmpMNotes = null;
            } else {
              _tmpMNotes = _cursor.getString(_cursorIndexOfMNotes);
            }
            _item.setNotes(_tmpMNotes);
            final String _tmpMContactPerson;
            if (_cursor.isNull(_cursorIndexOfMContactPerson)) {
              _tmpMContactPerson = null;
            } else {
              _tmpMContactPerson = _cursor.getString(_cursorIndexOfMContactPerson);
            }
            _item.setContactPerson(_tmpMContactPerson);
            final String _tmpMContactPhone;
            if (_cursor.isNull(_cursorIndexOfMContactPhone)) {
              _tmpMContactPhone = null;
            } else {
              _tmpMContactPhone = _cursor.getString(_cursorIndexOfMContactPhone);
            }
            _item.setContactPhone(_tmpMContactPhone);
            final String _tmpMContactEmail;
            if (_cursor.isNull(_cursorIndexOfMContactEmail)) {
              _tmpMContactEmail = null;
            } else {
              _tmpMContactEmail = _cursor.getString(_cursorIndexOfMContactEmail);
            }
            _item.setContactEmail(_tmpMContactEmail);
            final String _tmpMSalaryRange;
            if (_cursor.isNull(_cursorIndexOfMSalaryRange)) {
              _tmpMSalaryRange = null;
            } else {
              _tmpMSalaryRange = _cursor.getString(_cursorIndexOfMSalaryRange);
            }
            _item.setSalaryRange(_tmpMSalaryRange);
            final boolean _tmpMArchived;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfMArchived);
            _tmpMArchived = _tmp_3 != 0;
            _item.setArchived(_tmpMArchived);
            final String _tmpMUserId;
            if (_cursor.isNull(_cursorIndexOfMUserId)) {
              _tmpMUserId = null;
            } else {
              _tmpMUserId = _cursor.getString(_cursorIndexOfMUserId);
            }
            _item.setUserId(_tmpMUserId);
            final Date _tmpMCreatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfMCreatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfMCreatedAt);
            }
            _tmpMCreatedAt = Converters.fromTimestamp(_tmp_4);
            _item.setCreatedAt(_tmpMCreatedAt);
            final Date _tmpMUpdatedAt;
            final Long _tmp_5;
            if (_cursor.isNull(_cursorIndexOfMUpdatedAt)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getLong(_cursorIndexOfMUpdatedAt);
            }
            _tmpMUpdatedAt = Converters.fromTimestamp(_tmp_5);
            _item.setUpdatedAt(_tmpMUpdatedAt);
            _result.add(_item);
          }
          if (_result == null) {
            throw new EmptyResultSetException("Query returned empty result set: " + _statement.getSql());
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flowable<List<JobEntity>> getAllJobsSortedByCompany(final String userId) {
    final String _sql = "SELECT * FROM jobs WHERE user_id = ? AND is_archived = 0 ORDER BY company ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    return RxRoom.createFlowable(__db, false, new String[] {"jobs"}, new Callable<List<JobEntity>>() {
      @Override
      public List<JobEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfMRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfMDateApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "date_applied");
          final int _cursorIndexOfMStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfMContactPerson = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_person");
          final int _cursorIndexOfMContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_phone");
          final int _cursorIndexOfMContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_email");
          final int _cursorIndexOfMSalaryRange = CursorUtil.getColumnIndexOrThrow(_cursor, "salary_range");
          final int _cursorIndexOfMArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfMUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfMCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfMUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<JobEntity> _result = new ArrayList<JobEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JobEntity _item;
            _item = new JobEntity();
            final String _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getString(_cursorIndexOfMId);
            }
            _item.setId(_tmpMId);
            final String _tmpMCompany;
            if (_cursor.isNull(_cursorIndexOfMCompany)) {
              _tmpMCompany = null;
            } else {
              _tmpMCompany = _cursor.getString(_cursorIndexOfMCompany);
            }
            _item.setCompany(_tmpMCompany);
            final String _tmpMRole;
            if (_cursor.isNull(_cursorIndexOfMRole)) {
              _tmpMRole = null;
            } else {
              _tmpMRole = _cursor.getString(_cursorIndexOfMRole);
            }
            _item.setRole(_tmpMRole);
            final Date _tmpMDateApplied;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfMDateApplied)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfMDateApplied);
            }
            _tmpMDateApplied = Converters.fromTimestamp(_tmp);
            _item.setDateApplied(_tmpMDateApplied);
            final JobStatus _tmpMStatus;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMStatus)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfMStatus);
            }
            _tmpMStatus = Converters.fromString(_tmp_1);
            _item.setStatus(_tmpMStatus);
            final String _tmpMNotes;
            if (_cursor.isNull(_cursorIndexOfMNotes)) {
              _tmpMNotes = null;
            } else {
              _tmpMNotes = _cursor.getString(_cursorIndexOfMNotes);
            }
            _item.setNotes(_tmpMNotes);
            final String _tmpMContactPerson;
            if (_cursor.isNull(_cursorIndexOfMContactPerson)) {
              _tmpMContactPerson = null;
            } else {
              _tmpMContactPerson = _cursor.getString(_cursorIndexOfMContactPerson);
            }
            _item.setContactPerson(_tmpMContactPerson);
            final String _tmpMContactPhone;
            if (_cursor.isNull(_cursorIndexOfMContactPhone)) {
              _tmpMContactPhone = null;
            } else {
              _tmpMContactPhone = _cursor.getString(_cursorIndexOfMContactPhone);
            }
            _item.setContactPhone(_tmpMContactPhone);
            final String _tmpMContactEmail;
            if (_cursor.isNull(_cursorIndexOfMContactEmail)) {
              _tmpMContactEmail = null;
            } else {
              _tmpMContactEmail = _cursor.getString(_cursorIndexOfMContactEmail);
            }
            _item.setContactEmail(_tmpMContactEmail);
            final String _tmpMSalaryRange;
            if (_cursor.isNull(_cursorIndexOfMSalaryRange)) {
              _tmpMSalaryRange = null;
            } else {
              _tmpMSalaryRange = _cursor.getString(_cursorIndexOfMSalaryRange);
            }
            _item.setSalaryRange(_tmpMSalaryRange);
            final boolean _tmpMArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfMArchived);
            _tmpMArchived = _tmp_2 != 0;
            _item.setArchived(_tmpMArchived);
            final String _tmpMUserId;
            if (_cursor.isNull(_cursorIndexOfMUserId)) {
              _tmpMUserId = null;
            } else {
              _tmpMUserId = _cursor.getString(_cursorIndexOfMUserId);
            }
            _item.setUserId(_tmpMUserId);
            final Date _tmpMCreatedAt;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfMCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfMCreatedAt);
            }
            _tmpMCreatedAt = Converters.fromTimestamp(_tmp_3);
            _item.setCreatedAt(_tmpMCreatedAt);
            final Date _tmpMUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfMUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfMUpdatedAt);
            }
            _tmpMUpdatedAt = Converters.fromTimestamp(_tmp_4);
            _item.setUpdatedAt(_tmpMUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flowable<List<JobEntity>> getAllJobsSortedByStatus(final String userId) {
    final String _sql = "SELECT * FROM jobs WHERE user_id = ? AND is_archived = 0 ORDER BY status ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    return RxRoom.createFlowable(__db, false, new String[] {"jobs"}, new Callable<List<JobEntity>>() {
      @Override
      public List<JobEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfMRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfMDateApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "date_applied");
          final int _cursorIndexOfMStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfMContactPerson = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_person");
          final int _cursorIndexOfMContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_phone");
          final int _cursorIndexOfMContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_email");
          final int _cursorIndexOfMSalaryRange = CursorUtil.getColumnIndexOrThrow(_cursor, "salary_range");
          final int _cursorIndexOfMArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfMUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfMCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfMUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<JobEntity> _result = new ArrayList<JobEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JobEntity _item;
            _item = new JobEntity();
            final String _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getString(_cursorIndexOfMId);
            }
            _item.setId(_tmpMId);
            final String _tmpMCompany;
            if (_cursor.isNull(_cursorIndexOfMCompany)) {
              _tmpMCompany = null;
            } else {
              _tmpMCompany = _cursor.getString(_cursorIndexOfMCompany);
            }
            _item.setCompany(_tmpMCompany);
            final String _tmpMRole;
            if (_cursor.isNull(_cursorIndexOfMRole)) {
              _tmpMRole = null;
            } else {
              _tmpMRole = _cursor.getString(_cursorIndexOfMRole);
            }
            _item.setRole(_tmpMRole);
            final Date _tmpMDateApplied;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfMDateApplied)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfMDateApplied);
            }
            _tmpMDateApplied = Converters.fromTimestamp(_tmp);
            _item.setDateApplied(_tmpMDateApplied);
            final JobStatus _tmpMStatus;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMStatus)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfMStatus);
            }
            _tmpMStatus = Converters.fromString(_tmp_1);
            _item.setStatus(_tmpMStatus);
            final String _tmpMNotes;
            if (_cursor.isNull(_cursorIndexOfMNotes)) {
              _tmpMNotes = null;
            } else {
              _tmpMNotes = _cursor.getString(_cursorIndexOfMNotes);
            }
            _item.setNotes(_tmpMNotes);
            final String _tmpMContactPerson;
            if (_cursor.isNull(_cursorIndexOfMContactPerson)) {
              _tmpMContactPerson = null;
            } else {
              _tmpMContactPerson = _cursor.getString(_cursorIndexOfMContactPerson);
            }
            _item.setContactPerson(_tmpMContactPerson);
            final String _tmpMContactPhone;
            if (_cursor.isNull(_cursorIndexOfMContactPhone)) {
              _tmpMContactPhone = null;
            } else {
              _tmpMContactPhone = _cursor.getString(_cursorIndexOfMContactPhone);
            }
            _item.setContactPhone(_tmpMContactPhone);
            final String _tmpMContactEmail;
            if (_cursor.isNull(_cursorIndexOfMContactEmail)) {
              _tmpMContactEmail = null;
            } else {
              _tmpMContactEmail = _cursor.getString(_cursorIndexOfMContactEmail);
            }
            _item.setContactEmail(_tmpMContactEmail);
            final String _tmpMSalaryRange;
            if (_cursor.isNull(_cursorIndexOfMSalaryRange)) {
              _tmpMSalaryRange = null;
            } else {
              _tmpMSalaryRange = _cursor.getString(_cursorIndexOfMSalaryRange);
            }
            _item.setSalaryRange(_tmpMSalaryRange);
            final boolean _tmpMArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfMArchived);
            _tmpMArchived = _tmp_2 != 0;
            _item.setArchived(_tmpMArchived);
            final String _tmpMUserId;
            if (_cursor.isNull(_cursorIndexOfMUserId)) {
              _tmpMUserId = null;
            } else {
              _tmpMUserId = _cursor.getString(_cursorIndexOfMUserId);
            }
            _item.setUserId(_tmpMUserId);
            final Date _tmpMCreatedAt;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfMCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfMCreatedAt);
            }
            _tmpMCreatedAt = Converters.fromTimestamp(_tmp_3);
            _item.setCreatedAt(_tmpMCreatedAt);
            final Date _tmpMUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfMUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfMUpdatedAt);
            }
            _tmpMUpdatedAt = Converters.fromTimestamp(_tmp_4);
            _item.setUpdatedAt(_tmpMUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Single<List<JobEntity>> getAllJobsOnce(final String userId) {
    final String _sql = "SELECT * FROM jobs WHERE user_id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (userId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, userId);
    }
    return RxRoom.createSingle(new Callable<List<JobEntity>>() {
      @Override
      @Nullable
      public List<JobEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMCompany = CursorUtil.getColumnIndexOrThrow(_cursor, "company");
          final int _cursorIndexOfMRole = CursorUtil.getColumnIndexOrThrow(_cursor, "role");
          final int _cursorIndexOfMDateApplied = CursorUtil.getColumnIndexOrThrow(_cursor, "date_applied");
          final int _cursorIndexOfMStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfMNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfMContactPerson = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_person");
          final int _cursorIndexOfMContactPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_phone");
          final int _cursorIndexOfMContactEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "contact_email");
          final int _cursorIndexOfMSalaryRange = CursorUtil.getColumnIndexOrThrow(_cursor, "salary_range");
          final int _cursorIndexOfMArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "is_archived");
          final int _cursorIndexOfMUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfMCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
          final int _cursorIndexOfMUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updated_at");
          final List<JobEntity> _result = new ArrayList<JobEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final JobEntity _item;
            _item = new JobEntity();
            final String _tmpMId;
            if (_cursor.isNull(_cursorIndexOfMId)) {
              _tmpMId = null;
            } else {
              _tmpMId = _cursor.getString(_cursorIndexOfMId);
            }
            _item.setId(_tmpMId);
            final String _tmpMCompany;
            if (_cursor.isNull(_cursorIndexOfMCompany)) {
              _tmpMCompany = null;
            } else {
              _tmpMCompany = _cursor.getString(_cursorIndexOfMCompany);
            }
            _item.setCompany(_tmpMCompany);
            final String _tmpMRole;
            if (_cursor.isNull(_cursorIndexOfMRole)) {
              _tmpMRole = null;
            } else {
              _tmpMRole = _cursor.getString(_cursorIndexOfMRole);
            }
            _item.setRole(_tmpMRole);
            final Date _tmpMDateApplied;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfMDateApplied)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfMDateApplied);
            }
            _tmpMDateApplied = Converters.fromTimestamp(_tmp);
            _item.setDateApplied(_tmpMDateApplied);
            final JobStatus _tmpMStatus;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfMStatus)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfMStatus);
            }
            _tmpMStatus = Converters.fromString(_tmp_1);
            _item.setStatus(_tmpMStatus);
            final String _tmpMNotes;
            if (_cursor.isNull(_cursorIndexOfMNotes)) {
              _tmpMNotes = null;
            } else {
              _tmpMNotes = _cursor.getString(_cursorIndexOfMNotes);
            }
            _item.setNotes(_tmpMNotes);
            final String _tmpMContactPerson;
            if (_cursor.isNull(_cursorIndexOfMContactPerson)) {
              _tmpMContactPerson = null;
            } else {
              _tmpMContactPerson = _cursor.getString(_cursorIndexOfMContactPerson);
            }
            _item.setContactPerson(_tmpMContactPerson);
            final String _tmpMContactPhone;
            if (_cursor.isNull(_cursorIndexOfMContactPhone)) {
              _tmpMContactPhone = null;
            } else {
              _tmpMContactPhone = _cursor.getString(_cursorIndexOfMContactPhone);
            }
            _item.setContactPhone(_tmpMContactPhone);
            final String _tmpMContactEmail;
            if (_cursor.isNull(_cursorIndexOfMContactEmail)) {
              _tmpMContactEmail = null;
            } else {
              _tmpMContactEmail = _cursor.getString(_cursorIndexOfMContactEmail);
            }
            _item.setContactEmail(_tmpMContactEmail);
            final String _tmpMSalaryRange;
            if (_cursor.isNull(_cursorIndexOfMSalaryRange)) {
              _tmpMSalaryRange = null;
            } else {
              _tmpMSalaryRange = _cursor.getString(_cursorIndexOfMSalaryRange);
            }
            _item.setSalaryRange(_tmpMSalaryRange);
            final boolean _tmpMArchived;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfMArchived);
            _tmpMArchived = _tmp_2 != 0;
            _item.setArchived(_tmpMArchived);
            final String _tmpMUserId;
            if (_cursor.isNull(_cursorIndexOfMUserId)) {
              _tmpMUserId = null;
            } else {
              _tmpMUserId = _cursor.getString(_cursorIndexOfMUserId);
            }
            _item.setUserId(_tmpMUserId);
            final Date _tmpMCreatedAt;
            final Long _tmp_3;
            if (_cursor.isNull(_cursorIndexOfMCreatedAt)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getLong(_cursorIndexOfMCreatedAt);
            }
            _tmpMCreatedAt = Converters.fromTimestamp(_tmp_3);
            _item.setCreatedAt(_tmpMCreatedAt);
            final Date _tmpMUpdatedAt;
            final Long _tmp_4;
            if (_cursor.isNull(_cursorIndexOfMUpdatedAt)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getLong(_cursorIndexOfMUpdatedAt);
            }
            _tmpMUpdatedAt = Converters.fromTimestamp(_tmp_4);
            _item.setUpdatedAt(_tmpMUpdatedAt);
            _result.add(_item);
          }
          if (_result == null) {
            throw new EmptyResultSetException("Query returned empty result set: " + _statement.getSql());
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
