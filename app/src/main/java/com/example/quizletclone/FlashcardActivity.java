package com.example.quizletclone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.view.WindowManager;
import android.widget.*;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;

/**
 * Created by jefflau on 11/3/15.
 */
public class FlashcardActivity extends AppCompatActivity {
    private ModelViewController mvc;
    private int position, testPosition;
    private boolean tapped = true, isFlashcardGraded = false, isTestGraded = false;
    private String callingClass;
    private TextView testTextView, header;
    private LinearLayout layout;
    private Flashcard card;
    private ArrayList<CheckBox> checkBoxes;
    private ArrayList<TextView> checkAllTextView, mpTextView, tfTextView;
    private ArrayList<RadioButton> mpRadioButtons, tfRadioButtons;
    private EditText saTextField;
    private Button saveAnswerButton;
    private final int MULTIPLE_CHOICE_LIMIT = 5, CHECK_ALL_LIMIT = 5, TRUE_FALSE_LIMIT = 2;
    private final String PLACEHOLDER = "   ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        // gets intent to obtain position of card clicked
        Intent intent = getIntent();
        mvc = ModelViewController.getInstance(this);

        // the position of the cardview clicked
        position = intent.getIntExtra("POS", 0);
        callingClass = intent.getStringExtra("callingClass");

        layout = (LinearLayout) findViewById(R.id.layout);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                hideKeyboard(view);
                return false;
            }
        });


        // set the question onto flashcard
        testTextView = (TextView) findViewById(R.id.testTextView);


        if (!mvc.isTag)
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
                final TextView textViewToChange = (TextView) findViewById(R.id.flashcardNum);
                textViewToChange.setText(
                        "Flashcard " + (position+1) + "/" + mvc.getFlashcards().size());
                createBasicFlashcardLayout(mvc.getFlashcards().get(position).getCategory());
            } else if (callingClass.contains("FlashcardListForTestsActivity")) {
                isTestGraded = TestGrader.isTestGraded();
                if (TestGrader.getAnswersBeingGraded()[position] == true)
                    isFlashcardGraded = true;

                testPosition = intent.getIntExtra("TestPOS", 0);
                testTextView.setText(mvc.getTests().get(testPosition).getSetOfFlashcards().get(position).getQuestion());
                String category = mvc.getTests().get(testPosition).getSetOfFlashcards().get(position).getCategory();

                final TextView textViewToChange = (TextView) findViewById(R.id.flashcardNum);
                textViewToChange.setText(
                        "Flashcard " + (position+1) + "/" + mvc.getTests().get(testPosition).getSetOfFlashcards().size());

                /* If the test has not been graded, call createTestFlashcardLayout, else call
                    createGradedTestFlashcardLayout.
                 */
                createTestFlashcardLayout(category);
                getUserAnswer(category);

                if (isFlashcardGraded || isTestGraded)
                    createGradedTestFlashcardLayout(category);
            }
        } else {
            testTextView.setText(card.getQuestion());
            final TextView textViewToChange = (TextView) findViewById(R.id.flashcardNum);
            textViewToChange.setText(
                    "Flashcard " + (position+1) + "/" + mvc.sortedCard.size());
            createBasicFlashcardLayout(card.getCategory());
            mvc.isTag = false;      //toggle the bool back for other activity
        }

    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /* Create a test flashcard layout depending on category type */
    private void createTestFlashcardLayout(final String category) {
        /*
         * mp = multiple choice (meant to write mc...)
         * tf = true false
         * sa = short answer
         *
         * For each category, an arrayList of widgets is created and added to a separate
         * LinearLayout named row. This LinearLayout has the vertical orientation. The row layout
         * is then added to the parent Layout which has a horizontal orientation.
         *
         * Any data needed to fill the widgets is called from the ModelViewController class.
         *
         * saveAnswerButton is added at the end and resized.
         */
        if (category.equals(CheckAllThatApply.CATEGORY)) {
            checkBoxes = new ArrayList<CheckBox>();
            checkAllTextView = new ArrayList<TextView>();

            for (int i = 0; i < CHECK_ALL_LIMIT; ++i) {
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                row.setOrientation(LinearLayout.HORIZONTAL);

                checkBoxes.add(new CheckBox(this));
                checkAllTextView.add(new TextView(this));
                checkAllTextView.get(i).setText(mvc.getTests().get(testPosition).
                        getSetOfFlashcards().get(position).getListOfAnswers().get(i));

                row.addView(checkBoxes.get(i));
                row.addView(checkAllTextView.get(i));
                layout.addView(row);
            }
        } else if (category.equals(MultipleChoice.CATEGORY)) {
            mpRadioButtons = new ArrayList<RadioButton>();
            mpTextView = new ArrayList<TextView>();

            for (int i = 0; i < MULTIPLE_CHOICE_LIMIT; ++i) {
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.
                        WRAP_CONTENT));
                row.setOrientation(LinearLayout.HORIZONTAL);

                mpRadioButtons.add(new RadioButton(this));
                final RadioButton currentButton = mpRadioButtons.get(i);

                currentButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (currentButton.isChecked()) {
                            for (int j = 0; j < MULTIPLE_CHOICE_LIMIT; ++j) {
                                if (mpRadioButtons.get(j) != currentButton)
                                    mpRadioButtons.get(j).setChecked(false);
                            }
                        }

                    }
                });

                mpTextView.add(new TextView(this));
                mpTextView.get(i).setText(mvc.getTests().get(testPosition).
                        getSetOfFlashcards().get(position).getListOfAnswers().get(i));

                row.addView(mpRadioButtons.get(i));
                row.addView(mpTextView.get(i));
                layout.addView(row);
            }
        } else if (category.equals(TrueFalse.CATEGORY)) {
            tfRadioButtons = new ArrayList<RadioButton>();
            tfTextView = new ArrayList<TextView>();

            for (int i = 0; i < TRUE_FALSE_LIMIT; ++i) {
                LinearLayout row = new LinearLayout(this);
                row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                row.setOrientation(LinearLayout.HORIZONTAL);

                tfRadioButtons.add(new RadioButton(this));
                final RadioButton currentButton = tfRadioButtons.get(i);

                currentButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (currentButton.isChecked()) {
                            for (int j = 0; j < TRUE_FALSE_LIMIT; ++j) {
                                if (tfRadioButtons.get(j) != currentButton)
                                    tfRadioButtons.get(j).setChecked(false);
                            }
                        }
                    }
                });

                tfTextView.add(new TextView(this));
                tfTextView.get(i).setText(mvc.getTests().get(testPosition).
                        getSetOfFlashcards().get(position).getListOfAnswers().get(i));

                row.addView(tfRadioButtons.get(i));
                row.addView(tfTextView.get(i));
                layout.addView(row);
            }
        } else {
            saTextField = new EditText(this);
            layout.addView(saTextField);
        }

        saveAnswerButton = new Button(this);
        saveAnswerButton.setText("Save Answer");
        layout.addView(saveAnswerButton);

        ViewGroup.LayoutParams params = saveAnswerButton.getLayoutParams();
        params.width = 400;
        saveAnswerButton.setLayoutParams(params);
    }

    /* Create a normal flashcard depending on layout */
    private void createBasicFlashcardLayout(String category) {
        if (category.equals(CheckAllThatApply.CATEGORY)) {
            ArrayList<TextView> textViews = new ArrayList<TextView>();
            String[] labels = {"A: ", "B: ", "C: ", "D: ", "E: "};

            for (int i = 0; i < CHECK_ALL_LIMIT; ++i) {
                textViews.add(new TextView(this));
                textViews.get(i).setText(labels[i] + card.getListOfAnswers().get(i));
                layout.addView(textViews.get(i));
            }
        } else if (category.equals(MultipleChoice.CATEGORY)) {
            ArrayList<TextView> textViews = new ArrayList<TextView>();
            String[] labels = {"A: ", "B: ", "C: ", "D: ", "E: "};

            for (int i = 0; i < MULTIPLE_CHOICE_LIMIT; ++i) {
                textViews.add(new TextView(this));
                textViews.get(i).setText(labels[i] + card.getListOfAnswers().get(i));
                layout.addView(textViews.get(i));
            }
        } else if (category.equals(TrueFalse.CATEGORY)) {
            ArrayList<TextView> textViews = new ArrayList<TextView>();
            String[] labels = {"A: ", "B: "};

            for (int i = 0; i < TRUE_FALSE_LIMIT; ++i) {
                textViews.add(new TextView(this));
                textViews.get(i).setText(labels[i] + card.getListOfAnswers().get(i));
                layout.addView(textViews.get(i));
            }
        } else {
            //ignore for now
        }
    }

    /* Creates a flashcard layout for cards that have already been graded, depending on category */
    private void createGradedTestFlashcardLayout(String category) {
        String correctAnswer = TestGrader.getCorrectAnswers()[position];
        String userAnswer = TestGrader.getUserAnswers()[position];
        boolean isAnswerCorrect = false;
        String[] allUserCheckAllAnswers = null;
        String[] allCorrectCheckAllAnswers;

        /* Special case for checkAllThatApply. Since checkAllThatApply has multiple answers, its
            answers need to be stored into an array.
         */
        allCorrectCheckAllAnswers = correctAnswer.split(PLACEHOLDER);

        /* Before preceding, a check is done to make sure the array containing answers from
           from the user matches the correct answers 1-to-1.
         */
        if ((userAnswer != null) && category.equals(CheckAllThatApply.CATEGORY)) {
            isAnswerCorrect = correctAnswer.toLowerCase().equals(userAnswer.toLowerCase());
            allUserCheckAllAnswers = userAnswer.split(PLACEHOLDER);

            isAnswerCorrect = TestGrader.checkArrayAnswer(allUserCheckAllAnswers, allCorrectCheckAllAnswers);
        } else if (userAnswer != null && !category.equals(CheckAllThatApply.CATEGORY)) {
            isAnswerCorrect = TestGrader.checkAnswer(userAnswer, correctAnswer);
        }

        /* Every category (except short answer) follows the same (messy) format.
            1) Check if the user provided answer is null. If the user did not provide an answer,
            by default it is null. This is an error check against users providing blank answers or
            forgetting to select an answer.

            If the answer is null, loop through the possible answers and highlight the correct ones
            by setting their text color to green. We only set the text to green if the card has
            already been graded. This is checked by calling TestGrader.isTestGraded(). If it has not
            been graded, nothing is highlighted. All possible options are then grayed out by calling
            setEnabled() and setting it to false.

            2) If the user answer is NOT null, highlight the options selected by the user by calling
                setChecked and setting it to true. If the user answer is correct, and test has been
                graded, highlight the user answer by setting the text color to green. If the answer
                is wrong and the test has been graded, highlight the user answer by setting the text
                to red. Any correct answers that are missed are then highlighted afterwards in the
                last for loop. If we are not in the testGrading phase, jump to the first else state-
                ment and gray out the options. This means the card is awaiting to be graded so the
                answers are now "locked".

                SaveAnswer button is grayed out regardless as the user can only save their answer
                once per test.

                These blocks essentially either highlight the correct and incorrect answers OR gray
                out the answer options to simulate a "lock". This means the user has already
                answered the card and cannot change anything until it is graded/test is cleared.
         */
        if (category.equals(CheckAllThatApply.CATEGORY)) {
            if (userAnswer == null) {
                for (int i = 0; i < checkBoxes.size(); ++i) {
                    for (int j = 0; j < allCorrectCheckAllAnswers.length; ++j) {
                        if (allCorrectCheckAllAnswers[j].equals(checkAllTextView.get(i)
                                .getText().toString()) && TestGrader.isTestGraded()) {
                            checkAllTextView.get(i).setTextColor(Color.rgb(0,204,0));
                        }
                        checkBoxes.get(i).setEnabled(false);
                        saveAnswerButton.setEnabled(false);
                    }
                }
                return;
            }

            for (int i = 0; i < checkBoxes.size(); ++i) {
                for (int j = 0; j < allUserCheckAllAnswers.length; ++j) {
                    if (allUserCheckAllAnswers[j].equals(checkAllTextView.get(i).getText()
                            .toString())) {
                        checkBoxes.get(i).setChecked(true);
                        if (isAnswerCorrect && isTestGraded) {
                            checkAllTextView.get(i).setTextColor(Color.rgb(0,204,0));
                        } else if (!isAnswerCorrect && isTestGraded) {
                            checkAllTextView.get(i).setTextColor(Color.RED);
                            for (int k = 0; k < checkBoxes.size(); ++k) {
                                for (int l = 0; l < allCorrectCheckAllAnswers.length; ++l) {
                                    if (allCorrectCheckAllAnswers[l].equals(checkAllTextView.get(k)
                                            .getText().toString())) {
                                        checkAllTextView.get(k).setTextColor(Color.rgb(0,204,0));
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
                    if (correctAnswer.equals(mpTextView.get(i).getText().toString()) &&
                            TestGrader.isTestGraded()) {
                        mpTextView.get(i).setTextColor(Color.rgb(0,204,0));
                    }
                    mpRadioButtons.get(i).setEnabled(false);
                    mpTextView.get(i).setEnabled(false);
                    saveAnswerButton.setEnabled(false);
                }
                return;
            }

            for (int i = 0; i < mpRadioButtons.size(); ++i) {
                if (userAnswer.equals(mpTextView.get(i).getText().toString())) {
                    mpRadioButtons.get(i).setChecked(true);
                    if (isAnswerCorrect && isTestGraded) {
                        mpTextView.get(i).setTextColor(Color.rgb(0,204,0));
                    } else if (!isAnswerCorrect && isTestGraded) {
                        mpTextView.get(i).setTextColor(Color.RED);
                        for (int j = 0; j < mpRadioButtons.size(); ++j) {
                            if (correctAnswer.equals(mpTextView.get(j).getText().toString())) {
                                mpTextView.get(j).setTextColor(Color.rgb(0,204,0));
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
                for (int i = 0; i < tfRadioButtons.size(); ++i) {
                    if (correctAnswer.equals(tfTextView.get(i).getText().toString())
                            && TestGrader.isTestGraded()) {
                        tfTextView.get(i).setTextColor(Color.rgb(0,204,0));
                    }
                    tfTextView.get(i).setEnabled(false);
                    tfRadioButtons.get(i).setEnabled(false);
                    saveAnswerButton.setEnabled(false);
                }
                return;
            }

            for (int i = 0; i < tfRadioButtons.size(); ++i) {
                if (userAnswer.equals(tfTextView.get(i).getText().toString())) {
                    tfRadioButtons.get(i).setChecked(true);
                    if (isAnswerCorrect && isTestGraded) {
                        tfTextView.get(i).setTextColor(Color.rgb(0,204,0));
                    } else if (!isAnswerCorrect && isTestGraded) {
                        tfTextView.get(i).setTextColor(Color.RED);
                        for (int j = 0; j < tfRadioButtons.size(); ++j) {
                            if (correctAnswer.equals(tfTextView.get(j).getText().toString())) {
                                tfTextView.get(j).setTextColor(Color.rgb(0,204,0));
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
                saTextField.setTextColor(Color.rgb(0,204,0));
            } else if (!isAnswerCorrect && isTestGraded) {
                saTextField.setTextColor(Color.RED);
                TextView correctAnswerTextView = new TextView(this);
                correctAnswerTextView.setText(correctAnswer);
                correctAnswerTextView.setTextColor(Color.rgb(0,204,0));
                layout.addView(correctAnswerTextView);
            }
            saTextField.setEnabled(false);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        saveAnswerButton.setEnabled(false);
    }

    /* Retrieve the user inputted answer for a flashcard depending on category. Answers are only
     * retrieved when the user selects the Save Answer option.
     */
    private void getUserAnswer(final String category) {
        /* Each block follows a similar pattern. First, the options/fields are checked to see if
            they have been selected or written to. If so, the answer is stored and sent to the
            TestGrader to be stored as a user answer. One check is done at the end incase the user
            enters a blank answer or doesn't select anything at all. In that case, null is set as
            the answer. Regardless of input, all options are then grayed out by setting setEnabled
            to false. This locks the answer options/fields so the user cannot modify them while the
            test is being graded.
         */
        final String correctAnswer = TestGrader.getCorrectAnswers()[position];
        saveAnswerButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if (category.equals(CheckAllThatApply.CATEGORY)) {
                            String checkAllAnswer = "";
                            for (int i = 0; i < checkBoxes.size(); ++i) {
                                if (checkBoxes.get(i).isChecked()) {
                                    if (!checkAllTextView.get(i).getText().toString().isEmpty())
                                        checkAllAnswer += checkAllTextView.get(i).getText().toString() +
                                                PLACEHOLDER;
                                }
                                checkBoxes.get(i).setEnabled(false);
                            }

                            if (checkAllAnswer.isEmpty() || checkAllAnswer.length() == 0)
                                checkAllAnswer = null;

                            TestGrader.addUserAnswer(checkAllAnswer, position);
                            if (TestGrader.checkArrayAnswer(checkAllAnswer.split(PLACEHOLDER),
                                    TestGrader.getCorrectAnswers()[position].split(PLACEHOLDER)))
                                TestGrader.increaseScore();

                        } else if (category.equals(MultipleChoice.CATEGORY)) {
                            boolean answerFound = false;
                            String userAnswer = null;
                            for (int i = 0; i < mpRadioButtons.size(); ++i) {
                                if (mpRadioButtons.get(i).isChecked()) {
                                    userAnswer = mpTextView.get(i).getText().toString();
                                    TestGrader.addUserAnswer(userAnswer, position);
                                    answerFound = true;
                                }

                                mpRadioButtons.get(i).setEnabled(false);
                            }
                            if (!answerFound) {
                                TestGrader.addUserAnswer(null, position);
                            }

                            if (TestGrader.checkAnswer(userAnswer, correctAnswer)) {
                                TestGrader.increaseScore();
                            }
                        } else if (category.equals(TrueFalse.CATEGORY)) {
                            boolean answerFound = false;
                            String userAnswer = null;
                            for (int i = 0; i < tfRadioButtons.size(); ++i) {
                                if (tfRadioButtons.get(i).isChecked()) {
                                    userAnswer = tfTextView.get(i).getText().toString();
                                    TestGrader.addUserAnswer(userAnswer, position);

                                    answerFound = true;
                                }
                                tfRadioButtons.get(i).setEnabled(false);
                            }

                            if (!answerFound) {
                                TestGrader.addUserAnswer(null, position);
                            }

                            if (TestGrader.checkAnswer(userAnswer, correctAnswer))
                                TestGrader.increaseScore();
                        } else {
                            if (saTextField.getText().toString() == null || saTextField.getText().toString().isEmpty()) {
                                TestGrader.addUserAnswer(null, position);
                            } else {
                                TestGrader.addUserAnswer(saTextField.getText().toString(), position);
                            }
                            saTextField.setEnabled(false);
                            if (TestGrader.checkAnswer(saTextField.getText().toString(), correctAnswer)) {
                                TestGrader.increaseScore();
                            }
                        }
                        saveAnswerButton.setEnabled(false);
                        nextCard(view); // goes to next card after clicking save
                    }
                });
    }

    public void showAnswer(View v) {
        if (tapped == true && !callingClass.contains("FlashcardListForTestsActivity")) {
            testTextView.setText(card.getAnswer());
            header.setText("ANSWER");
            tapped = false;
        } else if (tapped == false && !callingClass.contains("FlashcardListForTestsActivity")) {
            testTextView.setText(card.getQuestion());
            header.setText("QUESTION");
            tapped = true;

        }
    }

    public void nextCard(View v) {

        finish(); // closes previous activities
        if (position < mvc.getFlashcards().size() - 1 &&
                callingClass.contains("ListActivity")) {
            position++;
            displayCardListActivity(v);

        } else if (callingClass.contains("FlashcardListForTestsActivity") && position < mvc.getTests().get(testPosition).getSetOfFlashcards().size() - 1) {
            position++;
            displayCardTestActivity(v);

        } else if (callingClass.contains("FlashcardlistForTag") && position < mvc.sortedCard.size() - 1) {
            System.out.println("position = "+position+" sorted size = "+mvc.sortedCard.size());
            position++;
            displayCardTagActivity(v);
        } else {
            System.out.println("end of deck");
        }
    }


    public void prevCard(View v) {
        finish(); // closes previous activities
        if (position > 0 &&
                callingClass.contains("ListActivity")) {
            position--;
            displayCardListActivity(v);
        } else if (position > 0 &&
                callingClass.contains("FlashcardListForTestsActivity")) {
            position--;
            displayCardTestActivity(v);
        } else if (position > 0 && callingClass.contains("FlashcardlistForTag")) {
            position--;
            displayCardTagActivity(v);
        } else {
            System.out.println("end of deck");
        }
    }

    public void displayCardTestActivity(View v) {
        Intent intent = new Intent(v.getContext(), FlashcardActivity.class);
        intent.putExtra("callingClass", FlashcardListForTestsActivity.class.toString());
        intent.putExtra("TestPOS", FlashcardListForTestsActivity.index);
        intent.putExtra("POS", position);
        v.getContext().startActivity(intent);
        finish();
    }

    public void displayCardListActivity(View v) {
        Intent intent = new Intent(v.getContext(), FlashcardActivity.class);
        intent.putExtra("POS", position);
        intent.putExtra("callingClass", ListActivity.class.toString());
        v.getContext().startActivity(intent);
        finish();
    }

    public void displayCardTagActivity(View v) {
        Intent intent = new Intent(v.getContext(), FlashcardActivity.class);
        intent.putExtra("POS", position);
        intent.putExtra("callingClass", FlashcardlistForTag.class.toString());
        mvc.isTag = true;
        v.getContext().startActivity(intent);
        finish();
    }
}
