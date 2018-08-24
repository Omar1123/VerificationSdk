package com.company.jak.faceverification.utils;

import android.support.v7.app.AppCompatActivity;

import com.company.jak.faceverification.gui.ErrorDialogFragment;
import com.company.jak.faceverification.gui.InfoDialogFragment;

import android.app.ProgressDialog;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {

    // ===========================================================
    // Private fields
    // ===========================================================

    private ProgressDialog mProgressDialog;

    // ===========================================================
    // Protected methods
    // ===========================================================

    protected void showProgress(int messageId) {
        showProgress(getString(messageId));
    }

    protected void showProgress(final String message) {
        hideProgress();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = ProgressDialog.show(BaseActivity.this, "", message);
            }
        });
    }

    protected void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

    protected void showError(String message, boolean close) {
        ErrorDialogFragment.newInstance(message, close).show(getFragmentManager(), "error");
    }

    protected void showError(int messageId) {
        showError(getString(messageId));
    }

    protected void showError(String message) {
        showError(message, false);
    }

    protected void showError(Throwable th) {
        Log.e(getClass().getSimpleName(), "Exception", th);
        showError(Utils.getMessage(th), false);
    }

    protected void showInfo(int messageId) {
        showInfo(getString(messageId));
    }

    protected void showInfo(String message) {
        InfoDialogFragment.newInstance(message).show(getFragmentManager(), "info");
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgress();
    }
}
