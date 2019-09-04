package com.example.myapplication.fragments;

import android.support.annotation.CallSuper;
import android.support.v4.app.Fragment;

/**
 * @author Junyi
 */
public abstract class AbstractLazyLoadFragment extends Fragment {
    private boolean wasVisible = false;

    @CallSuper
    protected void onFirstVisible() {
        wasVisible = true;
    }

    protected void onVisibilityChange(boolean isVisible) {
        throw new UnsupportedOperationException("method not overridden");
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // is full initialized
        if (AbstractLazyLoadFragment.this.getActivity() == null) return;

        // is first show - lazy load data
        if (!wasVisible) {
            onFirstVisible();
        }

        // call visibility change
        try {
            onVisibilityChange(isVisibleToUser);
        } catch (UnsupportedOperationException e) {
            // child not override 'onVisibilityChange' method
        }
    }
}