package com.example.chesstimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    Button setTimeButton, resetTimeButton; // przyciski setTimer i resetTimer
    ConstraintLayout playerOneConstraint, playerTwoConstraint; // layout ekranu dwie połowy
    TextView playerOneCountdown, playerTwoCountdown; // TextView do wyswietlania tekstu (zegar)
    CountDownTimer playerTwoCountDownTimer = null; // timer
    CountDownTimer playerOneCountDownTimer = null; // timer
    long playerOneLeftTime, playerTwoLeftTime, gameTime; // zmienne do przetrzymywania czasu w milisekundach typ long
    int countDownInterval = 1000; // odliczamy po sekundach

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // tutaj łączymy zmienne z gory z layoutami kazdy ma swoj id w pliku activity_main i tak je odnajdujemy, wtedy uzyskujemy dostep do tych widoków
        playerOneConstraint = findViewById(R.id.player_one_constraint);
        playerTwoConstraint = findViewById(R.id.player_two_constraint);
        playerOneCountdown = findViewById(R.id.player_one_countdown_textView);
        playerTwoCountdown = findViewById(R.id.player_two_countdown_textView);
        setTimeButton = (Button) findViewById(R.id.set_time_button);
        resetTimeButton = findViewById(R.id.reset_time_button);

        // onClicklistener daje nam akcje dla nacisniecia przycisku badz jakiegokolwiek widoku
        // metoda, ktora wyswietla dialogbox(ten dialog co sie wpisuje czas gry)
        setTimeButton.setOnClickListener(v -> showAddItemDialog(MainActivity.this));

        // onClicklistener daje nam akcje dla nacisniecia przycisku badz jakiegokolwiek widoku
        resetTimeButton.setOnClickListener(v -> {
            resetTimer(); // metoda zerujaca zegary
        });

        // onClicklistener daje nam akcje dla nacisniecia przycisku badz jakiegokolwiek widoku
        playerOneConstraint.setOnClickListener(v -> {
            if (playerTwoLeftTime > 0 && playerOneLeftTime > 0) // if to wiadomo
            {
                // tworzymy nowy obiekt CountDownTimer jako argument podajemy czas do odliczania i interwal
                playerTwoCountDownTimer = new CountDownTimer(playerTwoLeftTime, countDownInterval)
                {
                    @Override
                    public void onTick(long millisUntilFinished)
                    {
                        // onTick wywoluje się po kazdym interwale u nas to 1000milisekund czyli 1 sekunda
                        playerTwoCountdown.setText(convertMillisToTime(millisUntilFinished)); // ustawiamy zegar drugiego gracza jako czas do konca odliczania
                        playerTwoLeftTime = millisUntilFinished; // do zmiennej przypisujemy czas jaki został do konca gry
                    }

                    @Override
                    public void onFinish()
                    {
                        // onFinish to metoda, ktora wywoluje sie po skonczeniu odliczania i tutaj zmieniamy tło polowek graczy oraz zmieniamy zegar na tekst
                        playerOneConstraint.setBackgroundColor(getResources().getColor(R.color.pastelGreen));
                        playerOneCountdown.setText(getResources().getString(R.string.winner));
                        playerTwoConstraint.setBackgroundColor(getResources().getColor(R.color.pastelRed));
                        playerTwoCountdown.setText(getResources().getString(R.string.loser));
                    }
                };
                playerTwoCountDownTimer.start(); // startujemy z odliczaniem czasu player2
            }

            pauseTimer(playerOneCountDownTimer);//pausujemy odliczanie player1
        });

        // onClicklistener daje nam akcje dla nacisniecia przycisku badz jakiegokolwiek widoku
        playerTwoConstraint.setOnClickListener(v -> {
            if (playerOneLeftTime > 0 && playerTwoLeftTime > 0)
            {
                // tworzymy nowy obiekt CountDownTimer jako argument podajemy czas do odliczania i interwal
                playerOneCountDownTimer = new CountDownTimer(playerOneLeftTime, countDownInterval)
                {
                    @Override
                    public void onTick(long millisUntilFinished)
                    {
                        // onTick wywoluje się po kazdym interwale u nas to 1000milisekund czyli 1 sekunda
                        playerOneCountdown.setText(convertMillisToTime(millisUntilFinished));
                        playerOneLeftTime = millisUntilFinished;
                    }

                    @Override
                    public void onFinish()
                    {
                        // onFinish to metoda, ktora wywoluje sie po skonczeniu odliczania i tutaj zmieniamy tło polowek graczy oraz zmieniamy zegar na tekst
                        playerTwoConstraint.setBackgroundColor(getResources().getColor(R.color.pastelGreen));
                        playerTwoCountdown.setText(getResources().getString(R.string.winner));
                        playerOneConstraint.setBackgroundColor(getResources().getColor(R.color.pastelRed));
                        playerOneCountdown.setText(getResources().getString(R.string.loser));
                    }
                };
                playerOneCountDownTimer.start(); // startujemy odliczanie player1
            }

            pauseTimer(playerTwoCountDownTimer);// pausujemy odlcizanie player2
        });

    }

    void resetTimer()
    {
        // jezeli counddowntimer rozne od null, to cancel, zeby nie bylo wyciekow pamieci.
        if(playerOneCountDownTimer!=null)
        {
            playerOneCountDownTimer.cancel();
        }

        if(playerTwoCountDownTimer!=null)
        {
            playerTwoCountDownTimer.cancel();
        }

        // ustawiamy zegar na 0 oraz zmienne do konca czasu na 0
        playerOneCountdown.setText(getResources().getString(R.string.default_timer));
        playerTwoCountdown.setText(getResources().getString(R.string.default_timer));
        playerOneLeftTime = 0;
        playerTwoLeftTime = 0;
    }

    //zatrzymujemy timer
    void pauseTimer(CountDownTimer countDownTimer)
    {
        if(countDownTimer!=null)
        {
            countDownTimer.cancel();
        }

    }

    //zamieniamy milisekundy na czas w formacie "HH:mm:ss"
    String convertMillisToTime(long millis)
    {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(new Date(millis));
    }

    void showAddItemDialog(Context c)
    {
        final EditText timerEditText = new EditText(c);
        timerEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle(getResources().getString(R.string.set_timer))
                .setMessage(getResources().getString(R.string.set_timer_hint_dialog))
                .setView(timerEditText)
                .setPositiveButton(getResources().getString(R.string.set_timer_dialog), (dialog1, which) -> {

                    long editText = Long.parseLong(String.valueOf(timerEditText.getText()));
                    gameTime = editText * 60000;
                    playerOneCountdown.setText(convertMillisToTime(gameTime));
                    playerTwoCountdown.setText(convertMillisToTime(gameTime));
                    playerOneLeftTime = gameTime;
                    playerTwoLeftTime = gameTime;
                    playerTwoConstraint.setBackgroundColor(getResources().getColor(R.color.playerTwoBackground));
                    playerOneConstraint.setBackgroundColor(getResources().getColor(R.color.playerOneBackground));
                })
                .setNegativeButton(getResources().getString(R.string.cancel_timer_dialog), null)
                .create();
        dialog.show();
    }
}