package co.jcesar.torreyalfil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ConfigurarTablero extends AppCompatActivity {

    TextInputEditText mRowText, mColText;
    TextInputLayout mLayoutRowText, mLayoutColText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurar_tablero);

        mRowText = findViewById(R.id.row_text);
        mColText = findViewById(R.id.col_text);
        mLayoutColText = findViewById(R.id.layout_col_text);
        mLayoutRowText = findViewById(R.id.layout_row_text);

        mRowText.addTextChangedListener(textWatcher);
        mColText.addTextChangedListener(textWatcher);

    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(mRowText.getText().toString().equals("") || mColText.getText().toString().equals("")){
                ((MaterialButton)findViewById(R.id.btnCrear)).setEnabled(false);
                ((MaterialButton)findViewById(R.id.btn_jugar)).setEnabled(true);
            } else {
                ((MaterialButton)findViewById(R.id.btnCrear)).setEnabled(true);
                ((MaterialButton)findViewById(R.id.btn_jugar)).setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void btCancelarClick(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void btCrearTablero(View view){
        if(mRowText.getText().toString().equals("")){
            mLayoutRowText.setError("Ingrese el numero de filas");
        } else if(mColText.getText().toString().equals("")){
            mLayoutColText.setError("Ingrese el numero de columnas");
        } else {
            Intent intent = new Intent(ConfigurarTablero.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(MainActivity.FILAS, Integer.parseInt(mColText.getText().toString()));
            bundle.putInt(MainActivity.COLUMNAS, Integer.parseInt(mRowText.getText().toString()));
            intent.putExtras(bundle);
            startActivity(intent);
            ConfigurarTablero.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
