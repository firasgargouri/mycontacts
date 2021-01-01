package com.nsc.mycontacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ContactsActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Cursor cur;
    LinearLayout _layNav, _laySearch;

    Button _btnSearch,_btnAdd,_btnUpdate,_btnDelete,_btnFirst,_btnPrev,_btnNext,_btnLast,_btnCancel,_btnSave,_btnCall,_btnCall2;
    EditText _txtSearch,_txtName,_txtAddress,_txtPhone,_txtPhone2,_txtJob;

    private static final int REQUEST_CALL = 1;

    int op = 0;
    String x,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        _txtSearch = (EditText) findViewById(R.id.txtSearch);
        _txtName = (EditText) findViewById(R.id.txtName);
        _txtAddress = (EditText) findViewById(R.id.txtAddress);
        _txtPhone = (EditText) findViewById(R.id.txtPhone);
        _txtPhone2 = (EditText) findViewById(R.id.txtPhone2);
        _txtJob = (EditText) findViewById(R.id.txtJob);
        _btnSearch = (Button) findViewById(R.id.btnSearch);
        _btnAdd = (Button) findViewById(R.id.btnAdd);
        _btnSave = (Button) findViewById(R.id.btnSave);
        _btnUpdate = (Button) findViewById(R.id.btnUpdate);
        _btnDelete = (Button) findViewById(R.id.btnDelete);
        _btnFirst = (Button) findViewById(R.id.btnFirst);
        _btnPrev = (Button) findViewById(R.id.btnPrev);
        _btnNext = (Button) findViewById(R.id.btnNext);
        _btnLast = (Button) findViewById(R.id.btnLast);
        _btnCancel = (Button) findViewById(R.id.btnCancel);
        _btnCall = (Button) findViewById(R.id.btnCall);
        _btnCall2 = (Button) findViewById(R.id.btnCall2);
        _layNav = (LinearLayout) findViewById(R.id.layNav);
        _laySearch = (LinearLayout) findViewById(R.id.laySearch);


        db = openOrCreateDatabase("bdContacts",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS contacts (id integer primary key autoincrement, nom VARCHAR, adresse VARCHAR, tel1 VARCHAR, tel2 VARCHAR, entreprise VARCHAR);");

        _layNav.setVisibility(View.INVISIBLE);
        _btnSave.setVisibility(View.INVISIBLE);
        _btnCancel.setVisibility(View.INVISIBLE);

        _btnSearch.setOnClickListener(v -> {
            String search = "%" + _txtSearch.getText().toString() + "%";
            cur = db.rawQuery("select * from contacts where nom like ? or tel1 like ? or tel2 like ? or entreprise like ?", new String[]{search,search,search,search});

            try {
                cur.moveToFirst();
                _txtName.setText(cur.getString(1));
                _txtAddress.setText(cur.getString(2));
                _txtPhone.setText(cur.getString(3));
                _txtPhone2.setText(cur.getString(4));
                _txtJob.setText(cur.getString(5));
                if (cur.getCount() == 1){
                    _layNav.setVisibility(View.INVISIBLE);
                } else {
                    _layNav.setVisibility(View.VISIBLE);
                    buttonsVisibility(cur);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"No Result.",Toast.LENGTH_SHORT).show();
                _txtName.setText("");
                _txtAddress.setText("");
                _txtPhone.setText("");
                _txtPhone2.setText("");
                _txtJob.setText("");
                _layNav.setVisibility(View.INVISIBLE);
            }
        });

        _btnFirst.setOnClickListener(v -> {
            try {
                cur.moveToFirst();
                _txtName.setText(cur.getString(1));
                _txtAddress.setText(cur.getString(2));
                _txtPhone.setText(cur.getString(3));
                _txtPhone2.setText(cur.getString(4));
                _txtJob.setText(cur.getString(5));
                buttonsVisibility(cur);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Contact Not Found",Toast.LENGTH_SHORT).show();
            }
        });

        _btnLast.setOnClickListener(v -> {
            try {
                cur.moveToLast();
                _txtName.setText(cur.getString(1));
                _txtAddress.setText(cur.getString(2));
                _txtPhone.setText(cur.getString(3));
                _txtPhone2.setText(cur.getString(4));
                _txtJob.setText(cur.getString(5));
                buttonsVisibility(cur);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Contact Not Found",Toast.LENGTH_SHORT).show();
            }
        });

        _btnNext.setOnClickListener(v -> {
            try {
                cur.moveToNext();
                _txtName.setText(cur.getString(1));
                _txtAddress.setText(cur.getString(2));
                _txtPhone.setText(cur.getString(3));
                _txtPhone2.setText(cur.getString(4));
                _txtJob.setText(cur.getString(5));
                buttonsVisibility(cur);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        _btnPrev.setOnClickListener(v -> {
            try {
                cur.moveToPrevious();
                _txtName.setText(cur.getString(1));
                _txtAddress.setText(cur.getString(2));
                _txtPhone.setText(cur.getString(3));
                _txtPhone2.setText(cur.getString(4));
                _txtJob.setText(cur.getString(5));
                buttonsVisibility(cur);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        _btnAdd.setOnClickListener(v -> {
            op = 1;
            _txtName.setText("");
            _txtAddress.setText("");
            _txtPhone.setText("");
            _txtPhone2.setText("");
            _txtJob.setText("");
            _btnSave.setVisibility(View.VISIBLE);
            _btnCancel.setVisibility(View.VISIBLE);
            _btnUpdate.setVisibility(View.INVISIBLE);
            _btnDelete.setVisibility(View.INVISIBLE);
            _btnAdd.setEnabled(false);
            _layNav.setVisibility(View.INVISIBLE);
            _laySearch.setVisibility(View.INVISIBLE);
        });

        _btnUpdate.setOnClickListener(v -> {
            try {
                x = cur.getString(0);
                op = 2;

                _btnSave.setVisibility(View.VISIBLE);
                _btnCancel.setVisibility(View.VISIBLE);

                _btnDelete.setVisibility(View.INVISIBLE);
                _btnUpdate.setEnabled(false);
                _btnAdd.setVisibility(View.INVISIBLE);

                _layNav.setVisibility(View.INVISIBLE);
                _laySearch.setVisibility(View.INVISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Select a contact to update",Toast.LENGTH_SHORT).show();
            }
        });

        _btnSave.setOnClickListener(v -> {
            if (op == 1){
                // insertion
                db.execSQL("insert into contacts (nom,adresse,tel1,tel2,entreprise) values (?,?,?,?,?);", new String[] {_txtName.getText().toString(), _txtAddress.getText().toString(),_txtPhone.getText().toString(),_txtPhone2.getText().toString(),_txtJob.getText().toString()});
            } else if (op == 2) {
                // Mise à jour
                db.execSQL("update contacts set nom=?, adresse=?, tel1=?, tel2=?, entreprise=? where id=?;", new String[] {_txtName.getText().toString(), _txtAddress.getText().toString(),_txtPhone.getText().toString(),_txtPhone2.getText().toString(),_txtJob.getText().toString(),x});
            }

            _btnSave.setVisibility(View.INVISIBLE);
            _btnCancel.setVisibility(View.INVISIBLE);
            _btnUpdate.setVisibility(View.VISIBLE);
            _btnDelete.setVisibility(View.VISIBLE);

            _btnAdd.setVisibility(View.VISIBLE);
            _btnAdd.setEnabled(true);
            _btnUpdate.setEnabled(true);
            _btnSearch.performClick();
            _laySearch.setVisibility(View.VISIBLE);
        });

        _btnCancel.setOnClickListener(v -> {
            op = 0;

            _btnSave.setVisibility(View.INVISIBLE);
            _btnCancel.setVisibility(View.INVISIBLE);
            _btnUpdate.setVisibility(View.VISIBLE);
            _btnDelete.setVisibility(View.VISIBLE);

            _btnAdd.setVisibility(View.VISIBLE);
            _btnAdd.setEnabled(true);
            _btnUpdate.setEnabled(true);

            _laySearch.setVisibility(View.VISIBLE);
        });

        _btnDelete.setOnClickListener(v -> {
            try {
                x=  cur.getString(0);
                AlertDialog dial = Options();
                dial.show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Select contact to delete",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        _btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = _txtPhone.getText().toString();
                makePhoneCall(phone);
            }
        });

        _btnCall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = _txtPhone2.getText().toString();
                makePhoneCall(phone);
            }
        });

    }

    private AlertDialog Options(){
        AlertDialog MiDia = new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Do you really want to delete this contact?")
                .setIcon(R.drawable.validate)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.execSQL("delete from comptes where id=?;",new String[] {cur.getString(0)});
                        _txtName.setText("");
                        _txtAddress.setText("");
                        _txtPhone.setText("");
                        _txtPhone2.setText("");
                        _txtJob.setText("");
                        _layNav.setVisibility(View.INVISIBLE);
                        cur.close();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        return MiDia;
    }

    private void makePhoneCall(String number) {
        if (number.trim().length() > 0) {
            if (ContextCompat.checkSelfPermission(ContactsActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ContactsActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(ContactsActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall(phone);
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void buttonsVisibility(Cursor c){
        if (c.isFirst()){
            _btnFirst.setEnabled(false);
            _btnPrev.setEnabled(false);
            _btnNext.setEnabled(true);
            _btnLast.setEnabled(true);
        }else if (c.isLast()){
            _btnFirst.setEnabled(true);
            _btnPrev.setEnabled(true);
            _btnNext.setEnabled(false);
            _btnLast.setEnabled(false);
        }else{
            _btnFirst.setEnabled(true);
            _btnPrev.setEnabled(true);
            _btnNext.setEnabled(true);
            _btnLast.setEnabled(true);
        }
    }
}