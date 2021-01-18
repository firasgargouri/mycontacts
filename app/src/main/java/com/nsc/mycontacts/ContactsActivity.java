package com.nsc.mycontacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsActivity extends AppCompatActivity {

    String ms ="";
    SQLiteDatabase db;
    Cursor cur;
    LinearLayout _layNav, _laySearch;

    Button _btnSearch,_btnAdd,_btnUpdate,_btnDelete,_btnFirst,_btnPrev,_btnNext,_btnLast,_btnCancel,_btnSave,_btnCall,_btnCall2;
    EditText _txtSearch,_txtName,_txtAddress,_txtPhone,_txtPhone2,_txtJob;

    private static final int REQUEST_CALL = 1;

    int op = 0;
    String x,phone;
    ArrayList<Contact> lst;
    int curs = 0;

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
        db.execSQL("CREATE TABLE IF NOT EXISTS contacts (id integer primary key autoincrement," +
                " nom VARCHAR, adresse VARCHAR, tel1 VARCHAR, tel2 VARCHAR, entreprise VARCHAR);");

        _layNav.setVisibility(View.INVISIBLE);
        _btnSave.setVisibility(View.INVISIBLE);
        _btnCancel.setVisibility(View.INVISIBLE);

        _btnSearch.setOnClickListener(v -> {
            String search = "%" + _txtSearch.getText().toString() + "%";
            //cur = db.rawQuery("select * from contacts where nom like ? or tel1 like ? or tel2 like ? or entreprise like ?", new String[]{search,search,search,search});

            bg_select_contacts bg = new bg_select_contacts(ContactsActivity.this);
            bg.execute(search);

            //System.out.println("tttttttttttttttttttttttttttt          "+lst.get(0).getNom());



        });

        _btnFirst.setOnClickListener(v -> {
            try {
                //cur.moveToFirst();
                curs = 0;
                _txtName.setText(lst.get(curs).getNom());
                _txtAddress.setText(lst.get(curs).getAdresse());
                _txtPhone.setText(lst.get(curs).getTel1());
                _txtPhone2.setText(lst.get(curs).getTel2());
                _txtJob.setText(lst.get(curs).getEntreprise());
                //buttonsVisibility(cur);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Contact Not Found",Toast.LENGTH_SHORT).show();
            }
        });

        _btnLast.setOnClickListener(v -> {
            try {
                //cur.moveToLast();
                curs = lst.size()-1;
                _txtName.setText(lst.get(curs).getNom());
                _txtAddress.setText(lst.get(curs).getAdresse());
                _txtPhone.setText(lst.get(curs).getTel1());
                _txtPhone2.setText(lst.get(curs).getTel2());
                _txtJob.setText(lst.get(curs).getEntreprise());
                //buttonsVisibility(cur);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Contact Not Found",Toast.LENGTH_SHORT).show();
            }
        });

        _btnNext.setOnClickListener(v -> {
            try {
                //cur.moveToNext();
                if (curs<lst.size()-1){
                    curs++;
                }
                _txtName.setText(lst.get(curs).getNom());
                _txtAddress.setText(lst.get(curs).getAdresse());
                _txtPhone.setText(lst.get(curs).getTel1());
                _txtPhone2.setText(lst.get(curs).getTel2());
                _txtJob.setText(lst.get(curs).getEntreprise());
                //buttonsVisibility(cur);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        _btnPrev.setOnClickListener(v -> {
            try {
                //cur.moveToPrevious();
                if (curs>0){
                    curs--;
                }
                _txtName.setText(lst.get(curs).getNom());
                _txtAddress.setText(lst.get(curs).getAdresse());
                _txtPhone.setText(lst.get(curs).getTel1());
                _txtPhone2.setText(lst.get(curs).getTel2());
                _txtJob.setText(lst.get(curs).getEntreprise());
                //buttonsVisibility(cur);

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
                x = String. valueOf(lst.get(curs).getId());
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
                //db.execSQL("insert into contacts (nom,adresse,tel1,tel2,entreprise) values (?,?,?,?,?);", new String[] {_txtName.getText().toString(), _txtAddress.getText().toString(),_txtPhone.getText().toString(),_txtPhone2.getText().toString(),_txtJob.getText().toString()});
                bg_insertion_contact bg = new bg_insertion_contact(ContactsActivity.this);
                bg.execute(_txtName.getText().toString(), _txtAddress.getText().toString(),_txtPhone.getText().toString(),_txtPhone2.getText().toString(),_txtJob.getText().toString());

            } else if (op == 2) {
                // Mise à jour
                //db.execSQL("update contacts set nom=?, adresse=?, tel1=?, tel2=?, entreprise=? where id=?;", new String[] {_txtName.getText().toString(), _txtAddress.getText().toString(),_txtPhone.getText().toString(),_txtPhone2.getText().toString(),_txtJob.getText().toString(),x});

                bg_update_contact bg = new bg_update_contact(ContactsActivity.this);
                bg.execute(x,_txtName.getText().toString(), _txtAddress.getText().toString(),_txtPhone.getText().toString(),_txtPhone2.getText().toString(),_txtJob.getText().toString());

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
                x = String. valueOf(lst.get(curs).getId());
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
                        //db.execSQL("delete from contacts where id=?;",new String[] {cur.getString(0)});

                        bg_delete_contact bg = new bg_delete_contact(ContactsActivity.this);
                        bg.execute(x);


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

    private class bg_insertion_contact extends AsyncTask<String, Void, String> {

        AlertDialog dialog;
        Context context;

        public bg_insertion_contact(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new AlertDialog.Builder(context).create();
            dialog.setTitle("Etat de connexion");
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            String strnom = strings[0];
            String stradresse = strings[1];
            String strtel1 = strings[2];
            String strtel2 = strings[3];
            String strentreprise = strings[4];

            String connstr = "http://192.168.1.3/contactdb/addContact.php";
            try {
                URL url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));

                String data = URLEncoder.encode("nom", "UTF-8") + "=" + URLEncoder.encode(strnom, "UTF-8");
                data += "&" + URLEncoder.encode("adresse", "UTF-8") + "=" + URLEncoder.encode(stradresse, "UTF-8");
                data += "&" + URLEncoder.encode("tel1", "UTF-8") + "=" + URLEncoder.encode(strtel1, "UTF-8");
                data += "&" + URLEncoder.encode("tel2", "UTF-8") + "=" + URLEncoder.encode(strtel2, "UTF-8");
                data += "&" + URLEncoder.encode("entreprise", "UTF-8") + "=" + URLEncoder.encode(strentreprise, "UTF-8");
                writer.write(data);
                Log.v("ContactsActivity", data);
                writer.flush();


                writer.close();
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
                String ligne = "";
                while ((ligne = reader.readLine()) != null) {
                    result += ligne;
                }
                reader.close();
                ips.close();
                http.disconnect();

                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.setMessage(s);
            dialog.show();
            if (s.contains("insert success")) {
                Toast.makeText(context, "Contact inséré avec succès.", Toast.LENGTH_LONG).show();
            } else if (s.contains("insert failed")){
                Toast.makeText(context, "Problème d'insertion.", Toast.LENGTH_LONG).show();
            } else if (s.contains("connexion failed")){
                Toast.makeText(context, "Problème de connecxion.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Problème.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class bg_update_contact extends AsyncTask<String, Void, String> {

        AlertDialog dialog;
        Context context;

        public bg_update_contact(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new AlertDialog.Builder(context).create();
            dialog.setTitle("Etat de connexion");
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            String strid = strings[0];
            String strnom = strings[1];
            String stradresse = strings[2];
            String strtel1 = strings[3];
            String strtel2 = strings[4];
            String strentreprise = strings[5];

            String connstr = "http://192.168.1.3/contactdb/updateContact.php";
            try {
                URL url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));

                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(strid, "UTF-8");
                data += "&" + URLEncoder.encode("nom", "UTF-8") + "=" + URLEncoder.encode(strnom, "UTF-8");
                data += "&" + URLEncoder.encode("adresse", "UTF-8") + "=" + URLEncoder.encode(stradresse, "UTF-8");
                data += "&" + URLEncoder.encode("tel1", "UTF-8") + "=" + URLEncoder.encode(strtel1, "UTF-8");
                data += "&" + URLEncoder.encode("tel2", "UTF-8") + "=" + URLEncoder.encode(strtel2, "UTF-8");
                data += "&" + URLEncoder.encode("entreprise", "UTF-8") + "=" + URLEncoder.encode(strentreprise, "UTF-8");
                writer.write(data);
                Log.v("ContactsActivity", data);
                writer.flush();


                writer.close();
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
                String ligne = "";
                while ((ligne = reader.readLine()) != null) {
                    result += ligne;
                }
                reader.close();
                ips.close();
                http.disconnect();

                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.setMessage(s);
            dialog.show();
            if (s.contains("update success")) {
                Toast.makeText(context, "Contact inséré avec succès.", Toast.LENGTH_LONG).show();
            } else if (s.contains("update failed")){
                Toast.makeText(context, "Problème d'insertion.", Toast.LENGTH_LONG).show();
            } else if (s.contains("connexion failed")){
                Toast.makeText(context, "Problème de connecxion.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Problème.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class bg_delete_contact extends AsyncTask<String, Void, String> {

        AlertDialog dialog;
        Context context;

        public bg_delete_contact(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new AlertDialog.Builder(context).create();
            dialog.setTitle("Etat de connexion");
        }

        @Override
        protected String doInBackground(String... strings) {

            String result = "";

            String strid = strings[0];

            String connstr = "http://192.168.1.3/contactdb/deleteContact.php";
            try {
                URL url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));

                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(strid, "UTF-8");
                writer.write(data);
                Log.v("ContactsActivity", data);
                writer.flush();


                writer.close();
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
                String ligne = "";
                while ((ligne = reader.readLine()) != null) {
                    result += ligne;
                }
                reader.close();
                ips.close();
                http.disconnect();

                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.setMessage(s);
            dialog.show();
            if (s.contains("delete success")) {
                Toast.makeText(context, "Contact supprimé.", Toast.LENGTH_LONG).show();
            } else if (s.contains("delete failed")){
                Toast.makeText(context, "Problème de suppression.", Toast.LENGTH_LONG).show();
            } else if (s.contains("connexion failed")){
                Toast.makeText(context, "Problème de connecxion.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Problème.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class bg_select_contacts extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog dialog;

        public bg_select_contacts(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new AlertDialog.Builder(ContactsActivity.this).create();
            dialog.setTitle("Affichage Liste Contacts...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String strsearch = strings[0];
            String connstr = "http://192.168.1.3/contactdb/getContacts.php";
            URL url = null;
            try {
                url = new URL(connstr);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));

                String data = URLEncoder.encode("search", "UTF-8") + "=" + URLEncoder.encode(strsearch, "UTF-8");
                writer.write(data);
                Log.v("ContactsActivity", data);
                writer.flush();


                writer.close();
                //http.setDoOutput(true);

                InputStream ips =  http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips,"ISO-8859-1"));
                String ligne ="";
                while ((ligne = reader.readLine()) != null)
                {
                    result += ligne;
                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;
            } catch (IOException e) {
                result = e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!s.equals("")){

                ArrayList<Contact> l = parse(s);
                setLST(parse(s));

                try {
                    _txtName.setText(l.get(0).getNom());
                    _txtAddress.setText(l.get(0).getAdresse());
                    _txtPhone.setText(l.get(0).getTel1());
                    _txtPhone2.setText(l.get(0).getTel2());
                    _txtJob.setText(l.get(0).getEntreprise());
                    if (l.size() == 1){
                        _layNav.setVisibility(View.INVISIBLE);
                    } else {
                        _layNav.setVisibility(View.VISIBLE);
                        //buttonsVisibility(cur);
                    }
                    System.out.println("nooooooooooooooooooooooooooooooooooooooo");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"No Result.",Toast.LENGTH_SHORT).show();
                    _txtName.setText("");
                    _txtAddress.setText("");
                    _txtPhone.setText("");
                    _txtPhone2.setText("");
                    _txtJob.setText("");
                    _layNav.setVisibility(View.INVISIBLE);
                    System.out.println("llllllllllllllllllllllllllllllllllllllll");
                }

                //remplir le listView
                /***
                 * Map : projection
                 * String (le premier élément : clé)
                 * String (le deuxième élément : valeur)
                 */





            }


        }

        private ArrayList<Contact> parse(final String json) {
            ms = "";
            final ArrayList<Contact> contacts = new ArrayList<>();
            try {
                final JSONArray jContactArray = new JSONArray(json);
                for (int i =0; i< jContactArray.length(); i++){
                    contacts.add(new Contact(jContactArray.optJSONObject(i)));
                    Contact c = new Contact(jContactArray.optJSONObject(i));
                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+c.getNom());
                    ms = ms + "id: " + jContactArray.optJSONObject(i).optInt("id") + " | " +
                            "nom: " + jContactArray.optJSONObject(i).optString("nom") + " | " +
                            "adresse: " + jContactArray.optJSONObject(i).optString("adresse") + " | " +
                            "tel1: " + jContactArray.optJSONObject(i).optString("tel1") + " | " +
                            "tel2: " + jContactArray.optJSONObject(i).optString("tel2") + " | " +
                            "entreprise: " + jContactArray.optJSONObject(i).optString("entreprise") + "\n";
                }
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxx          "+contacts.get(0).getNom());
                return contacts;


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    private void setLST(ArrayList<Contact> l){
        this.lst = l;
    }
}

