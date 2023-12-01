package com.example.appcomedor;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.*;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


import android.os.AsyncTask;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {
    private Button botonIni;
    private Button botonReg;
    private TextView text1;
    private TextView text2;
    private EditText inCorreo;
    private EditText inPassword;

    private int trys=0;

    String correo = "victormasobolta@gmail.com";
    String contrasena = "Abcd12345";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = findViewById(R.id.textView);
        text2 = findViewById(R.id.textView2);
        text1.setText("E-mail");
        text2.setText("Password");

        inCorreo = findViewById(R.id.editTextTextEmailAddress);
        inPassword = findViewById(R.id.editTextTextPassword);

        botonIni = findViewById(R.id.button);
        botonIni.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                answerPulse1();
            }
        });
        botonReg = findViewById(R.id.button2);
        botonReg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){answerPulse2();}
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void answerPulse1(){
        String mailIntro = inCorreo.getText().toString();
        String contraIntro = inPassword.getText().toString();
        if(validateContra(contraIntro, mailIntro)){
            new ValidarUsuarioTask().execute(mailIntro, contraIntro);
        }

        else{


        }
       /* if(mailIntro.equals(correo)&&(contraIntro.equals(contrasena))){
            Toast.makeText(MainActivity.this,"Sesion Iniciada", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this,"Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
        }*/
    }
    public void answerPulse2(){
        Intent intent = new Intent(this, MainActivityRegister.class);
        startActivity(intent);
        finish();
    }
    public boolean validateContra(String contrasena, String mail){
        if((inCorreo.getText().toString().isEmpty() )||(inPassword.getText().toString().isEmpty())){/*||((contraIntro.isEmpty())&&(contraIntro.isEmpty()))*/
            Toast.makeText(MainActivity.this,"None of the fields must be empty." , Toast.LENGTH_LONG).show();
            return false;
        }
        if((contrasena.length()<= 4)&&(contrasena.length()>=8)){
            Toast.makeText(MainActivity.this,"Password lenght should be between 4 and 8 characters." , Toast.LENGTH_LONG).show();
            for (int i=0; i==contrasena.length();i++){
                char c = contrasena.charAt(i);
                if (!(Character.isDigit(c))&&(!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z'))){
                    Toast.makeText(MainActivity.this,"Only alphanumeric characters allowed" , Toast.LENGTH_LONG).show();
                    return false;
                }
                return false;
            }



        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    public void abrirNuevaActividad(String validUser){

        if(validUser.equals("ok")){
            Intent intent = new Intent(this, NuevaActividad.class);
            startActivity(intent);
            finish();

        }
        else if (validUser.equals("ko")&&(trys<3)) {
            trys++;
            Toast.makeText(this, "Non valid user or password", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Too many trys.", Toast.LENGTH_LONG).show();
            inCorreo.setEnabled(false);
            inPassword.setEnabled(false);
            inCorreo.setFocusable(false);
            inPassword.setFocusable(false);
            timer(30000);
            }
            //Intent intent = new Intent(this, NuevaActividad2.class);
            /*startActivity(intent);
            AdmBaseDatosSQLite adm = new AdmBaseDatosSQLite(MainActivity.this, "Intentos", null, 1);
            SQLiteDatabase baseDatos = adm.getReadableDatabase();
            ContentValues data = new ContentValues();
            data.put("username", inCorreo.getText().toString());
            data.put("password", inPassword.getText().toString());
            data.put("date", fechaHoy());
            finish();*/
        }


    @RequiresApi(api = Build.VERSION_CODES.P)
    private void timer(int ms) {
        Handler handler =new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                inCorreo.setEnabled(true);
                inPassword.setEnabled(true);
                inCorreo.setFocusable(true);
                inPassword.setFocusable(true);


            }
        },ms);
/*        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd/MM/yyyy hh:mm  a", Locale.getDefault());
        Date fechaAct = new Date();
        return dateFormat.format(fechaAct);*/
    }

    private static Document convertirStringToXMLDocument(String xmlString)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    private class ValidarUsuarioTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String usuario = params[0];
            String contrasena = params[1];
            //String url = "http://127.0.0.1/validacuenta.php"; // Reemplaza esto con la URL de tu archivo PHP
            String url = "http://10.0.2.2/validacuenta.php"; // Reemplaza esto con la URL de tu archivo PHP
            //String url = "http://192.168.1.15/validacuenta.php"; // Reemplaza esto con la URL de tu archivo PHP

            String resultado = null;
            try {
                // Crear la conexión HTTP
                URL direccion = new URL(url);
                HttpURLConnection conexion = (HttpURLConnection) direccion.openConnection();
                conexion.setRequestMethod("POST");
                conexion.setDoOutput(true);

                // Crear los datos del formulario
                String datos = "usuario=" + usuario + "&contrasena=" + contrasena;

                // Escribir los datos del formulario en la solicitud HTTP
                OutputStream salida = conexion.getOutputStream();
                byte[] bytes = datos.getBytes(Charset.forName("UTF-8"));
                salida.write(bytes);
                salida.flush();
                salida.close();

                // Leer la respuesta del servidor
                InputStream entrada = conexion.getInputStream();
                BufferedReader lector = new BufferedReader(new InputStreamReader(entrada));
                StringBuilder respuesta = new StringBuilder();
                String linea;

                while ((linea = lector.readLine()) != null) {
                    respuesta.append(linea);
                }

                // Cerrar la conexión HTTP
                entrada.close();
                conexion.disconnect();

                // Procesar la respuesta del servidor
                resultado = respuesta.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return resultado;
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        protected void onPostExecute(String resultado)  {
            super.onPostExecute(resultado);
            try{
                Document doc = convertirStringToXMLDocument(resultado);
                NodeList listaItem = (NodeList) doc.getElementsByTagName("respuesta");
                Element element = (Element) listaItem.item(0);
                String var_id = element.getElementsByTagName("estado").item(0).getTextContent();
                // Abrir la nueva actividad en función del resultado de la validación
                abrirNuevaActividad(var_id);
            }
            catch (Exception e) {
            e.printStackTrace();

                System.out.println(e + " " + resultado);
            }
        }
    }
}




