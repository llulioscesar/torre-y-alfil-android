package co.jcesar.torreyalfil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import co.jcesar.torreyalfil.clases.Tablero;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Tablero tablero;
    TextView consola;

    public static final String FILAS  = "FILAS";
    public static final String COLUMNAS  = "COLUMNAS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        int filas = 8;
        int columnas = 8;

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            filas = bundle.getInt(FILAS);
            columnas = bundle.getInt(COLUMNAS);
        }

        tablero = new Tablero(this,filas,columnas);
        consola = findViewById(R.id.consola);

        tablero.OnChangeFichaListener(new Tablero.OnChangeFicha() {
            @Override
            public void getFichaSeleccionada(String msj) {
                consola.append("\n"+msj);
                consola.setMovementMethod(new ScrollingMovementMethod());
            }
        });

        tablero.OnFinishGameListener(new Tablero.OnFinishGame() {
            @Override
            public void gameEnd() {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setCancelable(false).create();
                alertDialog.setMessage("Fin del juego");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Nueva partida",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                startActivity(new Intent(MainActivity.this, ConfigurarTablero.class));
                                MainActivity.this.finish();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
