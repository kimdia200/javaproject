package net.skhu.javaproject_address;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity{
    Intent intent1;
    Intent intent2;
    private static final String TAG = "파이어베이스 ";
    private static final int RC_SIGN_IN=9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView googleId;
    private TextView statusNow;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googleId = (TextView)findViewById(R.id.textView_googleID);
        statusNow = (TextView)findViewById(R.id.textView_status);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .build();
        mGoogleSignInClient =  GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();


        final Button btn1 = (Button)findViewById(R.id.btn_list);
        Button btn2 = (Button)findViewById(R.id.btn_add);
        intent1 = new Intent(this, RecyclerView.class);
        intent2 = new Intent(this, AddAdressActivity.class);

        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1.putExtra("googleID",googleId.getText().toString());
                intent2.putExtra("googleID",googleId.getText().toString());
                if(v.getId()==R.id.btn_list) {
                    if (mAuth.getCurrentUser() != null)
                        startActivity(intent1);
                    else
                        Toast.makeText(MainActivity.this, "로그인을 먼저해주세요", Toast.LENGTH_SHORT).show();
                }
                else if(v.getId()==R.id.btn_add) {
                    if (mAuth.getCurrentUser() != null)
                        startActivity(intent2);
                    else
                        Toast.makeText(MainActivity.this, "로그인을 먼저해주세요", Toast.LENGTH_SHORT).show();
                }



            }
        };
        btn1.setOnClickListener(listener1);
        btn2.setOnClickListener(listener1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void signOut(){
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            int lastIndex = user.getEmail().toString().lastIndexOf("@");
            id = user.getEmail().toString().substring(0,lastIndex);
            googleId.setText(id);
            statusNow.setText(R.string.status_login);
        } else {
            googleId.setText(null);
            statusNow.setText(R.string.status_logout);
            Toast.makeText(this, "로그아웃처리 되었습니다", Toast.LENGTH_SHORT).show();
        }
    }
    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }


    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_login) {
            if(mAuth.getCurrentUser()==null) signIn();
            else Toast.makeText(this, "이미 로그인 되어있습니다.", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.btn_logout) {
            if(mAuth.getCurrentUser()!=null) signOut();
            else Toast.makeText(this, "로그인 되어있지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

}
