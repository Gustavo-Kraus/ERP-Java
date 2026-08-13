package dev.kraus.ERP.ServiceAPI.Clientes;


import dev.kraus.ERP.Controller.API.RespostaErros.GlobalException;
import dev.kraus.ERP.DTO.Clientes.ClienteRequest;
import dev.kraus.ERP.DTO.Clientes.ClienteResponse;
import dev.kraus.ERP.Mapper.Clientes.ClienteMapper;
import dev.kraus.ERP.Model.Clientes.Clientes;
import dev.kraus.ERP.Model.Produtos.Produtos;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Clientes.ClientesRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.client.endpoint.RestClientTokenExchangeTokenResponseClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClientesServiceAPI {

    private final ClientesRepository clientesRepository;
    private final ClienteMapper mapper;

    public ClientesServiceAPI(ClientesRepository clientesRepository, ClienteMapper mapper) {
        this.clientesRepository = clientesRepository;
        this.mapper = mapper;
    }

    public List<ClienteResponse> listarClientes(Usuarios usuario) {
        validarUsuario(usuario);
        return clientesRepository.findAll()
                .stream()
                .map( clientes -> new ClienteResponse(
                        clientes.getId(),
                        clientes.getNome(),
                        clientes.getNomeFantasia(),
                        clientes.getRazaoSocial(),
                        clientes.getCnpjCpf(),
                        clientes.getTelefone(),
                        clientes.getCelular(),
                        clientes.getEmail(),
                        clientes.getNumeroCasa(),
                        clientes.getBairro(),
                        clientes.getCep(),
                        clientes.getRua(),
                        clientes.getCriadoEm(),
                        clientes.getEditadoEm(),
                        clientes.getExcluidoEm()
                ))
                .toList();
    }

    public Clientes listarClientePorID(Long id, Usuarios usuario) {
        validarUsuario(usuario);

        return clientesRepository.findById(id)
                .orElseThrow(() -> new GlobalException("Cliente nao encontrado"));
    }

    public Clientes salvarCliente(ClienteRequest request, Usuarios usuarios){
        validarUsuario(usuarios);
        Clientes clientes = mapper.toEntity(request);

        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new GlobalException("Nome do cliente e obrigatorio");
        }
        return clientesRepository.save(clientes);

    }

    public Clientes deletarCliente(Long id, Usuarios usuario) {
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

    public Clientes atualizarCliente(Long id, ClienteRequest request ,Usuarios usuarios){
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
