package com.anotap.whatagreatmovie.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.anotap.whatagreatmovie.MovieApp;
import com.anotap.whatagreatmovie.R;
import com.anotap.whatagreatmovie.api.ApiManager;
import com.anotap.whatagreatmovie.model.User;
import com.anotap.whatagreatmovie.util.FragmentUtil;
import com.anotap.whatagreatmovie.util.TextUtil;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class LoginFragment extends BaseFragment {
    @BindView(R.id.textInputLogin)
    TextInputLayout textInputLogin;
    @BindView(R.id.editTextLogin)
    EditText editTextLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflateWithLoadingIndicator(R.layout.fragment_login, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setTitle(getString(R.string.app_name));
    }

    @OnClick(R.id.buttonGo)
    public void onGoClick() {
        final String username = editTextLogin.getText().toString();

        if (TextUtils.isEmpty(username)) {
            showErrorMessage(R.string.error_enter_username);
            return;
        }
        if (!TextUtil.isValidLogin(username)) {
            showErrorMessage(R.string.error_invalid_login);
            return;
        }


        setLoading(true);
        ApiManager.getInstance()
                .getUsers(username, new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        setLoading(false);
                        if (users.size() > 0) {
                            navigateToMainScreen(null);
                            MovieApp.getInstance().setUser(users.get(0));
                        } else {
                            showCreateUserDialog(username);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        setLoading(false);
                        showErrorMessage(throwable.getMessage());
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * saves new user if needed and navigates to the next screen
     */
    private void navigateToMainScreen(@Nullable String username) {
        FragmentUtil.replaceFragment(getFragmentManager(), new MainFragment(), false);
    }

    private void showCreateUserDialog(final String username) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.username_not_found)
                .setMessage(String.format(Locale.getDefault(),
                        getString(R.string.create_new_user_message), username))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createUser(username);
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void createUser(final String username) {
        setLoading(true);
        ApiManager.getInstance()
                .createUser(new User(username), new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> users) throws Exception {
                        setLoading(false);
                        if (users.size() > 0) {
                            if (users.get(0).isSuccessful()) {
                                navigateToMainScreen(username);
                                MovieApp.getInstance().setUser(users.get(0));
                            } else {
                                showErrorMessage(users.get(0).getError());
                            }
                        } else {
                            showErrorMessage(R.string.error_creating_user);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        setLoading(false);
                        throwable.printStackTrace();
                        showErrorMessage(throwable.getMessage());
                    }
                });
    }
}
