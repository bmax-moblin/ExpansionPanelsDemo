package com.moblin.expansionpanelsdemo.gui;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.moblin.expansionpanelsdemo.R;
import com.moblin.expansionpanelsdemo.util.Assert;

public class MainActivity extends AppCompatActivity {

    /** Activity methods */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupGui();
    }

    /** Private methods */

    private void setupGui() {
        RecyclerView rv = lookup(R.id.rv_expandable_panels);
        Assert.notNull(rv, "View not found: rv_expandable_panels");
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new TripOptionsAdapter(getResources(), rv));
    }

    private <T extends View> T lookup(@IdRes int viewId) {
        //noinspection unchecked
        return (T) findViewById(viewId);
    }
}
