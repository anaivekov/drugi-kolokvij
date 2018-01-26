package hr.math.drugikolokvij;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor Stranice = db.getAllSites();
        Cursor Vlasnici = db.getAllOwners();

        text += "Stranice:\n";

        if (Stranice.moveToFirst())
        {
            do {
                text += Stranice.getString(0) + "\t" + Stranice.getString(1) + "\t" + Stranice.getString(2) + "\n";
            } while (Stranice.moveToNext());
        }

        text += "\nVlasnici:\n";
        if (Vlasnici.moveToFirst()) {
            do {
                text += Vlasnici.getString(0) + "\t" + Vlasnici.getString(1) + "\n";
            } while (Vlasnici.moveToNext());
        }

        TextView mText = (TextView) findViewById(R.id.sadrzaj_tablice);
        mText.setText(text);

            db.close();

    }
}
