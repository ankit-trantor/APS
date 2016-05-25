package es.esy.chhg.chatapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import es.esy.chhg.chatapp.R;
import es.esy.chhg.chatapp.asynctasks.BaseAsyncTask;
import es.esy.chhg.chatapp.fragments.AsyncTaskFragment;
import es.esy.chhg.chatapp.fragments.SignInFragment;
import es.esy.chhg.chatapp.data.SignInResponse;
import es.esy.chhg.chatapp.utils.NetworkUtil;
import es.esy.chhg.chatapp.utils.PasswordUtil;
import es.esy.chhg.chatapp.utils.PreferencesUtil;

public class SignInActivity extends BaseActivity implements
        View.OnClickListener,
        AsyncTaskFragment.AsyncTaskFragmentListener,
        SignInFragment.ISignInFragmentListener {

    public static final String PUT_EXTRA_USERNAME = "put_extra_username";

    private final String FRAGMENT_TAG_WORKER_SIGN_IN = "frag_tag_worker_sign_in";

    private EditText mEditTextUsername;
    private EditText mEditTextEmail;
    private EditText mEditTextPassword;
    private Button mButtonSignIn;
    private LinearLayout mLinearLayoutForm;
    private View mProgressView;

    private BaseAsyncTask.AsyncTaskListener<SignInResponse> mSignInResponseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initialize();
        setupSavedInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialize() {
        setupToolbar(false, true);
        initializeObjects();
        registerClickEvent();
    }

    private void setupSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            String username = getIntent().getStringExtra(PUT_EXTRA_USERNAME);

            if (username != null) {
                mEditTextUsername.setText(username);
            }
        }
    }

    private void initializeObjects() {
        mEditTextUsername = (EditText) findViewById(R.id.edit_text_username);
        mEditTextPassword = (EditText) findViewById(R.id.edit_text_password);
        mEditTextEmail = (EditText) findViewById(R.id.edit_text_email);
        mLinearLayoutForm = (LinearLayout) findViewById(R.id.linear_layout_form);
        mButtonSignIn = (Button) findViewById(R.id.button_sign_in);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void registerClickEvent() {
        mButtonSignIn.setOnClickListener(this);
    }

    private void doSignIn() {
        // Reset errors.
        mEditTextUsername.setError(null);
        mEditTextEmail.setError(null);
        mEditTextPassword.setError(null);

        String username = mEditTextUsername.getText().toString();
        String email = mEditTextEmail.getText().toString();
        String password = mEditTextPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            mEditTextUsername.setError(getString(R.string.error_field_required));
            focusView = mEditTextUsername;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mEditTextUsername.setError(getString(R.string.error_invalid_username_digits));
            focusView = mEditTextUsername;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mEditTextPassword.setError(getString(R.string.error_field_required));
            focusView = mEditTextPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mEditTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = mEditTextPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEditTextEmail.setError(getString(R.string.error_field_required));
            focusView = mEditTextEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEditTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEditTextEmail;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            if (NetworkUtil.isConnectedInternet(getApplicationContext())) {
                signInWorkerFragment();
            } else {
                Snackbar.make(mLinearLayoutForm, getString(R.string.error_not_connected_internet), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private boolean isUsernameValid(String username) {
        return username.length() >= 3 && username.length() <= 20;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.length() <= 255;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 5 && password.length() <= 16;
    }

    @Override
    public void onClick(View v) {
        if (v == mButtonSignIn) {
            doSignIn();
        }
    }

    @Override
    public BaseAsyncTask.AsyncTaskListener getAsyncTaskListener(AsyncTaskFragment instance) {
        if (instance instanceof SignInFragment) {
            return getSignResponseAsyncTask();
        }
        return null;
    }

    private void signInWorkerFragment() {
        Fragment task = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_WORKER_SIGN_IN);
        if (!(task instanceof SignInFragment)) {
            task = new SignInFragment();
            getSupportFragmentManager().beginTransaction().add(task, FRAGMENT_TAG_WORKER_SIGN_IN).commit();
        }
    }

    private BaseAsyncTask.AsyncTaskListener<SignInResponse> getSignResponseAsyncTask() {
        if (mSignInResponseListener == null) {
            mSignInResponseListener = new BaseAsyncTask.AsyncTaskListener<SignInResponse>() {
                @Override
                public void onAsyncTaskCancelled() {

                }

                @Override
                public void onAsyncTaskComplete(SignInResponse signInResponse) {
                    if (signInResponse.isSuccess()) {
                        PreferencesUtil.saveLoginUser(getApplicationContext(), signInResponse.getUser().toJsonString());

                        setResult(RESULT_OK);
                        finish();
                    } else {
                        if (signInResponse.isUserExist()) {
                            mEditTextUsername.setError(getString(R.string.error_user_exist));
                            mEditTextUsername.requestFocus();
                        } else if (signInResponse.isEmailExist()) {
                            mEditTextEmail.setError(getString(R.string.error_email_exist));
                            mEditTextEmail.requestFocus();
                        } else {
                            Snackbar.make(mLinearLayoutForm, getString(R.string.error_unknown), Snackbar.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onAsyncTaskFail() {

                }

                @Override
                public void onAsyncTaskFinish() {
                    removeWorkerFragment(FRAGMENT_TAG_WORKER_SIGN_IN);
                    showProgress(false);
                }

                @Override
                public void onAsyncTaskStart() {
                    showProgress(true);
                }
            };
        }

        return mSignInResponseListener;
    }

    @Override
    public String getUsername() {
        return mEditTextUsername.getText().toString();
    }

    @Override
    public String getEmail() {
        return mEditTextEmail.getText().toString();
    }

    @Override
    public String getPassword() {
        return PasswordUtil.hashPassword(mEditTextPassword.getText().toString());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLinearLayoutForm.setVisibility(show ? View.GONE : View.VISIBLE);
            mLinearLayoutForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLinearLayoutForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLinearLayoutForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}