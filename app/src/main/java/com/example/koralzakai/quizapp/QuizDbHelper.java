package com.example.koralzakai.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.example.koralzakai.quizapp.QuizContract.CategoriesTable;
import com.example.koralzakai.quizapp.QuizContract.QuestionsTable;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION = 1;

    //inorder to avoid memory leak we will make the DBhelper class singelton
    private static QuizDbHelper instance;

    private SQLiteDatabase db;// referance to the database
    //constractor
    private QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized QuizDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new QuizDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
//create categorical table
        final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                CategoriesTable.TABLE_NAME + "( " +
                CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoriesTable.COLUMN_NAME + " TEXT " +
                ")";
//create Question table
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT, " +
                QuestionsTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + QuestionsTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                CategoriesTable.TABLE_NAME + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                ")";

        db.execSQL(SQL_CREATE_CATEGORIES_TABLE);//create the data base just one time, at the create of the app.
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillCategoriesTable();
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }
//called every time we open/ edit the data base
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //avoid insert question that is not in the categories that we have in the categorical table
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategoriesTable() {
        Category c1 = new Category("Programming");
        insertCategory(c1);
        Category c2 = new Category("Geography");
        insertCategory(c2);
        Category c3 = new Category("Music");
        insertCategory(c3);
    }

    public void addCategory(Category category) {
        db = getWritableDatabase();
        insertCategory(category);
    }

    public void addCategories(List<Category> categories) {
        db = getWritableDatabase();

        for (Category category : categories) {
            insertCategory(category);
        }
    }

    private void insertCategory(Category category) {
        ContentValues cv = new ContentValues();
        cv.put(CategoriesTable.COLUMN_NAME, category.getName());
        db.insert(CategoriesTable.TABLE_NAME, null, cv);
    }

    private void fillQuestionsTable() {
        Question q1 = new Question("Programming, Hard: What kind of design pattern Broadcast receiver  use?" ,
                "Publish-Subscribe", "Singleton", "Adapter", 1,
                Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        insertQuestion(q1);

        Question q11 = new Question("Programming, Medium: What kind of design pattern Broadcast receiver  use?" ,
                "Publish-Subscribe", "Singleton", "Adapter", 1,
                Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        insertQuestion(q11);

        Question q12 = new Question("Programming, Easy: What kind of design pattern Broadcast receiver  use?" ,
                "Publish-Subscribe", "Singleton", "Adapter", 1,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(q12);

        Question q2 = new Question("Geography, Easy: Where is Jerusalem beach?",
                "Jerusalem", "Tel Aviv", "Asdod", 2,
                Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        insertQuestion(q2);

        Question q21 = new Question("Geography, Medium: Where is Jerusalem beach?",
                "Jerusalem", "Tel Aviv", "Asdod", 2,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        insertQuestion(q21);

        Question q22 = new Question("Geography, Hard: Where is Jerusalem beach?",
                "Jerusalem", "Tel Aviv", "Asdod", 2,
                Question.DIFFICULTY_HARD, Category.GEOGRAPHY);
        insertQuestion(q22);

        Question q3 = new Question("Music, Hard: What is the name of the song?",
                "Crazy", "Amazing", "Livin' On The Edge", 3,
                Question.DIFFICULTY_HARD, Category.MUSIC);
        insertQuestion(q3);

        Question q4 = new Question("Music, Easy: In what year this song has been written?",
                "2000", "1998", "2003", 1,
                Question.DIFFICULTY_EASY, Category.MUSIC);
        insertQuestion(q4);

        Question q5 = new Question("Programming, Easy: Inorder to use malloc you need to include:",
                "stdlib", "stdio", "string", 1,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(q5);

        Question q51 = new Question("Programming, Medium: Inorder to use malloc you need to include:",
                "stdlib", "stdio", "string", 1,
                Question.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
        insertQuestion(q51);

        Question q52 = new Question("Programming, Hard: Inorder to use malloc you need to include:",
                "stdlib", "stdio", "string", 1,
                Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        insertQuestion(q52);

        Question q6 = new Question("Geography, Medium: How many people live in USA?",
                "380 million", "327 million", "200 million", 2,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        insertQuestion(q6);

        Question q61 = new Question("Geography, Hard: How many people live in USA?",
                "380 million", "327 million", "200 million", 2,
                Question.DIFFICULTY_HARD, Category.GEOGRAPHY);
        insertQuestion(q61);

        Question q62 = new Question("Geography, Easy: How many people live in USA?",
                "380 million", "327 million", "200 million", 2,
                Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        insertQuestion(q62);

        Question q7 = new Question("Music, Medium: With who Red Band sing with?",
                "Siri Mimon", "Marina Maximilian", "Ninet Tayeb", 2,
                Question.DIFFICULTY_MEDIUM, Category.MUSIC);
        insertQuestion(q7);

        Question q8 = new Question("Music, Easy: What is the name of the song?",
                "Take me home", "Paradise country", "Paradise city", 3,
                Question.DIFFICULTY_EASY, Category.MUSIC);
        insertQuestion(q8);

        Question q9 = new Question("Programming, Hard: Which of the following is a legal identifier in assembly language?",
                "10percent", "a1a2a3...a247a248", "july_2012", 3,
                Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        insertQuestion(q9);

        Question q10 = new Question("Programming, Easy: Which of these is a Python control flow statement type?",
                "else if", "elif", "elsif", 2,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(q10);

        Question q13 = new Question("Programming, Easy: In C++ Which of the following operators has the highest precedence?",
                "!", "&&", "!=", 1,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(q13);

        Question q14 = new Question("Programming, Easy: Which of the following data structures falls under the category of a 'dictionary'?",
                "Hash table", "Linked list", "Hash", 1,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(q14);

        Question q15 = new Question("Programming, Hard: In 1983, this person was the first to offer a definition of the term 'computer virus'.",
                "McAfee", "Cohen", "Norton", 2,
                Question.DIFFICULTY_HARD, Category.PROGRAMMING);
        insertQuestion(q15);

        Question q16 = new Question("Programming, Easy: Which of these structures does not exist in Python?",
                "Dictionary", "List", "Array", 3,
                Question.DIFFICULTY_EASY, Category.PROGRAMMING);
        insertQuestion(q16);

        Question q17 = new Question("Geography, Hard: What is Earth's largest continent?",
                "Asia", "Africa", "Europe", 1,
                Question.DIFFICULTY_HARD, Category.GEOGRAPHY);
        insertQuestion(q17);

        Question q18 = new Question("Geography, Medium:  What razor-thin country accounts for more than half of the western coastline of South America?",
                "Peru", "Bolivia", "Chile", 3,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        insertQuestion(q18);

        Question q19 = new Question("Geography, Medium: What is Earth's largest continent?",
                "Asia", "Africa", "Europe", 1,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        insertQuestion(q19);

        Question q20 = new Question("Geography, Hard: What river runs through Baghdad?",
                "Tigris", "Karun", "Jordan", 1,
                Question.DIFFICULTY_HARD, Category.GEOGRAPHY);
        insertQuestion(q20);

        Question q23 = new Question("Geography, Easy: What river runs through Baghdad?",
                "Tigris", "Karun", "Jordan", 1,
                Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        insertQuestion(q23);

        Question q24 = new Question("Geography, Easy: What country has the most natural lakes?",
                "India", "Canada", "USA", 2,
                Question.DIFFICULTY_EASY, Category.GEOGRAPHY);
        insertQuestion(q24);

        Question q25 = new Question("Geography, Medium: What country has the most natural lakes?",
                "India", "Canada", "USA", 2,
                Question.DIFFICULTY_MEDIUM, Category.GEOGRAPHY);
        insertQuestion(q25);
    }

    public void addQuestion(Question question) {
        db = getWritableDatabase();
        insertQuestion(question);
    }

    public void addQuestions(List<Question> questions) {
        db = getWritableDatabase();

        for (Question question : questions) {
            insertQuestion(question);
        }
    }
    //insert question into the data base.
    private void insertQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionsTable.COLUMN_CATEGORY_ID, question.getCategoryID());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(CategoriesTable._ID)));
                category.setName(c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                categoryList.add(category);
            } while (c.moveToNext());
        }

        c.close();
        return categoryList;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        //all the question without considering the difficulty & category
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(int categoryID, String difficulty) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        //all the question with considering the difficulty & category
        String selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? " +
                " AND " + QuestionsTable.COLUMN_DIFFICULTY + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(categoryID), difficulty};
        //return all the questions with the given difficulty and category.
        Cursor c = db.query(
                QuestionsTable.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(c.getInt(c.getColumnIndex(QuestionsTable._ID)));
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategoryID(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}
