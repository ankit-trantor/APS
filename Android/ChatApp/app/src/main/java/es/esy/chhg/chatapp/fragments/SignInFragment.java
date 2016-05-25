package es.esy.chhg.chatapp.fragments;

import android.content.Context;

import es.esy.chhg.chatapp.asynctasks.BaseAsyncTask;
import es.esy.chhg.chatapp.asynctasks.SignInAsyncTask;
import es.esy.chhg.chatapp.data.SignInResponse;

public class SignInFragment extends AsyncTaskFragment<SignInResponse> {
    public interface ISignInFragmentListener {
        String getUsername();

        String getEmail();

        String getPassword();
    }

    private ISignInFragmentListener mAsyncTaskFragmentListener;

    @Override
    public BaseAsyncTask startAsyncTask() {
        String username = mAsyncTaskFragmentListener.getUsername();
        String email = mAsyncTaskFragmentListener.getEmail();
        String password = mAsyncTaskFragmentListener.getPassword();

        SignInAsyncTask task = new SignInAsyncTask(getActivity().getApplicationContext(), username, email, password, getAsyncTaskListener());
        task.executeCompat();
        return task;
    }

    @Override
    public boolean isValidContext(Context context) {
        boolean valid = false;

        if (context instanceof ISignInFragmentListener) {
            valid = true;
            mAsyncTaskFragmentListener = (ISignInFragmentListener) context;
        }
        return valid;
    }
}