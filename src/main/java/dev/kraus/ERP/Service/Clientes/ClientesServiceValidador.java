package dev.kraus.ERP.Service.Clientes;


import dev.kraus.ERP.Model.Clientes.Clientes;
import dev.kraus.ERP.Repository.Clientes.ClientesRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientesServiceValidador {

    private final ClientesRepository clientesRepository;

    public ClientesServiceValidador(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    //Valida se o nome do cliente está vazio
    public void validarCamposObrigadotorios(Clientes clientes) {
        if (clientes == null) {
            throw new RuntimeException("Cliente não informado.");
        }

        if (clientes.getNome() == null || clientes.getNome().isBlank()) {
            throw new RuntimeException("O campo 'nome' é obrigatório.");
        }
    }

    //Valida se o cpfCnpj está vazio
    public void validaCpfCnpj(Clientes clientes) {
        if (clientes == null) {
            throw new RuntimeException("Cliente não informado.");
        }

        String cpfCnpj = clientes.getCnpjCpf();

        if (cpfCnpj == null || cpfCnpj.isBlank()) {
            throw new RuntimeException("CPF/CNPJ é obrigatório.");
        }
    }
}
