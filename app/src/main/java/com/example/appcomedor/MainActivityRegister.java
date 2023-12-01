package com.example.appcomedor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivityRegister extends AppCompatActivity {
    private EditText inCorreo;
    private EditText inPassword;
    private EditText inConfPass;
    private Button reg;
    private Spinner centersSchool;
    private Spinner docType;
    private EditText dateBorn;

    private EditText document;
    private EditText nombre;
    private EditText apellido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register);
        docType = findViewById(R.id.spinner);
        centersSchool = findViewById(R.id.spinner2);
        dateBorn = findViewById(R.id.editTextDate);
        inCorreo = findViewById(R.id.editTextTextEmailAddress2);
        inPassword = findViewById(R.id.editTextTextPassword2);
        inConfPass = findViewById(R.id.editTextTextPassword3);
        reg = findViewById(R.id.buttonReg);
        nombre = findViewById(R.id.editTextText);
        apellido = findViewById(R.id.editTextText2);
        document = findViewById(R.id.editTextText3);
        String[] typesDoc = {"", "DNI", "NIE", "Passport"};
        String[] centersPos = {"", "Monlau"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, typesDoc);
        docType.setAdapter(adapter1);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, centersPos);
        centersSchool.setAdapter(adapter2);

        reg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tryReg();
            }
        });


    }

    public void tryReg() {
        String usuario = inCorreo.getText().toString();
        String contrasena = inPassword.getText().toString();
        String contrasenaConf = inConfPass.getText().toString();
        String borndate = dateBorn.getText().toString();
        String center = centersSchool.getSelectedItem().toString();
        String name = nombre.getText().toString();
        String surname = apellido.getText().toString();
        String doc = document.getText().toString();
        String docTipo = docType.getSelectedItem().toString();
        if(validations(contrasena, contrasenaConf, doc,docTipo)) {

        }
        else{

        }
    }


    public boolean validations(String contra, String comContra, String doc,String docType) {
        if (!noEmptyFields()){
            Toast.makeText(this, "None of the fields should be empty", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!contra.equals(comContra)) {
            Toast.makeText(this, "Both passords should be the same", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!(contra.length() >= 8)) {
            Toast.makeText(this, "Password should contain at least 8 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!checkPassChars(contra)) {
            Toast.makeText(this, "Password should should only contain numbers and letters", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!checkDocument(doc,docType)){
            Toast.makeText(this, "Use the proper format for the doc type", Toast.LENGTH_LONG).show();
            return false;
        }


        return true;
    }

    public boolean checkPassChars(String contra) {


/*        for(int i=0;i==contra.length();i++){
            if (contra[i].matches("[A-Z]*")) {

            }
        }*/
        if (contra.matches("[A-Z]*")||(contra.matches("[0-9]*"))){
            return true;
        }

        else{
            return false;
        }

    }

    public boolean checkDocument(String doc, String doctype) {
        if(doctype.equals("")){

        }
        return false;
    }


    public boolean noEmptyFields() {
        if ((!nombre.getText().toString().isEmpty()) &&
                (!apellido.getText().toString().isEmpty()) &&
                (!document.getText().toString().isEmpty()) &&
                (!inCorreo.getText().toString().isEmpty()) &&
                (!inPassword.getText().toString().isEmpty()) &&
                (!inConfPass.getText().toString().isEmpty()) &&
                (!dateBorn.getText().toString().isEmpty()) &&
                (centersSchool.getSelectedItemPosition() != 0) &&
                (docType.getSelectedItemPosition() != 0)) {

            return false;
        } else {

            return true;
        }
    }

    public class InsertDataAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String usuario = params[0];
            String contrasena = params[1];
            String bornDate = params[2];
            String center = params[3];
            String name = params[4];
            String surname = params[5];
            String document = params[6];
            //String
            String url = "http://10.0.2.2/validacuenta.php"; // Reemplaza esto con la URL de tu archivo PHP

            try {
                URL apiUrl = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) apiUrl.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                String datos = "usuario=" + usuario + "&contrasena=" + contrasena+"&bornDate="+bornDate+"&center="+center+"&name="+name+"&surname="+surname+"&document="+document;
                // Escribir los datos en el cuerpo de la solicitud
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(datos);
                writer.flush();
                writer.close();
                outputStream.close();

                // Obtener la respuesta del servidor
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                inputStream.close();

                // Devolver la respuesta del servidor
                return response.toString();

            } catch (IOException e) {
                e.printStackTrace();
                return "Error al realizar la solicitud: " + e.getMessage();
            }
        }
    }
}


