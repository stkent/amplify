package com.github.stkent.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.stkent.amplify.prompt.interfaces.IPromptView;
import com.github.stkent.amplify.tracking.Amplify;

public class MainActivity extends AppCompatActivity {

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Amplify.get(this).promptIfReady(
                this,
                (IPromptView) findViewById(R.id.default_layout_prompt_view));

        Amplify.get(this).promptIfReady(
                this,
                (IPromptView) findViewById(R.id.custom_layout_prompt_view));
    }

}
