package dev.kraus.ERP.ServiceAPI.Cache.Clientes;

import dev.kraus.ERP.DTO.Clientes.ClienteResponse;
import dev.kraus.ERP.Repository.Clientes.ClientesRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientesCacheService {

    private final ClientesRepository clientesRepository;

    public ClientesCacheService(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    @Cacheable(cacheNames = "clientes", key = "'todos'")
    public List<ClienteResponse> listarClientes() {
        return clientesRepository.findAll()
                .stream()
                .map(cliente -> new ClienteResponse(
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getNomeFantasia(),
                        cliente.getRazaoSocial(),
                        cliente.getCnpjCpf(),
                        cliente.getTelefone(),
                        cliente.getCelular(),
                        cliente.getEmail(),
                        cliente.getNumeroCasa(),
                        cliente.getBairro(),
                        cliente.getCep(),
                        cliente.getRua(),
                        cliente.getCriadoEm(),
                        cliente.getEditadoEm(),
                        cliente.getExcluidoEm()
                ))
                .toList();
    }
}
