package com.jobread.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.snackbar.Snackbar;
import com.jobread.app.JobReadApplication;
import com.jobread.app.R;
import com.jobread.app.databinding.FragmentDashboardBinding;
import com.jobread.app.di.ViewModelFactory;
import com.jobread.app.utils.DateUtils;
import com.jobread.app.viewmodels.DashboardViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding mBinding;
    private DashboardViewModel mViewModel;

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
        mBinding = FragmentDashboardBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this, mViewModelFactory).get(DashboardViewModel.class);

        setupChart();
        setupSwipeRefresh();
        observeViewModel();

        mViewModel.loadDashboard();
    }

    private void setupChart() {
        mBinding.barChart.setDescription(null);
        mBinding.barChart.getLegend().setEnabled(false);
        mBinding.barChart.setDrawGridBackground(false);
        mBinding.barChart.getAxisRight().setEnabled(false);
        mBinding.barChart.getAxisLeft().setGranularity(1f);
        mBinding.barChart.getAxisLeft().setAxisMinimum(0f);

        XAxis xAxis = mBinding.barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
    }

    private void setupSwipeRefresh() {
        mBinding.swipeRefresh.setOnRefreshListener(() -> mViewModel.refresh());
    }

    private void observeViewModel() {
        mViewModel.getStats().observe(getViewLifecycleOwner(), stats -> {
            if (stats != null) {
                mBinding.tvTotalCount.setText(String.valueOf(stats.getTotal()));
                mBinding.tvInterviewCount.setText(String.valueOf(stats.getInterviews()));
                mBinding.tvOfferCount.setText(String.valueOf(stats.getOffers()));
                mBinding.tvRejectionCount.setText(String.valueOf(stats.getRejections()));
            }
        });

        mViewModel.getWeeklyCounts().observe(getViewLifecycleOwner(), weeklyCounts -> {
            if (weeklyCounts != null) {
                updateChart(weeklyCounts);
            }
        });

        mViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            mBinding.swipeRefresh.setRefreshing(loading);
        });

        mViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Snackbar.make(mBinding.getRoot(), error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void updateChart(List<Float> weeklyCounts) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < weeklyCounts.size(); i++) {
            entries.add(new BarEntry(i, weeklyCounts.get(i)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Applications per week");
        dataSet.setColor(0xFF6200EE);
        dataSet.setValueTextColor(0xFF000000);
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.7f);
        mBinding.barChart.setData(barData);

        // Set X axis labels
        String[] labels = new String[4];
        for (int i = 0; i < 4; i++) {
            labels[i] = DateUtils.getWeekLabel(3 - i);
        }
        mBinding.barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        mBinding.barChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
