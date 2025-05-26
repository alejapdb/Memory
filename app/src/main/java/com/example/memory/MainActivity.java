package com.example.memory;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.memory.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageButton[] cartas = new ImageButton[12];
    private int[] imagenes = {R.drawable.circulo_amarillo,R.drawable.circulo_amarillo,
            R.drawable.corazon_rosa,R.drawable.corazon_rosa,
            R.drawable.cuadrado_rojo,R.drawable.cuadrado_rojo,
            R.drawable.estrella_azul,R.drawable.estrella_azul,
            R.drawable.luna_amarilla,R.drawable.luna_amarilla,
            R.drawable.triangulo_verde,R.drawable.triangulo_verde
    }; // Pares de imágenes

    private int[] cartasMezcladas = new int[12]; // Para almacenar las imágenes mezcladas
    private List<ImageButton> seleccionadas = new ArrayList<>();
    private int puntuacion = 0;
    private TextView txtPuntuacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPuntuacion = findViewById(R.id.puntuacion2);
        Button btnComprobar = findViewById(R.id.comprobar);
        Button btnReiniciar = findViewById(R.id.again);

        // Asignar las cartas del layout
        for (int i = 0; i < 12; i++) {
            String cartaID = "carta" + (i + 1);
            int resID = getResources().getIdentifier(cartaID, "id", getPackageName());
            cartas[i] = findViewById(resID);
            cartas[i].setOnClickListener(this::seleccionarCarta);
        }

        mezclarCartas();

        btnComprobar.setOnClickListener(v -> comprobarPareja());
        btnReiniciar.setOnClickListener(v -> reiniciarJuego());
    }

    private void mezclarCartas() {
        List<Integer> tempList = new ArrayList<>();
        for (int img : imagenes) tempList.add(img);
        Collections.shuffle(tempList);
        for (int i = 0; i < 12; i++) cartasMezcladas[i] = tempList.get(i);

        for (ImageButton carta : cartas) {
            carta.setImageResource(R.drawable.carta); // Imagen trasera
        }

        seleccionadas.clear();
        puntuacion = 0;
        txtPuntuacion.setText("0");
    }

    private void seleccionarCarta(View view) {
        if (seleccionadas.size() < 2) {
            ImageButton cartaSeleccionada = (ImageButton) view;
            int index = encontrarIndice(cartaSeleccionada);
            cartaSeleccionada.setImageResource(cartasMezcladas[index]);
            seleccionadas.add(cartaSeleccionada);
        }
    }

    private void comprobarPareja() {
        if (seleccionadas.size() == 2) {
            ImageButton carta1 = seleccionadas.get(0);
            ImageButton carta2 = seleccionadas.get(1);

            int index1 = encontrarIndice(carta1);
            int index2 = encontrarIndice(carta2);

            if (cartasMezcladas[index1] == cartasMezcladas[index2]) {
                puntuacion += 20;
                seleccionadas.clear();
            } else {
                puntuacion = Math.max(0, puntuacion - 5);
                new Handler().postDelayed(() -> {
                    carta1.setImageResource(R.drawable.carta);
                    carta2.setImageResource(R.drawable.carta);
                    seleccionadas.clear();
                }, 500);
            }
            txtPuntuacion.setText(String.valueOf(puntuacion));
        }
    }

    private void reiniciarJuego() {
        mezclarCartas();
    }

    private int encontrarIndice(ImageButton carta) {
        for (int i = 0; i < cartas.length; i++) {
            if (cartas[i] == carta) return i;
        }
        return -1;
    }
}
