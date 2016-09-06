package favorite.favoritefood.favoritefood;

/**
 * Created by macintosh on 2016. 9. 4..
 */
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();       // 3 초후 이미지를 닫아버림
            }
        }, 3000);

    }
}