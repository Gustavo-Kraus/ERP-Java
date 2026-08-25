package dev.kraus.ERP.Config.DadosInicias.Usuarios;


import dev.kraus.ERP.Model.Filial.Filiais;
import dev.kraus.ERP.Model.Token.ApiToken;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Filiais.FiliaisRepository;
import dev.kraus.ERP.Repository.Usuarios.UsuariosRepository;
import dev.kraus.ERP.Service.Tokens.ApiTokenService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDadosInicial {

    private final UsuariosRepository usuariosRepository;
    private final ApiTokenService apiTokenService;
    private final FiliaisRepository filiaisRepository;

    public UsuarioDadosInicial(UsuariosRepository usuariosRepository, ApiTokenService apiTokenService, FiliaisRepository filiaisRepository) {
        this.usuariosRepository = usuariosRepository;
        this.apiTokenService = apiTokenService;
        this.filiaisRepository = filiaisRepository;
    }

    //aqui eu puxo se está como modo desenvoledor ativo, se tiver o if vai impedir de gerar novas linhas
    @Value("${developer-mode}")
    private boolean modoDesenvolvedor;

    @Transactional
    @Order(999)
    @EventListener(ApplicationReadyEvent.class)
    public void iniciar() {
        if (!modoDesenvolvedor) {
            System.out.println("Modo produção detectado. Não será criado usuário padrão.");
            return;
        }

        if (usuariosRepository.count() > 0) {
            System.out.println("Usuário padrão já existe.");
            return;
        }

        Filiais filial = filiaisRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Nenhuma filial encontrada. Não foi possível criar o usuario padrão."
                        )
                );


        Usuarios usuarios = new Usuarios();
        usuarios.setNome("admin");
        usuarios.setSenha("1234");
        usuarios.setEmail("teste@test.com");
        usuarios.setRole("ADMIN");
        usuarios.setFiliais(filial);
        Usuarios usuarioSalvo = usuariosRepository.save(usuarios);
        System.out.println("Usuário padrão criado com sucesso: " + usuarioSalvo.getEmail() + " e senha: " + usuarioSalvo.getSenha());

        ApiToken apiToken = apiTokenService.gerarToken(usuarioSalvo);
        System.out.println("Token gerado para o usuário: " + apiToken.getToken());
    }
}
