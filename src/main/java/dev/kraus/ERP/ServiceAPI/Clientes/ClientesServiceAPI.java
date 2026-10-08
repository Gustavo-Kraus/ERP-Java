package dev.kraus.ERP.ServiceAPI.Clientes;


import dev.kraus.ERP.Controller.API.RespostaErros.GlobalException;
import dev.kraus.ERP.DTO.Clientes.ClienteRequest;
import dev.kraus.ERP.DTO.Clientes.ClienteResponse;
import dev.kraus.ERP.Mapper.Clientes.ClienteMapper;
import dev.kraus.ERP.Model.Clientes.Clientes;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Clientes.ClientesRepository;
import dev.kraus.ERP.ServiceAPI.Cache.Clientes.ClientesCacheService;
import dev.kraus.ERP.ServiceAPI.Permissoes.PermissoesServiceClientes;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientesServiceAPI {

    private final ClientesRepository clientesRepository;
    private final ClienteMapper mapper;
    private final PermissoesServiceClientes permissoesServiceClientes;
    private final ClientesCacheService clientesCacheService;

    public ClientesServiceAPI(ClientesRepository clientesRepository, ClienteMapper mapper, PermissoesServiceClientes permissoesServiceClientes, ClientesCacheService clientesCacheService) {
        this.clientesRepository = clientesRepository;
        this.mapper = mapper;
        this.permissoesServiceClientes = permissoesServiceClientes;
        this.clientesCacheService = clientesCacheService;
    }

    public List<ClienteResponse> listarClientes(Usuarios usuario) {

        permissoesServiceClientes.verificarPodeListarCliente(usuario);
        validarUsuario(usuario);

        return clientesCacheService.listarClientes();
    }

    public Clientes listarClientePorID(Long id, Usuarios usuario) {
        permissoesServiceClientes.verificarPodeListarCliente(usuario);
        validarUsuario(usuario);
        return clientesRepository.findById(id)
                .orElseThrow(() -> new GlobalException("Cliente nao encontrado"));
    }

    @CacheEvict(cacheNames = "clientes", allEntries = true)
    public Clientes salvarCliente(ClienteRequest request, Usuarios usuarios){
        permissoesServiceClientes.verificarPodeCadastrarCliente(usuarios);
        validarUsuario(usuarios);
        Clientes clientes = mapper.toEntity(request);

        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new GlobalException("Nome do cliente e obrigatorio");
        }
        return clientesRepository.save(clientes);

    }

    @CacheEvict(cacheNames = "clientes", allEntries = true)
    public Clientes deletarCliente(Long id, Usuarios usuario) {
        permissoesServiceClientes.verificarPodeExcluirCliente(usuario);
        validarUsuario(usuario);
        Clientes clientes = clientesRepository.findById(id)
                .orElseThrow(() -> new GlobalException("Cliente nao encontrado"));

        clientesRepository.findByExcluidoEmIsNullAndId(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new GlobalException("ja excluido"));

        clientes.setExcluidoEm(LocalDateTime.now());
        return clientesRepository.save(clientes);
    }

    @CacheEvict(cacheNames = "clientes", allEntries = true)
    public Clientes atualizarCliente(Long id, ClienteRequest request ,Usuarios usuarios){
        permissoesServiceClientes.verificarPodeEditarCliente(usuarios);
        validarUsuario(usuarios);

        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new GlobalException("Nome do cliente e obrigatorio");
        }

        Clientes clientes = clientesRepository.findByExcluidoEmIsNullAndId(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new GlobalException("Cliente excludo, impossivel atualizar, não vai rolar, se quiser reativar, use /reativar"));

        return clientesRepository.findById(id)
                .map(cliente -> {
                    cliente.setNome(request.getNome());
                    cliente.setEmail(request.getEmail());
                    //cliente.setTelefone(request.getTelefone());
                    //cliente.setEndereco(request.getEndereco());
                    cliente.setEditadoEm(LocalDateTime.now());
                    return clientesRepository.save(cliente);
                })
                .orElseThrow(() -> new GlobalException("Cliente nao encontrado"));
    }


    private void validarUsuario(Usuarios usuario) {
        if (usuario == null || !usuario.isEnabled()) {
            throw new GlobalException("Usuario autenticado invalido");
        }
    }
}
