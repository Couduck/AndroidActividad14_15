package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText CodigoET, NombreET, DescripcionET, GeneroET;
    Button IngresarBTN, ConsultarBTN, ActualizarBTN, BorrarBTN, LimpiarBTN;
    List<Anime> listAnimes = new ArrayList<Anime>();

    FirebaseDatabase DB = FirebaseDatabase.getInstance();
    DatabaseReference DBref = DB.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CodigoET = findViewById(R.id.codigoET);
        NombreET = findViewById(R.id.nombreET);
        DescripcionET = findViewById(R.id.descripcionET);
        GeneroET = findViewById(R.id.generoET);

        IngresarBTN = findViewById(R.id.ingresarBTN);
        ConsultarBTN = findViewById(R.id.consultarBTN);
        ActualizarBTN = findViewById(R.id.actualizarBTN);
        BorrarBTN = findViewById(R.id.borrarBTN);
        LimpiarBTN = findViewById(R.id.limpiarBTN);

        obtenerlista();

        IngresarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String codigo = CodigoET.getText().toString();
                String nombre = NombreET.getText().toString();
                String descripcion = DescripcionET.getText().toString();
                String genero = GeneroET.getText().toString();

                if(codigo.isEmpty() || nombre.isEmpty() || descripcion.isEmpty() || genero.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Faltan campos por llenar", Toast.LENGTH_LONG).show();
                    return;
                }

                Anime nuevo = new Anime(codigo, nombre, descripcion, genero);

                DBref.child("Animes").child(nuevo.getCodigo()).setValue(nuevo);

                Toast.makeText(MainActivity.this, "Nuevo registro guardado exitosamente!", Toast.LENGTH_LONG).show();

                limpiarPantalla();
            }
        });

        ConsultarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!cargarDatosACampos())
                {
                    Toast.makeText(MainActivity.this, "No se encontró ningun registro con ese codigo", Toast.LENGTH_LONG).show();
                }
            }
        });

        ActualizarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        BorrarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        LimpiarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                limpiarPantalla();
            }
        });


    }

    public void obtenerlista()
    {
        DBref.child("Animes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                listAnimes.clear();

                for(DataSnapshot obj : snapshot.getChildren())
                {
                    Anime anime = obj.getValue(Anime.class);
                    listAnimes.add(anime);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    public boolean cargarDatosACampos()
    {
        boolean existe = false;

        for(Anime anime : listAnimes)
        {
            if(CodigoET.getText().toString().equals(anime.getCodigo()))
            {
                existe = true;
                NombreET.setText(anime.getNombre());
                DescripcionET.setText(anime.getDescripcion());
                GeneroET.setText(anime.getGenero());
                break;
            }
        }

        return existe;
    }

    public void limpiarPantalla()
    {
        CodigoET.setText("");
        NombreET.setText("");
        DescripcionET.setText("");
        GeneroET.setText("");
    }


}