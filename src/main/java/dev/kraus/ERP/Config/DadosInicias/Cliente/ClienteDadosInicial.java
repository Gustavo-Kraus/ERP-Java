package dev.kraus.ERP.Config.DadosInicias.Cliente;

import dev.kraus.ERP.Model.Clientes.Clientes;
import dev.kraus.ERP.Repository.Clientes.ClientesRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ClienteDadosInicial implements ApplicationRunner{

    private final ClientesRepository clientesRepository;

    public ClienteDadosInicial(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
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

