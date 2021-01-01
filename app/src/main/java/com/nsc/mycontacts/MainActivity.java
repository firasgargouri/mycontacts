package com.nsc.mycontacts;

import androidx.appcompat.app.AppCompatActivity;
import static com.nsc.mycontacts.SHA256.sha256;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    Button _btnConnect;
    EditText _txtLogin,_txtPassword;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _txtLogin = (EditText) findViewById(R.id.txtLogin);
        _txtPassword = (EditText) findViewById(R.id.txtPassword);
        _btnConnect = (Button) findViewById(R.id.btnConnect);

        //Database init
        db = openOrCreateDatabase("ComptesWeb",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS USERS (login varchar primary key, password varchar);");

        SQLiteStatement s = db.compileStatement("select count(*) from users;");
        long c = s.simpleQueryForLong();
        if (c==0){
            db.execSQL("insert into users (login, password) values (?,?)", new String[] {"root", sha256("12345")});
        }
        _btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strLogin = _txtLogin.getText().toString();
                String strPwd = _txtPassword.getText().toString();
                // Créer un curseur pour récupérer le résultat de la requête select
                Cursor cur = db.rawQuery("select password from users where login =?", new String[] {strLogin});
                try {
                    cur.moveToFirst();
                    String p = cur.getString(0);
                    if (p.equals(sha256(strPwd))){
                        Toast.makeText(getApplicationContext(),"Bienvenue " + strLogin, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),ContactsActivity.class);
                        startActivity(i);
                    }else{
                        _txtLogin.setText("");
                        _txtPassword.setText("");
                        Toast.makeText(getApplicationContext(),"Echec de connexion",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    _txtLogin.setText("");
                    _txtPassword.setText("");
                    Toast.makeText(getApplicationContext(),"Utilisateur Inexistant",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}