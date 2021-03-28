package dev.basjansen.scribble;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import dev.basjansen.scribble.models.User;
import dev.basjansen.scribble.services.UserService;

public class LoginActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1;

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        SignInButton googleSignInButton = findViewById(R.id.google_sign_in_button);

        googleSignInButton.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(Exception.class);
                if (account == null)
                    throw new Exception("Google sign in failed");
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (Exception e) {
                Log.w(LoginActivity.class.getName(), "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String IDToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(IDToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    FirebaseUser user = auth.getCurrentUser();
                    if (!task.isSuccessful() || user == null) {
                        Log.w(LoginActivity.class.getName(), "signInWithCredential:failure", task.getException());
                        return;
                    }

                    UserService userService = new UserService();
                    userService.save(new User(user.getUid(), user.getDisplayName()));
                    Intent intent = new Intent(this, GalleryActivity.class);
                    startActivity(intent);
                });
    }

}