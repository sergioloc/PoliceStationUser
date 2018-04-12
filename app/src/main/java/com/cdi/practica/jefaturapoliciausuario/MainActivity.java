package com.cdi.practica.jefaturapoliciausuario;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView registro;
    private Button botonLogin;
    private EditText email, pass;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        buttons();
    }

    /**Inicializacion de variables**/
    private void init() {
        // EditText
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.password);
        // TextView
        registro = (TextView) findViewById(R.id.registro);
        // Buttons
        botonLogin = (Button) findViewById(R.id.botonLogin);
        // Firebase
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    //iniciado sesion
                    startActivity(new Intent(MainActivity.this,MainMenu.class));
                }else{
                    //sesion cerrada
                }
            }
        };
    }

    /**Acciones de botones**/
    private void buttons() {
        // Boton aceptar
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailS = email.getText().toString();
                String passS = pass.getText().toString();
                if(emailS.equals("")||passS.equals(""))
                    Toast.makeText(getApplicationContext(),"Debes rellenar ambos campos",Toast.LENGTH_SHORT).show();
                else
                    signIn(emailS,passS);
            }
        });
        // Boton registrarse
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Registro.class);
                i.putExtra("data",false);
                startActivity(i);
            }
        });
    }

    /**Login**/
    private void signIn(String email, String pass) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Sesion iniciada",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"No se ha podido iniciar sesion",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }
}
