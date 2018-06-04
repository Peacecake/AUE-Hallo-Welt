package de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.widget.EditText;
import java.util.ArrayList;

import de.ur.fischermeierhoeferpeiser.hallowelt.aue.hallowelt.R;

public class FormValidator {

    private ArrayList<String> errors;
    private Context context;

    public FormValidator(Context context) {
        errors = new ArrayList<>();
        this.context = context;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public String getErrorsAsString() {
        StringBuilder b = new StringBuilder();
        for(String error : errors) {
            b.append(error+"\n");
        }
        String errorString = b.toString();
        return errorString.substring(0, errorString.length() - 1);
    }

    public void clearErrors() {
        errors = new ArrayList<>();
    }

    public boolean isValid() {
        return errors.size() == 0;
    }

    public boolean isValidEmail(String target) {
        if (target.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
            return true;
        }
        errors.add(context.getString(R.string.msgInvalidEmail));
        return false;
    }

    public boolean passwortRepeatMatches(String password, String passwordRepeat) {
        if (password.equals(passwordRepeat)) {
            return true;
        }
        errors.add(context.getString(R.string.msgPasswordMatch));
        return false;
    }

    public boolean requiredFieldsAreEmpty(EditText... editTexts) {
        boolean isEmpty = false;
        for(EditText et : editTexts) {
            if (et.getText().toString().equals("")) {
                errors.add(et.getHint() + " " + context.getString(R.string.msgRequiredField));
                isEmpty = true;
                break;
            }
        }

        return isEmpty;
    }
}
