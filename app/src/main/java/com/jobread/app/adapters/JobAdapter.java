package com.jobread.app.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.jobread.app.R;
import com.jobread.app.database.entities.JobEntity;
import com.jobread.app.models.JobStatus;
import com.jobread.app.utils.DateUtils;

public class JobAdapter extends ListAdapter<JobEntity, JobAdapter.JobViewHolder> {

    private OnJobClickListener mClickListener;
    private OnJobLongClickListener mLongClickListener;

    public interface OnJobClickListener {
        void onJobClick(JobEntity job, View sharedView);
    }

    public interface OnJobLongClickListener {
        void onJobLongClick(JobEntity job, View anchorView);
    }

    private static final DiffUtil.ItemCallback<JobEntity> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<JobEntity>() {
                @Override
                public boolean areItemsTheSame(@NonNull JobEntity oldItem, @NonNull JobEntity newItem) {
                    return oldItem.getId().equals(newItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull JobEntity oldItem, @NonNull JobEntity newItem) {
                    return oldItem.getId().equals(newItem.getId()) &&
                           oldItem.getCompany().equals(newItem.getCompany()) &&
                           oldItem.getRole().equals(newItem.getRole()) &&
                           oldItem.getStatus() == newItem.getStatus() &&
                           oldItem.isArchived() == newItem.isArchived();
                }
            };

    public JobAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setClickListener(OnJobClickListener listener) {
        this.mClickListener = listener;
    }

    public void setLongClickListener(OnJobLongClickListener listener) {
        this.mLongClickListener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobEntity job = getItem(position);
        holder.bind(job);

        holder.itemView.setOnClickListener(v -> {
            if (mClickListener != null) {
                mClickListener.onJobClick(job, holder.mCardView);
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (mLongClickListener != null) {
                mLongClickListener.onJobLongClick(job, v);
                return true;
            }
            return false;
        });
    }

    public JobEntity getJobAt(int position) {
        return getItem(position);
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {
        final MaterialCardView mCardView;
        final TextView mCompanyTextView;
        final TextView mRoleTextView;
        final TextView mDateTextView;
        final TextView mStatusTextView;
        final View mStatusIndicator;

        JobViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_view);
            mCompanyTextView = itemView.findViewById(R.id.tv_company);
            mRoleTextView = itemView.findViewById(R.id.tv_role);
            mDateTextView = itemView.findViewById(R.id.tv_date);
            mStatusTextView = itemView.findViewById(R.id.tv_status);
            mStatusIndicator = itemView.findViewById(R.id.status_indicator);
        }

        void bind(JobEntity job) {
            mCompanyTextView.setText(job.getCompany());
            mRoleTextView.setText(job.getRole());
            mDateTextView.setText(DateUtils.formatDisplay(job.getDateApplied()));
            mStatusTextView.setText(job.getStatus() != null ?
                    job.getStatus().getDisplayName() : "Applied");

            int statusColor = getStatusColor(job.getStatus());
            mStatusIndicator.setBackgroundColor(statusColor);
            mStatusTextView.setTextColor(statusColor);

            // Tag for shared element transition
            mCardView.setTransitionName("job_card_" + job.getId());
        }

        private int getStatusColor(JobStatus status) {
            if (status == null) return Color.parseColor("#2196F3");
            switch (status) {
                case APPLIED:           return Color.parseColor("#2196F3");
                case INTERVIEW_SCHEDULED: return Color.parseColor("#FF9800");
                case OFFER_RECEIVED:    return Color.parseColor("#4CAF50");
                case REJECTED:          return Color.parseColor("#F44336");
                case ACCEPTED:          return Color.parseColor("#9C27B0");
                default:                return Color.parseColor("#2196F3");
            }
        }
    }
}
