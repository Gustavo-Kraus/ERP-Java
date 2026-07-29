package dev.kraus.ERP.ServiceAPI.Clientes;


import dev.kraus.ERP.Controller.API.RespostaErros.ProdutosException;
import dev.kraus.ERP.Mapper.Clientes.ClienteMapper;
import dev.kraus.ERP.Model.Clientes.Clientes;
import dev.kraus.ERP.Model.Produtos.Produtos;
import dev.kraus.ERP.Model.Usuarios.Usuarios;
import dev.kraus.ERP.Repository.Clientes.ClientesRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientesServiceAPI {

    private final ClientesRepository clientesRepository;
    private final ClienteMapper mapper;

    public ClientesServiceAPI(ClientesRepository clientesRepository, ClienteMapper mapper) {
        this.clientesRepository = clientesRepository;
        this.mapper = mapper;
    }

    public List<Clientes> listarClientes(String busca, Usuarios usuario) {
        validarUsuario(usuario);

        Pageable limite = PageRequest.of(0, 6);

        if (busca == null || busca.isBlank()) {
            return clientesRepository.findByExcluidoEmIsNullOrderByCriadoEmDesc(limite);
        }

        return clientesRepository.buscarAtivos(busca.trim(), limite);
    }



    private void validarUsuario(Usuarios usuario) {
        if (usuario == null || !usuario.isEnabled()) {
            throw new ProdutosException("Usuario autenticado invalido");
        }
    }
}
