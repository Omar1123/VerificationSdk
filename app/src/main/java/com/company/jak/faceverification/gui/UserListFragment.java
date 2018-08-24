package com.company.jak.faceverification.gui;

/**
 * Created by jake on 19/08/18.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.company.jak.faceverification.R;
import com.neurotec.face.verification.NFaceVerification;
import com.neurotec.face.verification.NFaceVerificationUser;

public class UserListFragment extends DialogFragment {

    // ===========================================================
    // Public types
    // ===========================================================

    public interface UserSelectionListener {
        void onUserSelected(NFaceVerificationUser user, Bundle bundle);
    }

    // ===========================================================
    // Private static fields
    // ===========================================================

    private static final String EXTRA_ENABLED = "enabled";

    private static NFaceVerification.UserCollection mUsers;

    // ===========================================================
    // Public static methods
    // ===========================================================

    public static DialogFragment newInstance(NFaceVerification.UserCollection users, boolean enabled, Bundle bundle) {
        mUsers = users;
        bundle.putBoolean(UserListFragment.EXTRA_ENABLED, true);
        UserListFragment fragment = new UserListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    // ===========================================================
    // Private fields
    // ===========================================================

    private ListView mSubjectListView;
    private UserSelectionListener mListener;
    private List<String> mItems;

    // ===========================================================
    // Private constructor
    // ===========================================================

    private UserListFragment() {
    }

    // ===========================================================
    // Public methods
    // ===========================================================

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (UserSelectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SubjectSelectionListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_list, null);
        mItems = new ArrayList<String>();
        for (int i = 0; i < mUsers.size(); i++) {
            NFaceVerificationUser user = mUsers.get(i);
            System.out.print("User id: " + user.getId());
            mItems.add(user.getId());
        }
        if (mItems.isEmpty()) {
            mItems.add(getResources().getString(R.string.msg_no_users));
        }
        mSubjectListView = (ListView) view.findViewById(R.id.list);
        mSubjectListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mItems));
        mSubjectListView.setEnabled(getArguments().getBoolean(EXTRA_ENABLED));
        mSubjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < mUsers.size(); i++) {
                    NFaceVerificationUser user = mUsers.get(i);
                    if (user.getId().equals(mItems.get(position))) {
                        mListener.onUserSelected(user, getArguments());
                        break;
                    }
                }
                dismiss();
            }
        });

        builder.setView(view);
        builder.setTitle(R.string.msg_database);
        return builder.create();
    }
}
