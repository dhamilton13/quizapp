package com.example.quizletclone;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by jefflau on 11/3/15.
 */
public class FlashcardActivity extends AppCompatActivity {
    private ModelViewController mvc;
    private int position, testPosition;
    private boolean tapped, isFlashcardGraded = false, isTestGraded = false;
    private String callingClass;
    private TextView testTextView, header;
    private LinearLayout layout;
    private Flashcard card;
    private ArrayList<CheckBox> checkBoxes;
    private ArrayList<TextView> checkAllTextView, mpTextView, tfTextView;
    private ArrayList<RadioButton> mpRadioButtons, tfRadioButtons;
    private EditText saTextField;
    private Button saveAnswerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        // when tapped == true, display question string, otherwise it will display answer
        tapped = true;

        // gets intent to obtain position of card clicked
        Intent intent = getIntent();
        mvc = ModelViewController.getInstance(this);

        // the position of the cardview clicked
        position = intent.getIntExtra("POS", 0);
        callingClass = intent.getStringExtra("callingClass");
        layout = (LinearLayout) findViewById(R.id.layout);

        // set the question onto flashcard
        testTextView = (TextView) findViewById(R.id.testTextView);

        if(!mvc.isTag)
            card = mvc.getFlashcards().get(position);       //get all cards from database
        else
            card = mvc.sortedCard.get(position);            //only get the card sorted by tag

        testTextView.setMovementMethod(new ScrollingMovementMethod()); // make scrollable

        // set the header onto flashcard
        header = (TextView) findViewById(R.id.header);
        header.setTypeface(null, Typeface.BOLD); // make bold
        header.setText("QUESTION");

        //mvc.isTag decide whether this flashCard activity is open from a test list or normal
        //list (i.e. flash card list or tag list,
        if (!mvc.isTag) {
            /* Flashcard activity layout will depend on whether it is a normal flashcard or a test
                flashcard. Normal flashcards show answer and question. Test flashcards allow the
                user to input questions. The below if else statements will build the layout
                depending on the type of flashcard (Test vs normal).

                Calls either createBasicLayout (normal flashcard) or createLayoutBasedOnCagetory
                (test flashcard)
             */
            if (!callingClass.contains("FlashcardListForTestsActivity")) {
                testTextView.setText(card.getQuestion());
                createBasicFlashcardLayout(mvc.getFlashcards().get(position).getCategory());
            } else if (callingClass.contains("FlashcardListForTestsActivity")) {
                isTestGraded = TestGrader.isTestGraded();
                if (TestGrader.getUserAnswers()[position] != null)
                    isFlashcardGraded = true;

                testPosition = intent.getIntExtra("TestPOS", 0);
                testTextView.setText(mvc.getTests().get(testPosition).getSetOfFlashcards().get(position).getQuestion());
                String category = mvc.getTests().get(testPosition).getSetOfFlashcards().get(position).getCategory();

                saveAnswerButton = new Button(this);
                saveAnswerButton.setText("Save Answer");
                layout.addView(saveAnswerButton);

                createTestFlashcardLayout(category);
                getUserAnswer(category);

                if (isFlashcardGraded || isTestGraded)
                    createGradedTestFlashcardLayout(category);
            }
        }
        else {
            mvc.isTag = false;      //toggle the bool back for other activity
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Create a test flashcard layout depending on category type */
    private void createTestFlashcardLayout(final String category) {
        /*
         * mp = multiple choice
         * tf = true false
         * sa = short answer
         */
        if (category.equals(CheckAllThatApply.CATEGORY)) {
            checkBoxes = new ArrayList<CheckBox>();
            checkAllTextView = new ArrayList<TextView>();

            for (int i = 0; i < 5; ++i) {
                checkBoxes.add(new CheckBox(this));
                checkAllTextView.add(new TextView(this));
                checkAllTextView.get(i).setText(mvc.getTests().get(testPosition).
                        getSetOfFlashcards().get(position).getListOfAnswers().get(i));
                layout.addView(checkBoxes.get(i));
                layout.addView(checkAllTextView.get(i));
            }
        } else if (category.equals(MultipleChoice.CATEGORY)) {
            mpRadioButtons = new ArrayList<RadioButton>();
            mpTextView = new ArrayList<TextView>();

            for (int i = 0; i < 5; ++i) {
                mpRadioButtons.add(new RadioButton(this));
                mpTextView.add(new TextView(this));
                mpTextView.get(i).setText(mvc.getTests().get(testPosition).
                        getSetOfFlashcards().get(position).getListOfAnswers().get(i));
                layout.addView(mpRadioButtons.get(i));
                layout.addView(mpTextView.get(i));
            }
        } else if (category.equals(TrueFalse.CATEGORY)) {
            tfRadioButtons = new ArrayList<RadioButton>();
            tfTextView = new ArrayList<TextView>();

            for (int i = 0; i < 2; ++i) {
                tfRadioButtons.add(new RadioButton(this));
                tfTextView.add(new TextView(this));
                tfTextView.get(i).setText(mvc.getTests().get(testPosition).
                        getSetOfFlashcards().get(position).getListOfAnswers().get(i));
                layout.addView(tfRadioButtons.get(i));
                layout.addView(tfTextView.get(i));
            }
        } else {
            saTextField = new EditText(this);
            layout.addView(saTextField);
        }
    }

    /* Create a normal flashcard depending on layout */
    private void createBasicFlashcardLayout(String category) {
        if (category.equals(CheckAllThatApply.CATEGORY)) {
            TextView t1 = new TextView(this);
            TextView t2 = new TextView(this);
            TextView t3 = new TextView(this);
            TextView t4 = new TextView(this);
            TextView t5 = new TextView(this);

            t1.setText("A: " + card.getListOfAnswers().get(0));
            t2.setText("B: " + card.getListOfAnswers().get(1));
            t3.setText("C: " + card.getListOfAnswers().get(2));
            t4.setText("D: " + card.getListOfAnswers().get(3));
            t5.setText("E: " + card.getListOfAnswers().get(4));

            layout.addView(t1);
            layout.addView(t2);
            layout.addView(t3);
            layout.addView(t4);
            layout.addView(t5);

        } else if (category.equals(MultipleChoice.CATEGORY)) {
            TextView t1 = new TextView(this);
            TextView t2 = new TextView(this);
            TextView t3 = new TextView(this);
            TextView t4 = new TextView(this);
            TextView t5 = new TextView(this);

            t1.setText("A: " + card.getListOfAnswers().get(0));
            t2.setText("B: " + card.getListOfAnswers().get(1));
            t3.setText("C: " + card.getListOfAnswers().get(2));
            t4.setText("D: " + card.getListOfAnswers().get(3));
            t5.setText("E: " + card.getListOfAnswers().get(4));

            layout.addView(t1);
            layout.addView(t2);
            layout.addView(t3);
            layout.addView(t4);
            layout.addView(t5);
        } else if (category.equals(TrueFalse.CATEGORY)) {
            TextView t1 = new TextView(this);
            TextView t2 = new TextView(this);

            t1.setText("A: " + card.getListOfAnswers().get(0));
            t2.setText("B: " + card.getListOfAnswers().get(1));

            layout.addView(t1);
            layout.addView(t2);
        } else {
            //ignore for now
        }
    }

    private void createGradedTestFlashcardLayout(String category) {
        String correctAnswer = TestGrader.getCorrectAnswers()[position];
        String userAnswer = TestGrader.getUserAnswers()[position];
        boolean isAnswerCorrect = false;
        String[] allUserCheckAllAnswers = null;
        String[] allCorrectCheckAllAnswers;

        allCorrectCheckAllAnswers = correctAnswer.split(" ");

        if ((userAnswer != null) && category.equals(CheckAllThatApply.CATEGORY)) {
            isAnswerCorrect = correctAnswer.toLowerCase().equals(userAnswer.toLowerCase());
            allUserCheckAllAnswers = userAnswer.split(" ");

            if (allUserCheckAllAnswers.length == allCorrectCheckAllAnswers.length) {
                isAnswerCorrect = true;
                for (int i = 0; i < allCorrectCheckAllAnswers.length; ++i) {
                    if (!allCorrectCheckAllAnswers[i].toLowerCase().equals
                            (allUserCheckAllAnswers[i].toLowerCase())) {
                        isAnswerCorrect = false;
                        break;
                    }
                }
            }
        }


        if (category.equals(CheckAllThatApply.CATEGORY)) {
            if (userAnswer == null) {
                for (int i = 0; i < checkBoxes.size(); ++i) {
                    for (int j = 0; j < allCorrectCheckAllAnswers.length; ++j) {
                        if (allCorrectCheckAllAnswers[j].equals(checkAllTextView.get(i).getText().toString())) {
                            checkAllTextView.get(i).setTextColor(Color.GREEN);
                        }
                        checkBoxes.get(i).setEnabled(false);
                        saveAnswerButton.setEnabled(false);
                    }
                } return;
            }

            for (int i = 0; i < checkBoxes.size(); ++i) {
                for (int j = 0; j < allUserCheckAllAnswers.length; ++j) {
                    if (allUserCheckAllAnswers[j].equals(checkAllTextView.get(i).getText().toString())) {
                        checkBoxes.get(i).setChecked(true);
                        if (isAnswerCorrect && isTestGraded) {
                            checkAllTextView.get(i).setTextColor(Color.GREEN);
                        } else if (!isAnswerCorrect && isTestGraded) {
                            checkAllTextView.get(i).setTextColor(Color.RED);
                            for (int k = 0; k < checkBoxes.size(); ++k) {
                                for (int l = 0; l < allCorrectCheckAllAnswers.length; ++l) {
                                    if (allCorrectCheckAllAnswers[l].equals(checkAllTextView.get(k).getText().toString())) {
                                        checkAllTextView.get(k).setTextColor(Color.GREEN);
                                    }
                                }
                            }
                        }
                    } else {
                        checkBoxes.get(i).setEnabled(false);
                        checkAllTextView.get(i).setEnabled(false);
                    }
                }
            }
        } else if (category.equals(MultipleChoice.CATEGORY)) {

            if (userAnswer == null) {
                for (int i = 0; i < mpRadioButtons.size(); ++i) {
                    if (correctAnswer.equals(mpTextView.get(i).getText().toString())) {
                        mpTextView.get(i).setTextColor(Color.GREEN);
                    }
                    mpRadioButtons.get(i).setEnabled(false);
                    mpTextView.get(i).setEnabled(false);
                    saveAnswerButton.setEnabled(false);
                } return;
            }

            for (int i = 0; i < mpRadioButtons.size(); ++i) {
                if (userAnswer.equals(mpTextView.get(i).getText().toString())) {
                    mpRadioButtons.get(i).setChecked(true);
                    if (isAnswerCorrect && isTestGraded) {
                        mpTextView.get(i).setTextColor(Color.GREEN);
                    } else if (!isAnswerCorrect && isTestGraded){
                        mpTextView.get(i).setTextColor(Color.RED);
                        for (int j = 0; j < mpRadioButtons.size(); ++j) {
                            if (correctAnswer.equals(mpTextView.get(j).getText().toString())) {
                                mpTextView.get(j).setTextColor(Color.GREEN);
                            }
                        }
                    }
                } else {
                    mpRadioButtons.get(i).setEnabled(false);
                    mpTextView.get(i).setEnabled(false);
                }
            }
        } else if (category.equals(TrueFalse.CATEGORY)) {

            if (userAnswer == null) {
                Log.v("Is TF Null?", "YES!");
                for (int i = 0; i < tfRadioButtons.size(); ++i) {
                    if (correctAnswer.equals(tfTextView.get(i).getText().toString())) {
                        tfTextView.get(i).setTextColor(Color.GREEN);
                    }
                    tfTextView.get(i).setEnabled(false);
                    tfRadioButtons.get(i).setEnabled(false);
                    saveAnswerButton.setEnabled(false);
                } return;
            }

            Log.v("Is TF Null?", "NO!");
            for (int i = 0; i < tfRadioButtons.size(); ++i) {
                if (userAnswer.equals(tfTextView.get(i).getText().toString())) {
                    tfRadioButtons.get(i).setChecked(true);
                    if (isAnswerCorrect && isTestGraded) {
                        tfTextView.get(i).setTextColor(Color.GREEN);
                    } else if (!isAnswerCorrect && isTestGraded) {
                        tfTextView.get(i).setTextColor(Color.RED);
                        for (int j = 0; j < tfRadioButtons.size(); ++j) {
                            if (correctAnswer.equals(tfTextView.get(j).getText().toString())) {
                                tfTextView.get(j).setTextColor(Color.GREEN);
                            }
                        }
                    }
                } else {
                    tfRadioButtons.get(i).setEnabled(false);
                    tfTextView.get(i).setEnabled(false);
                }
            }
        } else {
                saTextField.setText(userAnswer);
            if (isAnswerCorrect && isTestGraded) {
                saTextField.setTextColor(Color.GREEN);
            } else if (!isAnswerCorrect && isTestGraded){
                saTextField.setTextColor(Color.RED);
                TextView correctAnswerTextView = new TextView(this);
                correctAnswerTextView.setText(correctAnswer);
                correctAnswerTextView.setTextColor(Color.GREEN);
                layout.addView(correctAnswerTextView);
            }
            saTextField.setEnabled(false);
        }
        saveAnswerButton.setEnabled(false);
    }

    private void getUserAnswer(final String category) {
        saveAnswerButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                       if (category.equals(CheckAllThatApply.CATEGORY)) {
                           String checkAllAnswer = "";
                           for (int i = 0; i < checkBoxes.size(); ++i) {
                               if (checkBoxes.get(i).isChecked()) {
                                   if (!checkAllTextView.get(i).getText().toString().isEmpty())
                                       checkAllAnswer += checkAllTextView.get(i).getText().toString() +
                                           " ";
                               }
                               checkBoxes.get(i).setEnabled(false);
                           }

                           if (checkAllAnswer.isEmpty() || checkAllAnswer.length() == 0)
                               checkAllAnswer = null;

                           TestGrader.addUserAnswer(checkAllAnswer, position);
                       } else if (category.equals(MultipleChoice.CATEGORY)) {
                           boolean answerFound = false;
                           for (int i = 0; i < mpRadioButtons.size(); ++i) {
                               if (mpRadioButtons.get(i).isChecked()) {
                                    TestGrader.addUserAnswer(mpTextView.get(i).getText()
                                            .toString(), position);
                                    answerFound = true;
                               }

                               mpRadioButtons.get(i).setEnabled(false);
                           }
                           if (!answerFound)
                               TestGrader.addUserAnswer(null, position);
                       } else if (category.equals(TrueFalse.CATEGORY)) {
                           boolean answerFound = false;
                           for (int i = 0; i < tfRadioButtons.size(); ++i) {
                               if (tfRadioButtons.get(i).isChecked()) {
                                    TestGrader.addUserAnswer(tfTextView.get(i).getText()
                                            .toString(), position);

                                   answerFound = true;
                               }

                               tfRadioButtons.get(i).setEnabled(false);
                           }

                           if (!answerFound) {
                               TestGrader.addUserAnswer(null, position);

                           }
                       } else {
                           if (saTextField.getText().toString().equals(null) || saTextField.getText().toString().isEmpty()) {
                               TestGrader.addUserAnswer(null, position);
                           } else {
                               TestGrader.addUserAnswer(saTextField.getText().toString(), position);
                           }
                           saTextField.setEnabled(false);
                       }
                        saveAnswerButton.setEnabled(false);
                    }
                });
    }

    public void showAnswer(View v) {
        if(tapped == true && !callingClass.contains("FlashcardListForTestsActivity")) {
            testTextView.setText(card.getAnswer());
            header.setText("ANSWER");
            tapped = false;
        } else if (tapped == false && !callingClass.contains("FlashcardListForTestsActivity")){
            testTextView.setText(card.getQuestion());
            header.setText("QUESTION");
            tapped = true;

        }
    }
}
