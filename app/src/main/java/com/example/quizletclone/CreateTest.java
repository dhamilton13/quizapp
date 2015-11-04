package com.example.quizletclone;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTest extends AppCompatActivity {
    private ModelViewController mvc;
    private Button createQuestion;
    private EditText nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_test);

        /* Retrieve the ModelViewController object from the calling class */
        Intent intent = getIntent();
        mvc = intent.getExtras().getParcelable("MVCObj");

        setTitle("Create a test");

        /* Initialize the GUI elements */
        createQuestion = (Button)findViewById(R.id.createTest);
        nameField = (EditText)findViewById(R.id.nameField);

        /* When the user clicks 'Create Test', use the ModelViewController object to create the
            test.
         */
        createQuestion.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        mvc.createTest(nameField.getText().toString(), true);
                        nameField.setText("");

                        /* Acknowledge the test was created by displaying a small message */
                        Toast toast = Toast.makeText(getApplicationContext(), "Test created", Toast.LENGTH_SHORT);
                        //TODO: need a better way of calling toast (instead of creating an object everytime).
                        toast.show();

                        /* After a 2000ms delay, return to the list of tests */
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(getApplicationContext(),ListTest.class);
                                intent.putExtra("MVCObj", mvc);
                                startActivity(intent);
                                finish();
                            }
                        }, 2000);

                    }
                });


    }
}
