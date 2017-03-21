package com.moblin.expansionpanelsdemo.gui;

import android.support.annotation.IdRes;
import android.support.transition.AutoTransition;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.moblin.expansionpanelsdemo.R;
import com.moblin.expansionpanelsdemo.util.Assert;

/**
 * TODO
 */
@SuppressWarnings("WeakerAccess")
public abstract class ExpansionPanelsAdapter extends RecyclerView.Adapter
        <ExpansionPanelsAdapter.ViewHolder> {

    public enum ViewHolderType {
        SUMMARY, DETAILS, ACTIONS
    }

    // TODO: use system's short animation duration
    private static final long TRANSITION_DURATION = 100L;
    private ViewGroup mSceneRoot;
    private Transition mTransition;
    private int mExpandedPosition = RecyclerView.NO_POSITION;

    /**
     * Public constructor
     * @param sceneRoot - scene's root-view
     */
    public ExpansionPanelsAdapter(ViewGroup sceneRoot) {
        mSceneRoot = sceneRoot;
        setupTransition();
    }

    /** Protected API */

    protected abstract RecyclerView.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType, ViewHolderType holderType);

    protected abstract void onBindViewHolder(RecyclerView.ViewHolder holder,
                                             int position, ViewHolderType holderType);

    /** Recycler View Adapter methods */

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.expansion_panel, parent, false);

        RecyclerView.ViewHolder summaryVH =
                onCreateViewHolder(parent, viewType, ViewHolderType.SUMMARY);

        RecyclerView.ViewHolder detailsVH =
                onCreateViewHolder(parent, viewType, ViewHolderType.DETAILS);

        RecyclerView.ViewHolder actionsVH =
                onCreateViewHolder(parent, viewType, ViewHolderType.ACTIONS);

        FrameLayout summaryContainer = lookup(view, R.id.fl_summary_container);
        Assert.notNull(summaryContainer, "View not found: fl_summary_container");
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_VERTICAL);
        summaryContainer.addView(summaryVH.itemView, lp);

        FrameLayout detailsContainer = lookup(view, R.id.fl_details_container);
        Assert.notNull(detailsContainer, "View not found: fl_details_container");
        detailsContainer.addView(detailsVH.itemView);

        FrameLayout actionsContainer = lookup(view, R.id.fl_actions_container);
        Assert.notNull(actionsContainer, "View not found: fl_actions_container");
        actionsContainer.addView(actionsVH.itemView);

        view.requestLayout();

        return new ViewHolder(view, summaryVH, detailsVH, actionsVH,
                new ItemClickListener() {
                    public void onItemClick(int position) {
                        mExpandedPosition = (mExpandedPosition == position) ?
                                RecyclerView.NO_POSITION : position;
                        TransitionManager.beginDelayedTransition(mSceneRoot, mTransition);
                        notifyDataSetChanged();
                    }
                }
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        onBindViewHolder(holder.getSummaryVH(), position, ViewHolderType.SUMMARY);
        onBindViewHolder(holder.getDetailsVH(), position, ViewHolderType.DETAILS);
        onBindViewHolder(holder.getActionsVH(), position, ViewHolderType.ACTIONS);

        int visibility = (position == mExpandedPosition) ? View.VISIBLE : View.GONE;
        holder.getDetailsContainer().setVisibility(visibility);
        holder.getActionsContainer().setVisibility(visibility);
        holder.getDivider().setVisibility(visibility);
    }

    /** Private methods */

    private void setupTransition() {
        mTransition = new AutoTransition();
        mTransition.setDuration(TRANSITION_DURATION);
        mTransition.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    private static <T extends View> T lookup(View rootView, @IdRes int viewId) {
        //noinspection unchecked
        return (T) rootView.findViewById(viewId);
    }

    private interface ItemClickListener {
        void onItemClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView.ViewHolder mSummaryVH;
        private RecyclerView.ViewHolder mDetailsVH;
        private RecyclerView.ViewHolder mActionsVH;
        private CardView mCardView;
        private ImageView mExpandIcon;
        private ViewGroup mDetailsContainer, mActionsContainer;
        private View mDivider;

        ViewHolder(View itemView,
                   RecyclerView.ViewHolder summaryViewHolder,
                   RecyclerView.ViewHolder detailsViewHolder,
                   RecyclerView.ViewHolder actionsViewHolder,
                   final ItemClickListener clickListener) {
            super(itemView);

            mSummaryVH = summaryViewHolder;
            mDetailsVH = detailsViewHolder;
            mActionsVH = actionsViewHolder;

            mCardView = lookup(itemView, R.id.cv_expansion_panel);
            Assert.notNull(mCardView, "View not found: cv_expansion_panel");
            mExpandIcon = lookup(itemView, R.id.iv_expand_icon);
            Assert.notNull(mExpandIcon, "View not found: iv_expand_icon");
            mDetailsContainer = lookup(itemView, R.id.fl_details_container);
            Assert.notNull(mDetailsContainer, "View not found: fl_details_container");
            mDivider = lookup(itemView, R.id.view_divider);
            Assert.notNull(mDivider, "View not found: view_divider");
            mActionsContainer = lookup(itemView, R.id.fl_actions_container);
            Assert.notNull(mActionsContainer, "View not found: fl_actions_container");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

        RecyclerView.ViewHolder getSummaryVH() {
            return mSummaryVH;
        }

        RecyclerView.ViewHolder getDetailsVH() {
            return mDetailsVH;
        }

        RecyclerView.ViewHolder getActionsVH() {
            return mActionsVH;
        }

        ImageView getExpandIcon() {
            return mExpandIcon;
        }

        ViewGroup getDetailsContainer() {
            return mDetailsContainer;
        }

        ViewGroup getActionsContainer() {
            return mActionsContainer;
        }

        View getDivider() {
            return mDivider;
        }
    }
}
