package com.moblin.expansionpanelsdemo.gui;

import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moblin.expansionpanelsdemo.R;

@SuppressWarnings("WeakerAccess")
public class TripOptionsAdapter extends ExpansionPanelsAdapter {
    private final String[] LABELS = {
            "Trip name", "Location", "Start and end dates", "Carrier", "Meal preferences"
    };

    public TripOptionsAdapter(ViewGroup sceneRoot) {
        super(sceneRoot);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType,
                                                         ViewHolderType holderType) {
        View view;
        switch (holderType) {
            case SUMMARY:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.simple_summary, parent, false);
                return new SummaryViewHolder(view);
            case DETAILS:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.simple_details, parent, false);
                return new DetailsViewHolder(view);
            case ACTIONS:
                view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.simple_actions, parent, false);
                return new ActionsViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder,
                                    int position, ViewHolderType holderType) {
        if (holder instanceof SummaryViewHolder) {
            SummaryViewHolder svh = (SummaryViewHolder) holder;
            svh.getTextView().setText(LABELS[position]);
        } else if (holder instanceof DetailsViewHolder) {
            DetailsViewHolder dvh = (DetailsViewHolder) holder;
            // TODO
        } else if (holder instanceof ActionsViewHolder) {
            ActionsViewHolder avh = (ActionsViewHolder) holder;
            // TODO
        }
    }

    @Override
    public int getItemCount() {
        return LABELS.length;
    }

    /** Private methods */

    private static <T extends View> T lookup(View rootView, @IdRes int viewId) {
        //noinspection unchecked
        return (T) rootView.findViewById(viewId);
    }

    private class SummaryViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        SummaryViewHolder(View itemView) {
            super(itemView);
            mTextView = lookup(itemView, R.id.tv_main);
        }
        TextView getTextView() {
            return mTextView;
        }
    }

    private class DetailsViewHolder extends RecyclerView.ViewHolder {
        DetailsViewHolder(View itemView) {
            super(itemView);
            // TODO
        }
    }

    private class ActionsViewHolder extends RecyclerView.ViewHolder {
        ActionsViewHolder(View itemView) {
            super(itemView);
            // TODO
        }
    }
}
