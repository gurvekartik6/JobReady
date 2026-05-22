# JobRead — Android Job Application Tracker

A production-ready Android application (Java 17) for tracking job applications with offline-first support, notifications, CSV import/export, and a rich Material 3 UI.

---

## Tech Stack

| Layer | Library |
|-------|---------|
| Language | Java 17 |
| Architecture | MVVM + LiveData + ViewModel |
| Database | Room (RxJava3 adapter) |
| DI | Dagger 2 |
| Networking | Retrofit 2 + OkHttp + Gson |
| Async | RxJava 3 (Completable / Single / Flowable) |
| Background | WorkManager (RxWorker) |
| Auth | Firebase Auth (email/password) |
| Secure prefs | EncryptedSharedPreferences |
| Image loading | Glide |
| Charts | MPAndroidChart |
| Animations | Lottie |
| UI | Material 3, Dynamic Color |
| Testing | JUnit4 + Mockito + Espresso + Room in-memory |

---

## Project Structure

```
app/src/main/java/com/jobread/app/
├── JobReadApplication.java          # Application class, Dagger init
├── activities/
│   ├── SplashActivity.java          # Auto-login check, routing
│   ├── AuthActivity.java            # Login / Register (Firebase)
│   ├── MainActivity.java            # Bottom nav host
│   ├── AddEditJobActivity.java      # Add or edit job form
│   ├── JobDetailActivity.java       # Full details + actions
│   └── SettingsActivity.java        # Theme, notifications, CSV
├── fragments/
│   ├── DashboardFragment.java       # Stats cards + bar chart
│   └── JobListFragment.java         # RecyclerView + search/filter/sort
├── viewmodels/
│   ├── AuthViewModel.java
│   ├── DashboardViewModel.java
│   ├── JobListViewModel.java
│   ├── AddEditJobViewModel.java
│   └── JobDetailViewModel.java
├── repositories/
│   └── JobRepository.java           # Single source of truth
├── database/
│   ├── AppDatabase.java             # Room singleton
│   ├── dao/JobDao.java              # All DB queries (RxJava3)
│   ├── entities/JobEntity.java      # Room entity
│   └── converters/Converters.java   # Date ↔ Long, Enum ↔ String
├── models/
│   ├── JobStatus.java               # APPLIED, INTERVIEW_SCHEDULED, …
│   ├── DashboardStats.java
│   └── SortOption.java
├── adapters/
│   ├── JobAdapter.java              # DiffUtil RecyclerView adapter
│   └── SwipeToActionCallback.java   # Swipe left/right actions
├── di/
│   ├── AppComponent.java            # Dagger @Component
│   ├── AppModule.java               # DB, Prefs, Repo providers
│   ├── NetworkModule.java           # OkHttp, Retrofit providers
│   └── ViewModelFactory.java        # ViewModelProvider.Factory
├── workers/
│   └── FollowUpNotificationWorker.java  # Daily WorkManager job
├── notifications/
│   └── NotificationReceiver.java    # Boot receiver
└── utils/
    ├── PreferenceManager.java       # EncryptedSharedPreferences wrapper
    ├── CsvUtils.java                # CSV import/export
    ├── DateUtils.java               # Date formatting helpers
    ├── DriveBackupManager.java      # Google Drive backup/restore
    ├── NetworkUtils.java            # Connectivity check
    ├── NotificationUtils.java       # Notification builder/channels
    └── WorkManagerUtils.java        # Schedule/cancel WorkManager tasks
```

---

## Setup Instructions

### 1. Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project named **JobRead**
3. Add Android app with package `com.jobread.app`
4. Download `google-services.json`
5. Replace `app/google-services.json` with your downloaded file
6. Enable **Email/Password** authentication in Firebase Console → Auth

### 2. Google Drive API (optional, for backup feature)

1. In Firebase/Google Cloud Console, enable the **Google Drive API**
2. Create OAuth 2.0 credentials for Android
3. Add your SHA-1 fingerprint for both debug and release builds

### 3. Lottie Animations

Download free animations from [lottiefiles.com](https://lottiefiles.com):
- `res/raw/loading.json` — a spinner/loading animation
- `res/raw/empty_state.json` — an empty box or search animation

Replace the placeholder JSON files provided.

### 4. Build & Run

```bash
# Clone the project
git clone <your-repo-url>
cd JobRead

# Open in Android Studio (Hedgehog or newer)
# File → Open → select JobRead directory

# Sync Gradle dependencies (automatic on open)

# Run on emulator or device (API 24+)
# Build → Run (Shift+F10)
```

### 5. Debug Build Signing

```bash
# Generate debug SHA-1 (needed for Firebase)
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

### 6. Release Build

```bash
./gradlew assembleRelease
```

Ensure you have a keystore configured in `build.gradle` or via Android Studio.

---

## Features

### Authentication
- Email/Password sign-up and login via Firebase Auth
- Auto-login on app launch if token is still valid
- Logout clears local Room database and user prefs

### Dashboard
- Total, Interview, Offer, Rejection stat cards
- Bar chart of applications per week (last 4 weeks) via MPAndroidChart
- Pull-to-refresh updates all statistics

### Job Management
- Add, edit, delete, archive, duplicate job applications
- Fields: Company, Role, Date Applied, Status, Notes, Contact Person, Phone, Email, Salary Range
- Material TextInputLayout with validation (required fields, no future dates)
- Status change with Snackbar + Undo option

### Job List
- RecyclerView with DiffUtil for smooth updates
- Swipe right → Delete (with Undo)
- Swipe left → Archive (with Undo)
- Long-press context menu: Edit, Delete, Archive, Duplicate
- SearchView filters by company or role
- Spinner filter by status (All, Applied, Interview, Offer, Rejected, Accepted)
- Sort menu: By Date, By Company A-Z, By Status

### Notifications
- WorkManager schedules daily check at 9:00 AM (configurable)
- Notifies about `APPLIED` jobs older than 7 days
- Tapping notification opens Dashboard
- Settings screen has TimePicker for custom reminder time
- Permission requested on Android 13+

### Data Management
- Export all jobs to CSV in Downloads folder
- Import jobs from CSV (handles format and errors gracefully)
- Google Drive backup/restore (appdata scope)

### Details Screen
- All job fields displayed
- Edit job or change status inline
- Add Interview to Google Calendar
- Call Contact (opens dialer)
- Email Contact (opens mail client)
- Delete application

### Settings
- Dark / Light theme toggle (AppCompatDelegate)
- Notification enable/disable + time picker
- CSV export & import buttons

---

## Testing

```bash
# Unit tests (ViewModel + Model)
./gradlew test

# Instrumentation tests (Room DB + Espresso)
./gradlew connectedAndroidTest
```

### Test Coverage
| Test File | Coverage |
|-----------|----------|
| `JobListViewModelTest` | ViewModel with Mockito, RxJava trampoline scheduler |
| `DashboardViewModelTest` | Stats loading, error states, refresh |
| `AddEditJobViewModelTest` | Validation, insert, update, field capture |
| `JobEntityTest` | Model creation, duplication, getters/setters |
| `CsvUtilsTest` | Enum helpers, DateUtils, status mapping |
| `JobDaoTest` | In-memory Room: insert, update, delete, archive, search |
| `AddJobEspressoTest` | UI flow: form validation, field display |
| `JobListEspressoTest` | Navigation, FAB, filter spinner |

---

## Architecture Diagram

```
UI Layer (Activities / Fragments)
        │  observe LiveData
        ▼
ViewModel (MVVM)
        │  calls
        ▼
Repository (Single source of truth)
        │  RxJava3 (Flowable / Single / Completable)
        ▼
Room DAO ────────────── AppDatabase (SQLite)
```

---

## Known Limitations / Notes

- The `google-services.json` included is a **template**. Replace it with your real Firebase config before building.
- Lottie JSON files are minimal placeholders. Replace with real animations from [lottiefiles.com](https://lottiefiles.com).
- Google Drive backup requires the user to sign in via `GoogleSignIn` — implement the sign-in flow in `SettingsActivity` using `GoogleSignInClient`.
- Dagger 2 annotation processing generates `DaggerAppComponent` at build time — the project will not compile until you run a Gradle build in Android Studio.

---

## License

MIT License — free to use for personal and commercial projects.
