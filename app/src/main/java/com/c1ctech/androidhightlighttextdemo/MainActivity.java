package com.c1ctech.androidhightlighttextdemo;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private int paragraphCount = 0;
    private ArrayList<String> stringArrayList = new ArrayList<>();
    TextView textView;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv_text);

        //creating TTS instance
        tts = new TextToSpeech(this, this);
        textView.setText(getString(R.string.text));
    }

    private void speakText() {
        if (paragraphCount == 0) {
            stringArrayList = new ArrayList<>(Arrays.asList(textView.getText().toString().split("\n")));
        }
        try {
            SpannableString spannableString = new SpannableString(textView.getText().toString());
            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimaryDark)),
                    0, textView.getText().toString().length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);


            spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)),
                    textView.getText().toString().indexOf(stringArrayList.get(paragraphCount)),
                    textView.getText().toString().indexOf(stringArrayList.get(paragraphCount)) +
                            stringArrayList.get(paragraphCount).length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            tts.speak(stringArrayList.get(paragraphCount), TextToSpeech.QUEUE_FLUSH, null, "UniqueID");

            textView.setText(spannableString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Called to signal the completion of the TextToSpeech engine initialization.
    @Override
    public void onInit(int i) {

        //Listener for events relating to the progress of an utterance
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {

            //called when speaking starts
            @Override
            public void onStart(String utteranceId) {
                Log.i("TTS", "utterance started");
            }

            //called when speaking is finished.
            @Override
            public void onDone(String utteranceId) {
                if (stringArrayList.size() - 1 != paragraphCount) {
                    paragraphCount++;
                    speakText();
                } else {
                    paragraphCount = 0;
                }
                Log.i("TTS", "utterance done");
            }

            //called when an error has occurred during processing.
            @Override
            public void onError(String utteranceId) {
                Log.i("TTS", "utterance error");
            }

        });
    }

    public void speakClicked(View ignored) {

        speakText();
    }


}
