package dev.kraus.ERP.Config.DadosInicias.Cliente;

import dev.kraus.ERP.Model.Clientes.Clientes;
import dev.kraus.ERP.Repository.Clientes.ClientesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ClienteDadosInicial implements ApplicationRunner{

    private final ClientesRepository clientesRepository;

    public ClienteDadosInicial(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
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
        if (clientesRepository.count() > 0) {
            return;
        }
        Long documento = 00000000000L;
        while (clientesRepository.count() < 5000) {
            Clientes clientes = new Clientes();
            clientes.setNome("Padrão");
            clientes.setCnpjCpf(String.valueOf(documento));
            clientes.setNomeFantasia("Padrão");
            clientes.setRazaoSocial("Padrão");
            Clientes salva = clientesRepository.save(clientes);
            documento++;
        }
    }
}

