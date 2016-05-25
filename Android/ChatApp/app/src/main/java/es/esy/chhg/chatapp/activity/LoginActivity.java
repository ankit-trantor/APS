package es.esy.chhg.chatapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
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
import es.esy.chhg.chatapp.fragments.LoginFragment;
import es.esy.chhg.chatapp.data.LoginResponse;
import es.esy.chhg.chatapp.utils.NetworkUtil;
import es.esy.chhg.chatapp.utils.PasswordUtil;
import es.esy.chhg.chatapp.utils.PreferencesUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener,
        AsyncTaskFragment.AsyncTaskFragmentListener,
        LoginFragment.LoginFragmentListener {

    private final String FRAGMENT_TAG_WORKER_LOGIN = "tag_worker_login";

    private final int REQUEST_CODE_SIGN_IN = 1;

    private Button mLogin;
    private Button mSignIn;
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private LinearLayout mLinearLayoutLogin;
    private View mProgressView;

    private BaseAsyncTask.AsyncTaskListener<LoginResponse> mLoginResponseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
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
        registerEventClick();
    }

    private void registerEventClick() {
        mLogin.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
    }

    private void initializeObjects() {
        mLinearLayoutLogin = (LinearLayout) findViewById(R.id.linear_layout_login);
        mEditTextUsername = (EditText) findViewById(R.id.edit_text_username);
        mEditTextPassword = (EditText) findViewById(R.id.edit_text_password);
        mLogin = (Button) findViewById(R.id.button_login);
        mSignIn = (Button) findViewById(R.id.button_sign_in);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public void onClick(View v) {
        if (v == mSignIn) {
            startSignIn();
        } else if (v == mLogin) {
            doLogin();
        }
    }

    private void doLogin() {
        // Reset errors.
        mEditTextUsername.setError(null);
        mEditTextPassword.setError(null);

        String username = mEditTextUsername.getText().toString();
        String password = mEditTextPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            mEditTextUsername.setError(getString(R.string.error_field_required));
            focusView = mEditTextUsername;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mEditTextUsername.setError(getString(R.string.error_invalid_username));
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

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (NetworkUtil.isConnectedInternet(getApplicationContext())) {
                loginWorkerFragment();
            } else {
                Snackbar.make(mLinearLayoutLogin, getString(R.string.error_not_connected_internet), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private boolean isUsernameValid(String username) {
        return username.length() >= 3 && username.length() <= 20;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 5 && password.length() <= 16;
    }

    private void startSignIn() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        intent.putExtra(SignInActivity.PUT_EXTRA_USERNAME, mEditTextUsername.getText().toString());
        startActivityForResult(intent, REQUEST_CODE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public BaseAsyncTask.AsyncTaskListener getAsyncTaskListener(AsyncTaskFragment instance) {
        if (instance instanceof LoginFragment) {
            return getLoginResponseAsyncTask();
        }
        return null;
    }

    private void loginWorkerFragment() {
        Fragment task = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_WORKER_LOGIN);
        if (!(task instanceof LoginFragment)) {
            task = new LoginFragment();
            getSupportFragmentManager().beginTransaction().add(task, FRAGMENT_TAG_WORKER_LOGIN).commit();
        }
    }

    private BaseAsyncTask.AsyncTaskListener<LoginResponse> getLoginResponseAsyncTask() {

        if (mLoginResponseListener == null) {
            mLoginResponseListener = new BaseAsyncTask.AsyncTaskListener<LoginResponse>() {
                @Override
                public void onAsyncTaskCancelled() {

                }

                @Override
                public void onAsyncTaskComplete(LoginResponse loginResponse) {
                    if (loginResponse.isSuccess()) {
                        PreferencesUtil.saveLoginUser(getApplicationContext(), loginResponse.getUser().toJsonString());

                        setResult(RESULT_OK);
                        finish();
                    } else {
                        if (loginResponse.isUserNotFound()) {
                            mEditTextUsername.setError(getString(R.string.error_user_not_found_server));
                            mEditTextUsername.requestFocus();
                        } else if (loginResponse.isUserOrPasswordIncorrect()) {
                            mEditTextPassword.setError(getString(R.string.error_password_invalid_server));
                            mEditTextPassword.requestFocus();
                        } else {
                            Snackbar.make(mLinearLayoutLogin, getString(R.string.error_unknown), Snackbar.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onAsyncTaskFail() {

                }

                @Override
                public void onAsyncTaskFinish() {
                    removeWorkerFragment(FRAGMENT_TAG_WORKER_LOGIN);
                    showProgress(false);
                }

                @Override
                public void onAsyncTaskStart() {
                    showProgress(true);
                }
            };
        }
        return mLoginResponseListener;
    }

    @Override
    public String getUsername() {
        return mEditTextUsername.getText().toString();
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

            mLinearLayoutLogin.setVisibility(show ? View.GONE : View.VISIBLE);
            mLinearLayoutLogin.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLinearLayoutLogin.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLinearLayoutLogin.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}