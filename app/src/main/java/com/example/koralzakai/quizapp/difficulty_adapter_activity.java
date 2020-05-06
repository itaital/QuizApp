package com.example.koralzakai.quizapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class difficulty_adapter_activity extends AppCompatActivity {
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";
    //initialize view's
    ListView simpleListView;
    String[] difficultyArry = {"Easy", "Medium", "Hard"};
    String[] difficultyImages = {"  *","**","  ***"};//fruits images
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_adapter_activity);
        simpleListView=(ListView)findViewById(R.id.simpleListView);

        ArrayList<HashMap<String,String>> arrayList=new ArrayList<>();
        for (int i=0;i<difficultyArry.length;i++)
        {
            HashMap<String,String> hashMap=new HashMap<>();//create a hashmap to store the data in key value pair
            hashMap.put("name",difficultyArry[i]);
            hashMap.put("image",difficultyImages[i]+"");
            arrayList.add(hashMap);//add the hashmap into arrayList
        }
        String[] from={"name","image"};//string array
        int[] to={R.id.textView,R.id.starView};//int array of views id's
        SimpleAdapter simpleAdapter=new SimpleAdapter(this,arrayList,R.layout.list_view_item,from,to);//Create object and set the parameters for simpleAdapter
        simpleListView.setAdapter(simpleAdapter);//sets the adapter for listView

        //perform listView item click event
        simpleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(),difficultyArry[i],Toast.LENGTH_LONG).show();//show the selected image in toast according to position
                Intent resultIntent = new Intent(); // for the first screen
                resultIntent.putExtra(EXTRA_DIFFICULTY, difficultyArry[i]); // return the the name
                setResult(RESULT_OK, resultIntent); //return difficulty string
                finish();
            }
        });
    }
}
