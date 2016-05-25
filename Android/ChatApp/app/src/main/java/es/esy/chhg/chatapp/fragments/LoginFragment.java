package es.esy.chhg.chatapp.fragments;

import android.content.Context;

import es.esy.chhg.chatapp.asynctasks.BaseAsyncTask;
import es.esy.chhg.chatapp.asynctasks.LoginAsyncTask;
import es.esy.chhg.chatapp.data.LoginResponse;

public class LoginFragment extends AsyncTaskFragment<LoginResponse> {
    public interface LoginFragmentListener {
        String getUsername();

        String getPassword();
    }

    private LoginFragmentListener mAsyncTaskFragmentListener;

    @Override
    public BaseAsyncTask startAsyncTask() {
        String username = mAsyncTaskFragmentListener.getUsername();
        String password = mAsyncTaskFragmentListener.getPassword();

        LoginAsyncTask task = new LoginAsyncTask(getActivity().getApplicationContext(), username, password, getAsyncTaskListener());
        task.executeCompat();

        return task;
    }

    @Override
    public boolean isValidContext(Context context) {
        boolean valid = false;

        if (context instanceof LoginFragmentListener) {
            valid = true;
            mAsyncTaskFragmentListener = (LoginFragmentListener) context;
        }
        return valid;
    }
}