package Activity.Pointeuse;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import wolfsoft1.pay2wallet.R;

public class SplashActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity1_splash);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity1.this, HomePointeuse.class);
                SplashActivity1.this.startActivity(mainIntent);
                SplashActivity1.this.finish();
            }
        }, 3000);

    }
}
