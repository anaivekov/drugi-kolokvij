package hr.math.drugikolokvij;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
    }

    public void finalDelete(View view) {
        EditText mEdit = (EditText) findViewById(R.id.idBrisaneStr);
        String someString = mEdit.getText().toString();
        int id = Integer.parseInt(someString);

        DBAdapter db = new DBAdapter(this);
        db.open();
        db.deleteSite(id);
        db.close();

        try {
            Intent j = new Intent(this, MainActivity.class);
            startActivity(j);
        } catch (Exception e) {
        }
    }
}
