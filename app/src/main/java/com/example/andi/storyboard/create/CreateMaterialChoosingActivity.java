package com.example.andi.storyboard.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.example.andi.storyboard.CreateStoryActivity;

import com.example.andi.storyboard.R;

public class CreateMaterialChoosingActivity extends AppCompatActivity {

//    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        auth = FirebaseAuth.getInstance();
//        if (auth.getCurrentUser() == null) {
//            finish();
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_material_choosing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button goto_create_story_button = findViewById(R.id.goto_create_story_button);
        goto_create_story_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreateStoryActivity.class);
                startActivity(intent);
                finish();

            }
        });

        Button goto_create_prompt_button = findViewById(R.id.goto_create_prompt_button);
        goto_create_prompt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                Intent intent = new Intent(getBaseContext(), MainActivity.class);
//                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_side_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;

        switch(id) {
            case R.id.action_settings:
                return true;
            case R.id.back_button:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
