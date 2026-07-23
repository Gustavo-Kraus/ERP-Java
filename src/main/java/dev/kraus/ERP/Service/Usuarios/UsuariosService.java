package dev.kraus.ERP.Service.Usuarios;



import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Usuarios.UsuariosRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuariosService implements UserDetailsService {


    private final UsuariosRepository usuariosRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuariosService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    //aqui é onde o usuário é salvo no banco de dados, utilizando as informações do token de autenticação do Google

    public Usuarios salvar(OAuth2AuthenticationToken token){
            String email = token.getPrincipal().getAttribute("email");
            String nome = token.getPrincipal().getAttribute("name");

            Optional<Usuarios> usuarioExistente = usuariosRepository.findByEmail(email);

            Usuarios usuarios = usuarioExistente.orElseGet(Usuarios::new);
            usuarios.setNome(nome);
            usuarios.setEmail(email);
        return usuariosRepository.save(usuarios);
    }

    //aqui ele salva caso nao seja pelo google

    public Usuarios registrarUsuario(String nome, String email, String senha) {
        System.out.println("Tentando registrar usuário: " + nome + ", " + email);
        if (usuariosRepository.findByEmail(email).isPresent()) {
            System.out.println("Email já cadastrado: " + email);
            throw new RuntimeException("Email já cadastrado");
        }
        Usuarios usuario = new Usuarios();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(senha));
        System.out.println("Salvando usuário: " + usuario);
        Usuarios saved = usuariosRepository.save(usuario);
        System.out.println("Usuário salvo com ID: " + saved.getId());
        return saved;
    }

    //aqui é onde o usuário é carregado a partir do email, para ser utilizado na autenticação do Spring Security

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuariosRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    //aqui é onde o password encoder é retornado para ser utilizado em outras partes do sistema, como na autenticação

    public BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
