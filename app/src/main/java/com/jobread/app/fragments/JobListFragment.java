package com.jobread.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.jobread.app.JobReadApplication;
import com.jobread.app.R;
import com.jobread.app.activities.AddEditJobActivity;
import com.jobread.app.activities.JobDetailActivity;
import com.jobread.app.adapters.JobAdapter;
import com.jobread.app.adapters.SwipeToActionCallback;
import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.databinding.FragmentJobListBinding;
import com.jobread.app.di.ViewModelFactory;
import com.jobread.app.models.JobStatus;
import com.jobread.app.models.SortOption;
import com.jobread.app.viewmodels.JobListViewModel;

import java.util.List;

import javax.inject.Inject;

public class JobListFragment extends Fragment {

    private static final int ADD_JOB_REQUEST = 1001;
    private static final int EDIT_JOB_REQUEST = 1002;

    private FragmentJobListBinding mBinding;
    private JobListViewModel mViewModel;
    private JobAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private int mScrollPosition = 0;

    @Inject
    ViewModelFactory mViewModelFactory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((JobReadApplication) requireActivity().getApplication())
                .getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentJobListBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this, mViewModelFactory).get(JobListViewModel.class);

        setupRecyclerView();
        setupStatusFilter();
        setupFab();
        setupMenuProvider();
        observeViewModel();

        if (savedInstanceState != null) {
            mScrollPosition = savedInstanceState.getInt("scroll_position", 0);
        }

        mViewModel.loadJobs();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mLayoutManager != null) {
            outState.putInt("scroll_position",
                    mLayoutManager.findFirstVisibleItemPosition());
        }
    }

    private void setupRecyclerView() {
        mAdapter = new JobAdapter();
        mLayoutManager = new LinearLayoutManager(requireContext());
        mBinding.recyclerView.setLayoutManager(mLayoutManager);
        mBinding.recyclerView.setAdapter(mAdapter);

        mAdapter.setClickListener((job, sharedView) -> {
            Intent intent = new Intent(requireContext(), JobDetailActivity.class);
            intent.putExtra(JobDetailActivity.EXTRA_JOB_ID, job.getId());
            intent.putExtra(JobDetailActivity.EXTRA_TRANSITION_NAME,
                    sharedView.getTransitionName());

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    requireActivity(), sharedView, sharedView.getTransitionName());
            startActivityForResult(intent, EDIT_JOB_REQUEST, options.toBundle());
        });

        mAdapter.setLongClickListener((job, anchorView) -> showContextMenu(job, anchorView));

        SwipeToActionCallback swipeCallback = new SwipeToActionCallback(
                requireContext(),
                new SwipeToActionCallback.OnSwipeListener() {
                    @Override
                    public void onSwipeDelete(int position) {
                        JobEntity job = mAdapter.getJobAt(position);
                        mViewModel.deleteJob(job);
                        showUndoSnackbar("Job deleted", job, false);
                    }

                    @Override
                    public void onSwipeArchive(int position) {
                        JobEntity job = mAdapter.getJobAt(position);
                        mViewModel.archiveJob(job.getId(), true);
                        showUndoSnackbar("Job archived", job, true);
                    }
                }
        );
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(mBinding.recyclerView);
    }

    private void setupStatusFilter() {
        String[] filterOptions = {"All", "Applied", "Interview Scheduled",
                "Offer Received", "Rejected", "Accepted"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, filterOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.spinnerFilter.setAdapter(spinnerAdapter);

        mBinding.spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mViewModel.setFilter(null);
                } else {
                    mViewModel.setFilter(JobStatus.values()[position - 1]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupFab() {
        mBinding.fabAddJob.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddEditJobActivity.class);
            startActivityForResult(intent, ADD_JOB_REQUEST);
        });
    }

    private void setupMenuProvider() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.job_list_menu, menu);

                MenuItem searchItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) searchItem.getActionView();
                if (searchView != null) {
                    searchView.setQueryHint("Search company or role...");
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            mViewModel.setSearchQuery(query);
                            return true;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            mViewModel.setSearchQuery(newText);
                            return true;
                        }
                    });
                    searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionExpand(MenuItem item) { return true; }

                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            mViewModel.setSearchQuery("");
                            return true;
                        }
                    });
                }
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.action_sort_date) {
                    mViewModel.setSort(SortOption.DATE_NEWEST);
                    return true;
                } else if (id == R.id.action_sort_company) {
                    mViewModel.setSort(SortOption.COMPANY_AZ);
                    return true;
                } else if (id == R.id.action_sort_status) {
                    mViewModel.setSort(SortOption.STATUS);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void observeViewModel() {
        mViewModel.getJobs().observe(getViewLifecycleOwner(), jobs -> {
            if (jobs != null) {
                updateJobList(jobs);
            }
        });

        mViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Snackbar.make(mBinding.getRoot(), error, Snackbar.LENGTH_LONG).show();
            }
        });

        mViewModel.getOperationSuccess().observe(getViewLifecycleOwner(), message -> {
            if (message != null) {
                Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void updateJobList(List<JobEntity> jobs) {
        mAdapter.submitList(jobs);

        if (jobs.isEmpty()) {
            mBinding.layoutEmpty.setVisibility(View.VISIBLE);
            mBinding.recyclerView.setVisibility(View.GONE);
        } else {
            mBinding.layoutEmpty.setVisibility(View.GONE);
            mBinding.recyclerView.setVisibility(View.VISIBLE);
            // Restore scroll position
            if (mScrollPosition > 0) {
                mLayoutManager.scrollToPosition(mScrollPosition);
                mScrollPosition = 0;
            }
        }
    }

    private void showContextMenu(JobEntity job, View anchorView) {
        String[] options = {"Edit", "Delete", "Archive", "Duplicate"};
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(job.getCompany() + " - " + job.getRole())
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: editJob(job); break;
                        case 1: confirmDelete(job); break;
                        case 2: mViewModel.archiveJob(job.getId(), !job.isArchived()); break;
                        case 3: mViewModel.duplicateJob(job); break;
                    }
                })
                .show();
    }

    private void editJob(JobEntity job) {
        Intent intent = new Intent(requireContext(), AddEditJobActivity.class);
        intent.putExtra(AddEditJobActivity.EXTRA_JOB_ID, job.getId());
        startActivityForResult(intent, EDIT_JOB_REQUEST);
    }

    private void confirmDelete(JobEntity job) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Job")
                .setMessage("Delete " + job.getCompany() + " - " + job.getRole() + "?")
                .setPositiveButton("Delete", (d, w) -> mViewModel.deleteJob(job))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showUndoSnackbar(String message, JobEntity job, boolean wasArchive) {
        Snackbar.make(mBinding.getRoot(), message, Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> {
                    if (wasArchive) {
                        mViewModel.archiveJob(job.getId(), false);
                    } else {
                        mViewModel.restoreJob(job);
                    }
                })
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ADD_JOB_REQUEST || requestCode == EDIT_JOB_REQUEST)) {
            mViewModel.loadJobs();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
