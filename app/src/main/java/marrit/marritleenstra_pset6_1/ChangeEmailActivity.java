package marrit.marritleenstra_pset6_1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeEmailActivity extends AppCompatActivity {

    // variables
    private String TAG = "CHANGEEMAILACTIVITY";

    // UI references
    EditText mNewEmail;
    Button mChangeEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        // initiate UI references
        mNewEmail = (EditText) findViewById(R.id.ET_email);
        mChangeEmail = (Button) findViewById(R.id.BUTTON_change_email);

        mChangeEmail.setOnClickListener(new changeEmailOnClick());

    }

    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private class changeEmailOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            // get current user info
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String mUID = user.getUid();



            boolean cancel = false;
            View focusView = null;

            // Check if new email is valid
            final String email = mNewEmail.getText().toString();

            // Check for a valid email address.
            if (TextUtils.isEmpty(email)) {
                mNewEmail.setError(getString(R.string.error_field_required));
                focusView = mNewEmail;
                cancel = true;
            } else if (!isEmailValid(email)) {
                mNewEmail.setError(getString(R.string.error_invalid_email));
                focusView = mNewEmail;
                cancel = true;
            }

            if (cancel) {
                // There was an error; don't attempt to change password and focus
                // form field with an error.
                focusView.requestFocus();
                System.out.println("before return");
                return;
            }
            else {

                // TODO: let the user know that he has to login with the new email adress

                // block of code from firebase guide on user management
                user.updateEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User email address updated.");

                                    // get database reference
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                                    // update email address in database as well
                                    mDatabase.child("users").child(mUID).child("email").setValue(email);

                                    // return to settings
                                    Intent intent = new Intent(ChangeEmailActivity.this, SettingsActivity.class);
                                    ChangeEmailActivity.this.startActivity(intent);
                                }
                                //TODO check if re-authentication is needed
                                /*else if(!task.isSuccessful()) {
                                    try {
                                        throw task.getException()
                                    } catch(FirebaseAuthRecentLoginRequiredException e) {
                                        // Get auth credentials from the user for re-authentication. The example below shows
                                        // email and password credentials but there are multiple possible providers,
                                        // such as GoogleAuthProvider or FacebookAuthProvider.
                                        AuthCredential credential = EmailAuthProvider
                                                .getCredential(email, "password1234");

/                                       // Prompt the user to re-provide their sign-in credentials
                                        user.reauthenticate(credential)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Log.d(TAG, "User re-authenticated.");
                                                    }
                                                });
                                    }
                                }*/


                            }
                        });
            }
        }
    }

}
