package com.example.quiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    Źle zrozumiałem polecenie, sądziłem że pytania maja być pobierane z pliku 

    private int liczba_punktow = 0;
    private int currentQuestionIndex = 0;
    private List<Pytanie> bazaPytan;
    private RadioButton radio1;
    private RadioButton radio2;
    private RadioButton radio3;
    private Button dalejButton;
    private ImageView obraz;
    private TextView pytanie;
    private RadioGroup radio_group;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        radio1 = findViewById(R.id.Radio1);
        radio2 = findViewById(R.id.Radio2);
        radio3 = findViewById(R.id.Radio3);
        dalejButton = findViewById(R.id.dalejButton);
        obraz = findViewById(R.id.Obraz);
        pytanie = findViewById(R.id.Pytanie);
        radio_group = findViewById(R.id.radio_group);

        bazaPytan = wczytajPytaniaZPliku();

        if (bazaPytan != null && !bazaPytan.isEmpty()) {
            wyswietlAktualnePytanie();
        } else {
            pytanie.setText("Błąd: Nie udało się załadować pytań z pliku.");
            dalejButton.setEnabled(false);
        }

        dalejButton.setOnClickListener(v -> obsluzPrzejscieDalej());
    }

    private List<Pytanie> wczytajPytaniaZPliku() {
        List<Pytanie> pobranePytania = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("pytania.txt"), "UTF-8"))) {
            String linia;
            String trescPytania = "";
            List<String> odpowiedzi = new ArrayList<>();
            String poprawnaOdpowiedz = "";

            while ((linia = reader.readLine()) != null) {
                linia = linia.trim();

                if (linia.startsWith("pytanie ")) {
                    trescPytania = reader.readLine().trim();
                } else if (linia.equals("odpowiedzi")) {
                    odpowiedzi = new ArrayList<>();
                    odpowiedzi.add(reader.readLine().trim());
                    odpowiedzi.add(reader.readLine().trim());
                    odpowiedzi.add(reader.readLine().trim());
                } else if (linia.equals("odpowiedz poprawna")) {
                    poprawnaOdpowiedz = reader.readLine().trim();
                } else if (linia.startsWith("obraz ")) {
                    String nazwaPliku = linia.replace("obraz ", "").replace(".jpg", "").trim();
                    int resIdObrazka = getResources().getIdentifier(nazwaPliku, "drawable", getPackageName());

                    pobranePytania.add(new Pytanie(trescPytania, odpowiedzi, poprawnaOdpowiedz, resIdObrazka));
                }
            }
        } catch (IOException ignored) {
        }

        return pobranePytania;
    }

    private void wyswietlAktualnePytanie() {
        radio_group.clearCheck();

        Pytanie aktualne = bazaPytan.get(currentQuestionIndex);

        pytanie.setText(aktualne.getTrescPytania());
        radio1.setText(aktualne.getOdpowiedzi().get(0));
        radio2.setText(aktualne.getOdpowiedzi().get(1));
        radio3.setText(aktualne.getOdpowiedzi().get(2));

        int resId = aktualne.getResIdObrazka();
        if (resId != 0) {
            obraz.setImageResource(resId);
        } else {
            obraz.setImageDrawable(null);
        }
    }

    private String getWybranaOdpowiedzTekst() {
        if (radio1.isChecked()) return radio1.getText().toString();
        if (radio2.isChecked()) return radio2.getText().toString();
        if (radio3.isChecked()) return radio3.getText().toString();
        return "";
    }

    private void obsluzPrzejscieDalej() {
        String wybranaOdpowiedz = getWybranaOdpowiedzTekst();

        if (wybranaOdpowiedz.isEmpty()) {
            Toast.makeText(this, "Proszę zaznaczyć jedną odpowiedź!", Toast.LENGTH_SHORT).show();
            return;
        }

        Pytanie aktualne = bazaPytan.get(currentQuestionIndex);
        if (wybranaOdpowiedz.equals(aktualne.getPoprawnaOdpowiedz())) {
            liczba_punktow++;
        }

        if (currentQuestionIndex < bazaPytan.size() - 1) {
            currentQuestionIndex++;
        } else {
            Toast.makeText(this, "Koniec quizu! Zdobyte punkty: " + liczba_punktow + "/" + bazaPytan.size(), Toast.LENGTH_LONG).show();
            currentQuestionIndex = 0;
            liczba_punktow = 0;
        }

        wyswietlAktualnePytanie();
    }
}