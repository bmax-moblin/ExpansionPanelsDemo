package com.moblin.expansionpanelsdemo.gui;

import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.moblin.expansionpanelsdemo.R;
import com.moblin.expansionpanelsdemo.util.Assert;

@SuppressWarnings("WeakerAccess")
public class TripOptionsAdapter extends ExpansionPanelsAdapter {
    private static final int POS_TRIP_NAME = 0;
    private static final int POS_LOCATION = 1;
    private static final int POS_DURATION = 2;
    private static final int POS_CARRIER = 3;
    private static final int POS_MEAL_PREF = 4;
    private String[] mSettings, mTripNameOptions, mLocationOptions,
            mDurationOptions, mCarrierOptions, mMealOptions;

    public TripOptionsAdapter(Resources res, ViewGroup sceneRoot) {
        super(res, sceneRoot);
        mSettings = res.getStringArray(R.array.settings);
        mTripNameOptions = res.getStringArray(R.array.trip_name_options);
        mLocationOptions = res.getStringArray(R.array.location_options);
        mDurationOptions = res.getStringArray(R.array.duration_options);
        mCarrierOptions = res.getStringArray(R.array.carrier_options);
        mMealOptions = res.getStringArray(R.array.meal_options);
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
            svh.getSetting().setText(mSettings[position]);
            // TODO
            svh.getValue().setText(mResources.getString(R.string.not_set));
        } else if (holder instanceof DetailsViewHolder) {
            DetailsViewHolder dvh = (DetailsViewHolder) holder;
            switch (position) {
                case POS_TRIP_NAME:
                    dvh.getOptionA().setText(mTripNameOptions[0]);
                    dvh.getOptionB().setText(mTripNameOptions[1]);
                    dvh.getOptionC().setText(mTripNameOptions[2]);
                    break;
                case POS_LOCATION:
                    dvh.getOptionA().setText(mLocationOptions[0]);
                    dvh.getOptionB().setText(mLocationOptions[1]);
                    dvh.getOptionC().setText(mLocationOptions[2]);
                    break;
                case POS_DURATION:
                    dvh.getOptionA().setText(mDurationOptions[0]);
                    dvh.getOptionB().setText(mDurationOptions[1]);
                    dvh.getOptionC().setText(mDurationOptions[2]);
                    break;
                case POS_CARRIER:
                    dvh.getOptionA().setText(mCarrierOptions[0]);
                    dvh.getOptionB().setText(mCarrierOptions[1]);
                    dvh.getOptionC().setText(mCarrierOptions[2]);
                    break;
                case POS_MEAL_PREF:
                    dvh.getOptionA().setText(mMealOptions[0]);
                    dvh.getOptionB().setText(mMealOptions[1]);
                    dvh.getOptionC().setText(mMealOptions[2]);
                    break;
                default:
                    break;
            }
        } else if (holder instanceof ActionsViewHolder) {
            ActionsViewHolder avh = (ActionsViewHolder) holder;
            // TODO
        }
    }

    @Override
    public int getItemCount() {
        return mSettings.length;
    }

    /** Private methods */

    private static <T extends View> T lookup(View rootView, @IdRes int viewId) {
        //noinspection unchecked
        return (T) rootView.findViewById(viewId);
    }

    private class SummaryViewHolder extends RecyclerView.ViewHolder {
        private TextView mSetting;
        private TextView mValue;

        SummaryViewHolder(View itemView) {
            super(itemView);
            mSetting = lookup(itemView, R.id.tv_setting);
            Assert.notNull(mSetting, "View not found: tv_setting");
            mValue = lookup(itemView, R.id.tv_value);
            Assert.notNull(mValue, "View not found: tv_value");
        }

        public TextView getSetting() {
            return mSetting;
        }

        public TextView getValue() {
            return mValue;
        }
    }

    private class DetailsViewHolder extends RecyclerView.ViewHolder {
        private RadioButton mOptionA, mOptionB, mOptionC;

        DetailsViewHolder(View itemView) {
            super(itemView);
            mOptionA = lookup(itemView, R.id.rb_option_a);
            mOptionB = lookup(itemView, R.id.rb_option_b);
            mOptionC = lookup(itemView, R.id.rb_option_c);
        }

        public RadioButton getOptionA() {
            return mOptionA;
        }

        public RadioButton getOptionB() {
            return mOptionB;
        }

        public RadioButton getOptionC() {
            return mOptionC;
        }
    }

    private class ActionsViewHolder extends RecyclerView.ViewHolder {
        ActionsViewHolder(View itemView) {
            super(itemView);
            // TODO
        }
    }
}
