package dev.kraus.ERP.Service.Clientes;


import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Clientes.ClientesRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import dev.kraus.ERP.Model.Clientes.Clientes;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class clientesService {

    private final ClientesRepository clientesRepository;
    private final ClientesServiceValidador clientesServiceValidador;

    public clientesService(ClientesRepository clientesRepository, ClientesServiceValidador clientesServiceValidador) {
        this.clientesRepository = clientesRepository;
        this.clientesServiceValidador = clientesServiceValidador;
    }

    public Clientes salvar(Clientes clientes, Usuarios usuarios) {

        clientesServiceValidador.validarCamposObrigadotorios(clientes);

        clientesServiceValidador.validaCpfCnpj(clientes);

        return clientesRepository.save(clientes);
    }


    //função para listar os clientes
    public List<Clientes> listar(String busca) {
        Pageable limite = PageRequest.of(0, 6);

        if (busca == null || busca.isBlank()) {
            return clientesRepository.findByExcluidoEmIsNullOrderByCriadoEmDesc(limite);
        }

        return clientesRepository.buscarAtivos(busca.trim(), limite);
    }

    //função para deletar o cliente, que na verdade só coloca uma data de exclusão, mas não apaga de fato
    public Clientes deletar(Long id){
        Clientes clientes = clientesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        clientes.setExcluidoEm(java.time.LocalDateTime.now());
        return clientesRepository.save(clientes);
    }

    //função para editar o cliente
    public Clientes editar(Clientes clientes, Long id) {
        Clientes clienteExistente = clientesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        clienteExistente.setNome(clientes.getNome());
        clienteExistente.setEmail(clientes.getEmail());
        clienteExistente.setCnpjCpf(clientes.getCnpjCpf());
        clienteExistente.setNomeFantasia(clientes.getNomeFantasia());
        clienteExistente.setRazaoSocial(clientes.getRazaoSocial());
        clienteExistente.setEditadoEm(java.time.LocalDateTime.now());
        clienteExistente.setTelefone(clientes.getTelefone());
        clienteExistente.setCelular(clientes.getCelular());
        clienteExistente.setCep(clientes.getCep());
        clienteExistente.setRua(clientes.getRua());
        clienteExistente.setBairro(clientes.getBairro());
        clienteExistente.setNumeroCasa(clientes.getNumeroCasa());

        return clientesRepository.save(clienteExistente);
    }
}
