package su.hm.netsugar;

import android.app.Application;

/**
 * App application class.
 * Do some inits.
 * <p>
 * Created by hm-su on 2017/2/21.
 */

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        say();
    }

    private void say() {

    }
}
