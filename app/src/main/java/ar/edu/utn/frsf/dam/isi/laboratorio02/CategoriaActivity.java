package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;

public class CategoriaActivity extends AppCompatActivity {

    private EditText textoCat;
    private Button btnCrear;
    private Button btnMenu;
    private boolean success=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        textoCat = (EditText) findViewById(R.id.txtNombreCategoria);
        btnCrear = (Button) findViewById(R.id.btnCrearCategoria);

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        CategoriaRest cr = new CategoriaRest();

                        try {
                            cr.crearCategoria(new Categoria(textoCat.getText().toString()));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(getApplicationContext(),"Se ha creado la categoria",Toast.LENGTH_SHORT).show();
                                    textoCat.setText("");

                                }
                            });
                        }catch(Exception e){

                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(getApplicationContext(),"No se ha creado la categoria",Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    }
                };

                Thread t= new Thread(r);
                t.start();
                //runOnUiThread(r);

            }
        });

        btnMenu= (Button) findViewById(R.id.btnCategoriaVolver);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoriaActivity.this,
                        MainActivity.class);
                startActivity(i);
            }
        });
    }
}
