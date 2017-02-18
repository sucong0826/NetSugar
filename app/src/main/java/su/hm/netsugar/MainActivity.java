package su.hm.netsugar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import su.hm.netsugar_master.NetworkSugar;
import su.hm.netsugar_master.annotations.NetSugar;
import su.hm.netsugar_master.annotations.Offline;
import su.hm.netsugar_master.annotations.Online;
import su.hm.netsugar_master.entity.NetworkType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetworkSugar.inject(this);

        showToast(2);
    }

    @NetSugar(
            check = true,
            online = @Online(type = NetworkType.WIFI),
            offline = @Offline(method = "offline"))
    public void showToast(int x) {
        Toast.makeText(MainActivity.this, "this".concat(String.valueOf(x)), Toast.LENGTH_SHORT).show();
    }

    /**
     * offline method is to handle situation without network.
     */
    public void offline() {
        // here is just a sample.
        Toast.makeText(MainActivity.this, "Sorry, Network is bad", Toast.LENGTH_SHORT).show();
    }
}
