package su.hm.netsugar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import su.hm.netsugar_master.NetworkSugar;
import su.hm.netsugar_master.annotations.NetSugar;
import su.hm.netsugar_master.annotations.Offline;
import su.hm.netsugar_master.entity.MatchResult;
import su.hm.netsugar_master.entity.NetworkType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkSugar.inject(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo();
            }
        });
    }

    @NetSugar(type = NetworkType.WIFI, pair = 0x2)
    public void playVideo() {
        Toast.makeText(MainActivity.this, "playVideo", Toast.LENGTH_SHORT).show();
    }

    @Offline(pair = 0x2)
    public void offline(MatchResult result) {
        Toast.makeText(MainActivity.this, "Sorry, Network is OK but match type is" + result.getMatchType(),
                Toast.LENGTH_SHORT).show();
    }
}
