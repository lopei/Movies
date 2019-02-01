package com.anotap.whatagreatmovie.ui;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anotap.whatagreatmovie.R;
import com.anotap.whatagreatmovie.Toolbarable;
import com.tapadoo.alerter.Alerter;

/**
 * The base class for all extending fragment classes that contains usefull features
 */

public class BaseFragment extends Fragment implements Toolbarable {
    private SwipeRefreshLayout swipeRefreshLayout;

    private Toolbarable toolbarable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Toolbarable) {
            this.toolbarable = (Toolbarable) context;
        }
    }

    /**
     * automatically adds loading indicator for every fragment view
     */
    protected View inflateWithLoadingIndicator(int resId, ViewGroup parent) {
        swipeRefreshLayout = new SwipeRefreshLayout(getActivity());
        swipeRefreshLayout.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        swipeRefreshLayout.setEnabled(false);
        View view = LayoutInflater.from(getActivity()).inflate(resId, parent, false);
        swipeRefreshLayout.addView(view);
        return swipeRefreshLayout;
    }

    /**
     * checks if loading indicator is currently active
     */
    protected boolean isLoading() {
        return swipeRefreshLayout.isRefreshing();
    }

    /**
     * enables of disables loading indicator
     */
    protected void setLoading(boolean loading) {
        swipeRefreshLayout.setRefreshing(loading);
    }

    protected void showErrorMessage(String text) {
        if (!isAttached()) {
            return;
        }
        Alerter.create(getActivity())
                .setIconColorFilter(ContextCompat.getColor(getActivity(), android.R.color.white))
                .setBackgroundColorRes(R.color.colorError)
                .setIcon(R.drawable.ic_close_circle)
                .setTitle(R.string.error)
                .setText(text)
                .show();
    }

    protected void showErrorMessage(int resId) {
        if (!isAttached()) {
            return;
        }
        showErrorMessage(getString(resId));
    }

    protected void showInfoMessage(String text) {
        if (!isAttached()) {
            return;
        }
        Alerter.create(getActivity())
                .setIconColorFilter(ContextCompat.getColor(getActivity(), android.R.color.white))
                .setBackgroundColorRes(R.color.colorInfo)
                .setIcon(R.drawable.ic_close_circle)
                .setTitle(R.string.information)
                .setText(text)
                .show();
    }

    protected void showInfoMessage(int resId) {
        if (!isAttached()) {
            return;
        }
        showInfoMessage(getString(resId));
    }

    protected void showSuccessMessage(String text) {
        if (!isAttached()) {
            return;
        }
        Alerter.create(getActivity())
                .setIconColorFilter(ContextCompat.getColor(getActivity(), android.R.color.white))
                .setBackgroundColorRes(R.color.colorSuccess)
                .setIcon(R.drawable.ic_check_circle)
                .setTitle(R.string.success)
                .setText(text)
                .show();
    }

    protected void showSuccessMessage(int resId) {
        if (!isAttached()) {
            return;
        }
        showSuccessMessage(getString(resId));
    }

    /**
     * checks if the fragment is currently available and active
     */
    protected boolean isAttached() {
        return getActivity() != null && !getActivity().isFinishing() && isAdded();
    }

    @Override
    public void setTitle(String title) {
        if (toolbarable != null) {
            toolbarable.setTitle(title);
        }
    }

    @Override
    public void setShowBackArrow(boolean show) {
        if (toolbarable != null) {
            toolbarable.setShowBackArrow(show);
        }
    }
}
