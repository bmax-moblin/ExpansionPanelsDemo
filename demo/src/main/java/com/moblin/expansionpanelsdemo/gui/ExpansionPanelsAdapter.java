package com.moblin.expansionpanelsdemo.gui;

import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.transition.AutoTransition;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.moblin.expansionpanelsdemo.R;
import com.moblin.expansionpanelsdemo.util.Assert;

/**
 * Adapter for RecyclerView, that displays material design expansion panels
 * with customizable content. Subclasses should create and bind view-holders
 * that supply panel's summary, details, and actions.
 */
@SuppressWarnings("WeakerAccess")
public abstract class ExpansionPanelsAdapter extends RecyclerView.Adapter
        <ExpansionPanelsAdapter.ViewHolder> {

    /**
     * Expansion panel's content is supplied by subclasses via
     * view-holders of these types.
     */
    public enum ViewHolderType {
        SUMMARY, DETAILS, ACTIONS
    }

    protected Resources mResources;
    private ViewGroup mSceneRoot;
    private Transition mTransition;
    private int mMarginCollapsed, mMarginExpanded;
    private long mTransitionDuration;
    private int mExpandedPanelPos = RecyclerView.NO_POSITION;

    /**
     * Public constructor
     * @param resources - app resources
     * @param sceneRoot - scene's root view
     */
    public ExpansionPanelsAdapter(Resources resources, ViewGroup sceneRoot) {
        mResources = resources;
        mSceneRoot = sceneRoot;
        readResourceValues(resources);
        setupTransition();
    }

    /**
     * Subclasses implement this method to supply view-holders of the given type.
     * @param parent - parent layout
     * @param viewType - view type
     * @param holderType - view-holder type
     * @param masterViewHolder - the master VH that hosts this VH (can used
     *                         to get the adapter position)
     * @return created view holder
     */
    protected abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType, ViewHolderType holderType, ViewHolder masterViewHolder);

    /**
     * Subclasses implement this method to bind view-holders of the given type.
     * @param holder - view-holder to bind
     * @param position - position in the data-set
     * @param holderType - view-holder type
     */
    protected abstract void onBindViewHolder(RecyclerView.ViewHolder holder,
                                             int position, ViewHolderType holderType);

    /**
     * Collapses all expansion panels.
     */
    protected void collapseAll() {
        mExpandedPanelPos = RecyclerView.NO_POSITION;
        TransitionManager.beginDelayedTransition(mSceneRoot, mTransition);
        notifyDataSetChanged();
    }

    /** Recycler View Adapter methods */

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.expansion_panel, parent, false);

        ViewHolder holder = new ViewHolder(view, new ItemClickListener() {
                    public void onItemClick(int position) {
                        mExpandedPanelPos = (mExpandedPanelPos == position) ?
                                RecyclerView.NO_POSITION : position;
                        TransitionManager.beginDelayedTransition(mSceneRoot, mTransition);
                        notifyDataSetChanged();
                    }
                }
        );

        // Create view-holders in the subclass.
        RecyclerView.ViewHolder summaryVH =
                onCreateViewHolder(parent, viewType, ViewHolderType.SUMMARY, holder);
        RecyclerView.ViewHolder detailsVH =
                onCreateViewHolder(parent, viewType, ViewHolderType.DETAILS, holder);
        RecyclerView.ViewHolder actionsVH =
                onCreateViewHolder(parent, viewType, ViewHolderType.ACTIONS, holder);

        // Store them in the master view-holder for later.
        holder.setSummaryVH(summaryVH);
        holder.setDetailsVH(detailsVH);
        holder.setActionsVH(actionsVH);

        // Embed the summary view.
        FrameLayout summaryContainer = lookup(view, R.id.fl_summary_container);
        Assert.notNull(summaryContainer, "View not found: fl_summary_container");
        summaryContainer.addView(summaryVH.itemView);

        // Embed the details view.
        FrameLayout detailsContainer = lookup(view, R.id.fl_details_container);
        Assert.notNull(detailsContainer, "View not found: fl_details_container");
        detailsContainer.addView(detailsVH.itemView);

        // Embed the actions view.
        FrameLayout actionsContainer = lookup(view, R.id.fl_actions_container);
        Assert.notNull(actionsContainer, "View not found: fl_actions_container");
        actionsContainer.addView(actionsVH.itemView);

        // Remeasure the size
        view.requestLayout();

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Bind view-holders in the subclass.
        onBindViewHolder(holder.getSummaryVH(), position, ViewHolderType.SUMMARY);
        onBindViewHolder(holder.getDetailsVH(), position, ViewHolderType.DETAILS);
        onBindViewHolder(holder.getActionsVH(), position, ViewHolderType.ACTIONS);

        boolean expanded = position == mExpandedPanelPos;

        // Show the details and actions only in expanded panels.
        int visibility = expanded ? View.VISIBLE : View.GONE;
        holder.getDetailsContainer().setVisibility(visibility);
        holder.getActionsContainer().setVisibility(visibility);
        holder.getDivider().setVisibility(visibility);
        holder.getExpandIcon().setSelected(expanded);

        // Set the margins according to state.
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)
                holder.getCardView().getLayoutParams();
        if (expanded) {
            if (position == 0) {
                // First panel's top margin should be regular.
                mlp.setMargins(mMarginCollapsed, mMarginCollapsed,
                        mMarginCollapsed, mMarginExpanded);
            } else {
                mlp.setMargins(mMarginCollapsed, mMarginExpanded,
                        mMarginCollapsed, mMarginExpanded);
            }
        } else {
            if (position == 0) {
                // First panel's top margin should be regular.
                mlp.setMargins(mMarginCollapsed, mMarginCollapsed,
                        mMarginCollapsed, mMarginCollapsed);
            } else {
                mlp.setMargins(mMarginCollapsed, 0,
                        mMarginCollapsed, mMarginCollapsed);
            }
        }
    }

    /** Private methods */

    private void readResourceValues(Resources res) {
        mMarginCollapsed = (int) res.getDimension(R.dimen.expansion_panel_margin_collapsed);
        mMarginExpanded = (int) res.getDimension(R.dimen.expansion_panel_margin_expanded);
        mTransitionDuration = res.getInteger(android.R.integer.config_shortAnimTime);
    }

    private void setupTransition() {
        mTransition = new AutoTransition();
        mTransition.setDuration(mTransitionDuration);
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

        ViewHolder(View itemView, final ItemClickListener clickListener) {
            super(itemView);

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

        void setSummaryVH(RecyclerView.ViewHolder viewHolder) {
            mSummaryVH = viewHolder;
        }

        void setDetailsVH(RecyclerView.ViewHolder viewHolder) {
            mDetailsVH = viewHolder;
        }

        void setActionsVH(RecyclerView.ViewHolder viewHolder) {
            mActionsVH = viewHolder;
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

        CardView getCardView() {
            return mCardView;
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
