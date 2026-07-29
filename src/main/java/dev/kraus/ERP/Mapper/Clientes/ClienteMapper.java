package dev.kraus.ERP.Mapper.Clientes;


import dev.kraus.ERP.DTO.Clientes.ClienteRequest;
import dev.kraus.ERP.Model.Clientes.Clientes;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Clientes toEntity(ClienteRequest request){

        Clientes clientes = new Clientes();

        clientes.setNome(request.getNome());


        return clientes;
    }
}
