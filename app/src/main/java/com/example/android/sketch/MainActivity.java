package com.example.android.sketch;

import android.content.Context;
import android.graphics.PorterDuff;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    //public static final int PORT = 53624;
    private Button resetBtn;
    private DrawingView drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = (DrawingView)findViewById(R.id.drawing);
        Button resetBtn = (Button)findViewById(R.id.reset_button);
        resetBtn.setOnClickListener(this);
        Button undoBtn = (Button)findViewById(R.id.undo_button);
        undoBtn.setOnClickListener(this);
        Button finBtn = (Button)findViewById(R.id.finish_button);
        finBtn.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        if(view.getId()==R.id.reset_button){
            drawView.startNew("reset");
        }
        else if(view.getId()==R.id.undo_button){
            drawView.undo();
        }
        else if(view.getId()==R.id.finish_button){
            drawView.startNew("new");
        }
    }

}
