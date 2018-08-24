package com.company.jak.faceverification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.neurotec.face.verification.NFaceVerification;
import com.neurotec.face.verification.NFaceVerificationCapturePreviewEvent;
import com.neurotec.face.verification.NFaceVerificationCapturePreviewListener;
import com.neurotec.face.verification.NFaceVerificationStatus;
import com.neurotec.face.verification.NFaceVerificationUser;
import com.neurotec.face.verification.view.NFaceVerificationView;
import com.neurotec.lang.NCore;
import com.neurotec.licensing.gui.ActivationActivity;
import com.company.jak.faceverification.gui.EnrollmentDialogFragment;
//import com.company.jak.faceverification.gui.SettingsActivity;
import com.company.jak.faceverification.gui.SettingsFragment;
import com.company.jak.faceverification.gui.UserListFragment;
import com.company.jak.faceverification.gui.EnrollmentDialogFragment.EnrollmentDialogListener;
import com.company.jak.faceverification.gui.UserListFragment.UserSelectionListener;
import com.company.jak.faceverification.utils.BaseActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class FaceVerificationActivity extends BaseActivity implements EnrollmentDialogListener, UserSelectionListener {

    private static final String EXTRA_REQUEST_CODE = "request_code";
    private static final int VERIFICATION_REQUEST_CODE = 1;
    private static final int TIMEOUT = 60000;
    private boolean mAppClosing;
    private NFaceVerificationView mFaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_verification);
        NCore.setContext(this);

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    showProgress(R.string.msg_initialising);

                    // get NFV for the first time
                    final NFaceVerification nfv = NFV.getInstance();

                    // load settings
                    SettingsFragment.loadSettings();

                    // button implementations
                    Button mEnrollButton = (Button) findViewById(R.id.button_enroll);
                    mEnrollButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            EnrollmentDialogFragment enrollDialog = new EnrollmentDialogFragment();
                            enrollDialog.show(getFragmentManager(), "enrollment");
                        }
                    });

                    Button mCancelButton = (Button) findViewById(R.id.button_cancel);
                    mCancelButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            showProgress(R.string.msg_cancelling);
                            nfv.cancel();
                            hideProgress();
                        }
                    });

                    Button mVerifyButton = (Button) findViewById(R.id.button_verify);
                    mVerifyButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(EXTRA_REQUEST_CODE, VERIFICATION_REQUEST_CODE);
                            UserListFragment userList = (UserListFragment) UserListFragment.newInstance(nfv.getUsers(), true, bundle);
                            userList.show(getFragmentManager(), "verification");
                        }
                    });

                    // set frontal camera
                    String[] names = nfv.getAvailableCameraNames();
                    for (String n : names) {
                        if (n.contains("Front")) {
                            nfv.setCamera(n);
                            break;
                        }
                    }

                    mFaceView = (NFaceVerificationView) findViewById(R.id.nFaceView);
                    nfv.addCapturePreviewListener(new NFaceVerificationCapturePreviewListener() {

                        @Override
                        public void capturePreview(NFaceVerificationCapturePreviewEvent arg0) {
                            mFaceView.setEvent(arg0);
                        }
                    });

                    hideProgress();
                } catch (Exception ex) {
                    hideProgress();
                    showError(ex);
                }
            }

        }).start();
    }

    @Override
    public void onEnrollmentIDProvided(final String id) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // cancel in there are any other operations in progress
                    NFV.getInstance().cancel();
                    NFaceVerificationStatus status = NFV.getInstance().enroll(id, TIMEOUT, null);
                    if (!mAppClosing) showInfo(String.format(getString(R.string.msg_operation_status), status.toString()));
                } catch (Throwable e) {
                    showError(e);
                }
            }
        }).start();
    };

    @Override
    public void onUserSelected(final NFaceVerificationUser user, Bundle bundle) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // cancel in there are any other operations in progress
                    NFV.getInstance().cancel();
                    NFaceVerificationStatus status = NFV.getInstance().verify(user.getId(), TIMEOUT);
                    if (!mAppClosing) showInfo(String.format(getString(R.string.msg_operation_status), status.toString()));
                } catch (Throwable e) {
                    showError(e);
                }
            }
        }).start();
    };

    @Override
    protected void onStop() {
        mAppClosing = true;
        NFV.getInstance().cancel();
        super.onStop();
    }
}
