package com.danielacedo.listproduct;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.danielacedo.listproduct.interfaces.ILoginMvp;
import com.danielacedo.listproduct.model.User;

import java.util.regex.Pattern;

/**
 * Created by Daniel on 6/10/16.
 */

/**
 * Presenter for the login Activity. It handles all the communication with the model and updates the view when necessary
 * @author Daniel Acedo Calderón
 */
public class LoginPresenter implements ILoginMvp.Presenter {

    private ILoginMvp.View view;

    public LoginPresenter(ILoginMvp.View view){
        this.view = view;
    }

    @Override
    public void validateCredentials(String user, String pass) {

        if(TextUtils.isEmpty(user)) {
            view.setMessageError(((Context) view).getResources().getString(R.string.err_emptyData), R.id.edt_User);
        }
        else if(TextUtils.isEmpty(pass)){
            view.setMessageError(((Context) view).getResources().getString(R.string.err_emptyData), R.id.edt_Pass);
        }
        else if(!Pattern.matches(".*[0-9].*", pass)){
            view.setMessageError(((Context)view).getResources().getString(R.string.err_Password_Digit), R.id.edt_Pass);
        }
        else if(!Pattern.matches(".*[a-z].*",pass) || !Pattern.matches(".*[A-Z].*",pass)){
            view.setMessageError(((Context)view).getResources().getString(R.string.err_Password_UpperLowerCase), R.id.edt_Pass);
        }
        else if(pass.length()<8){
            view.setMessageError(((Context)view).getResources().getString(R.string.err_Password_Length), R.id.edt_Pass);
        }else{
            //Save the user in the Application class
            ((ListProduct_Application)((Context)view).getApplicationContext()).setUser(new User(user, pass));
            Intent intent = new Intent((Context)view, ListProduct_Activity.class);
            ((Context) view).startActivity(intent);
        }


    }
}
