package net.parisotto.epistemic.activity;

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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import net.parisotto.epistemic.R;
import net.parisotto.epistemic.config.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    private EditText campoEmail,campoSenha;
    private TextView campoTermosUso,campoCadastrese,esqueceuSenha;
    private Button botaoEntrar;
    private FirebaseAuth autenticacao;

    private String textoEmail;
    private String textoSenha;

    private int erro = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        campoEmail = findViewById(R.id.editEmailEsqueciS);
        campoSenha = findViewById(R.id.editSenha);
        campoCadastrese = findViewById(R.id.textCadastrese);
        campoTermosUso = findViewById(R.id.textTermos);
        botaoEntrar = findViewById(R.id.botaoEntrar);
        esqueceuSenha = findViewById(R.id.textEsqueceuSenha);

        esqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esqueciMinhaSenha();
            }
        });

        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 textoEmail = campoEmail.getText().toString();
                 textoSenha = campoSenha.getText().toString();
                if (!textoEmail.isEmpty()) {
                    if (!textoSenha.isEmpty()) {
                        textoEmail = textoEmail.trim();
                        textoSenha = textoSenha.trim();
                        validarLogin();
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Preencha a senha", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,
                            "Preencha o email", Toast.LENGTH_SHORT).show();
                }
            }
        });

        campoCadastrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirCadastro();
            }
        });

        campoTermosUso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTermosDeUSo();
            }
        });


    }

    public void validarLogin(){
        autenticacao = ConfiguracaoFirebase.getFirebaseautenticacao();
        autenticacao.signInWithEmailAndPassword(
                textoEmail,textoSenha
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this,"Login ocorreu com sucesso", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                    startActivity(intent);
                }else {
                    String execao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        execao = "Email ou senha errado";
                        erro++;
                    }catch(FirebaseAuthInvalidCredentialsException e) {
                        execao = "Email ou senha errado";
                        erro++;
                    }catch (Exception e){
                        execao = "Erro ao cadastrar";
                        erro++;
                        e.printStackTrace();
                    }
                    Toast.makeText(MainActivity.this,
                            execao, Toast.LENGTH_SHORT).show();
                }
                if(erro == 3){
                    Toast.makeText(MainActivity.this,"Muitos erros, tente novamente mais tarde", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
        );
    }

    public void abrirCadastro(){
        startActivity(new Intent(this,CadastroActivity.class));
    }

    public void abrirTermosDeUSo(){
        startActivity(new Intent(this,TermosUsoActivity.class));
    }

    public void esqueciMinhaSenha(){
        if(campoEmail != null) {
            Intent intent = new Intent(getApplicationContext(), EsqueciSenhaActivity.class);
            String email = campoEmail.getText().toString();
            intent.putExtra("email", email);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), EsqueciSenhaActivity.class);
            startActivity(intent);
        }
    }

}
