package dev.kraus.ERP.Config.DadosInicias.Usuarios;


import dev.kraus.ERP.Model.Token.ApiToken;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Usuarios.UsuariosRepository;
import dev.kraus.ERP.Service.Tokens.ApiTokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDadosInicial implements ApplicationRunner {

    private final UsuariosRepository usuariosRepository;
    private final ApiTokenService apiTokenService;

    public UsuarioDadosInicial(UsuariosRepository usuariosRepository, ApiTokenService apiTokenService) {
        this.usuariosRepository = usuariosRepository;
        this.apiTokenService = apiTokenService;
    }
    //aqui eu puxo se está como modo desenvoledor ativo, se tiver o if vai impedir de gerar novas linhas
    @Value("${developer-mode}")
    private boolean modoDesenvolvedor;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!modoDesenvolvedor) {
            System.out.println("Modo produção detectado. Não será criado usuário padrão.");
            return;
        }

        if (usuariosRepository.count() > 0) {
            System.out.println("Usuário padrão já existe.");
            return;
        }


        Usuarios usuarios = new Usuarios();
        usuarios.setNome("admin");
        usuarios.setSenha("Binho1379@");
        usuarios.setEmail("teste@test.com");
        usuarios.setRole("ADMIN");
        Usuarios usuarioSalvo = usuariosRepository.save(usuarios);
        System.out.println("Usuário criado. ID: " + usuarioSalvo.getId());

        ApiToken apiToken = apiTokenService.gerarToken(usuarioSalvo);
        System.out.println("Token gerado para o usuário: " + apiToken.getToken());
    }
}
