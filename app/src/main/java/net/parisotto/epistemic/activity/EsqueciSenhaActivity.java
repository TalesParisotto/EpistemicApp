package net.parisotto.epistemic.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import net.parisotto.epistemic.R;

public class EsqueciSenhaActivity extends AppCompatActivity {
    private EditText campoEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);

        campoEmail = findViewById(R.id.editEmailEsqueciS);

        Bundle dados = getIntent().getExtras();

        if(dados != null) {
            String email = dados.getString("email");
            campoEmail.setText(email);
        }
    }
}
