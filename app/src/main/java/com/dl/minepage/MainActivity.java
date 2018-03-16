package com.dl.minepage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dl.minepage.view.MineWaveView;

public class MainActivity extends Activity {

    private MineWaveView waveView;
    private ImageView chuan, chuan2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waveView = findViewById(R.id.waveview);
        chuan = findViewById(R.id.chuan);
        chuan2 = findViewById(R.id.chuan2);

        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
        lp.gravity = Gravity.BOTTOM | Gravity.LEFT;
        waveView.setOnWaveAnimListener(new MineWaveView.OnWaveAnimListener() {
            @Override
            public void onWaveAnimLoading(float x, float aboveY, float belowY) {
                Log.e("MainActivity", "x:" + x + "aboveY:" + aboveY + "belowY:" + belowY);
                lp.setMargins(0, 0, 0, (int) aboveY + 3);
                chuan.setLayoutParams(lp);
                chuan2.setLayoutParams(lp);
                chuan.setX(-chuan.getWidth() + getX(x, chuan, 0));
                chuan2.setX(-chuan2.getWidth() + getX(x, chuan2, 500)-500);

            }
        });
    }

    public float getX(float x, View view, int distance) {
        float x2 = x;
        int waveW = waveView.getWidth();
        if (x > waveW + view.getWidth() + distance) {
            int b = (int) Math.floor((x - view.getWidth()) / waveW);
            x2 = x - view.getWidth() - b * waveW;
        }
        return x2;
    }
}
