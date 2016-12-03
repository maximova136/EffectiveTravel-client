package com.et.gui;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.et.R;


public class TestActivity extends BaseActivity {

    private TextView text;
    private TestAsynkTask task;

    public TestActivity() {
        super(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        text = (TextView) findViewById(R.id.testText);

        task = new TestAsynkTask();
        task.execute((Void) null);
    }


    private class TestAsynkTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
                return false;
        }

        @Override
        protected void onPostExecute(final Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if(aBoolean) {
//                for (RouteObject r : routes.getRoutes() ) {
////                    text.setText(text.getText() + "/n" + r.toString());
//                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Failed to fetch routes from server", Toast.LENGTH_SHORT).show();
            }
            task = null;
        }
    }

}
