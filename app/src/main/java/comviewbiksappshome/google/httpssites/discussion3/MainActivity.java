package comviewbiksappshome.google.httpssites.discussion3;

import android.app.Activity;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static int SIGN_IN_CODE = 1;
    // used to display messages in the ListView
    private FirebaseListAdapter<Messages> mFirebaseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // checking if the current FirebaseUser object is null
        // if null then ask for the user to sign in
        // if not then have the app display the text messages
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // used to start a new activity
            // creates the sign in/up
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),
                    SIGN_IN_CODE);
        } else {
            // notifies the user that they have successfully signed in
            Toast.makeText(this, "You have been signed in!", Toast.LENGTH_SHORT).show();
            // get the text messages
            getTextMessages();
        }

        Button send = findViewById(R.id.sendButton);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText textMessage = findViewById(R.id.textMessage);

                // Reads the data in textMessage and stores it in the database
                FirebaseDatabase.getInstance().getReference().push().setValue(textMessage.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                // clears the text after storing it in the database
                textMessage.setText("");
            }
        });
    }

    private void getTextMessages() {
        ListView messageList = findViewById(R.id.messageList);
        Query q = FirebaseDatabase.getInstance().getReference();
        FirebaseListOptions<Messages> options = new FirebaseListOptions.Builder<Messages>().setQuery(q, Messages.class).setLayout(R.layout.message).build();
        mFirebaseListAdapter = new FirebaseListAdapter<Messages>(options) {
            @Override
            protected void populateView(View v, Messages model, int position) {
                TextView textMessage = v.findViewById(R.id.messageBody);
                TextView userName = v.findViewById(R.id.userName);
                TextView time = v.findViewById(R.id.timeStamp);

                textMessage.setText(model.getText());
                userName.setText(model.getUserName());
                time.setText(DateFormat.DATE_FIELD);
            }
        };
        messageList.setAdapter(mFirebaseListAdapter);
    }

    // this method handles the Intent which is received by the java file once a user signs in
    @Override
    protected void onActivityResult(int req, int res, Intent intent) {
        super.onActivityResult(req, res, intent);
        if (req == SIGN_IN_CODE) {
            // if the res code is equal to RESULT_OK, it means the user has been able to sign in
            if (res == RESULT_OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                getTextMessages();
            } else {
                Toast.makeText(this, "Unsuccessful Sign In", Toast.LENGTH_SHORT).show();
                // quit the app
                finish();
            }
        }
    }

    @Override
    // accesses the menu_sign_out.xml file in order to specify the options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflate = getMenuInflater();
        // "inflates" the menu from the xml file menu_sign_out.xml
        inflate.inflate(R.menu.menu_sign_out, menu);
        return true;
    }

    @Override
    // this method is a handler for when a user selects an item from the options menu
    public boolean onOptionsItemSelected(MenuItem item) {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // displays short pop-up message to user that they have signed out
                Toast.makeText(MainActivity.this, "Successfully Signed Out!",
                        Toast.LENGTH_SHORT).show();
                // closes the app
                finish();
            }
        });
        return true;
    }

}
