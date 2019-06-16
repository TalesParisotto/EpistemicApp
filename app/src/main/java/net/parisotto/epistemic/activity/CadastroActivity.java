package net.parisotto.epistemic.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import net.parisotto.epistemic.R;
import net.parisotto.epistemic.config.ConfiguracaoFirebase;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoEmail,campoSenha;
    private Button botaocadastro;
    private FirebaseAuth autenticacao;

    private String email;
    private String senha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoEmail = findViewById(R.id.editEmailCadastro);
        campoSenha = findViewById(R.id.editSenhaCadastro);

        botaocadastro = findViewById(R.id.botaoCadastro);

        botaocadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = campoEmail.getText().toString();
                senha = campoSenha.getText().toString();
                if (!email.isEmpty()) {
                    if (!senha.isEmpty()) {
                        email =  email.trim();
                        senha = senha.trim();
                        cadastrarUsuario();
                    } else {
                        Toast.makeText(CadastroActivity.this,
                                "Preencha o email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CadastroActivity.this,
                            "Preencha o nome", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseautenticacao();
        autenticacao.createUserWithEmailAndPassword(
                email,senha
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this,
                            "Usuario cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    String execao = "";
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        execao = "digite uma senha mais forte";
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        execao = "Digite um email valido";
                    }catch (FirebaseAuthUserCollisionException e){
                        execao = "ja foi criado esse email";
                    }catch (Exception e){
                        execao = "Erro ao cadastrar";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this,
                            execao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
