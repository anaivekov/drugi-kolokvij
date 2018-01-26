package hr.math.drugikolokvij;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Main5Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor Stranice = db.getAllSites();

        int counter = 0;

        if (Stranice.moveToFirst()) {
            do {
                counter += 1;
                Stranice.moveToNext();
            } while (counter < 2);
        }

        EditText mEdit = (EditText) findViewById(R.id.prikazanaStranica);
       mEdit.setText(Stranice.getString(1));
    }
}
