package dev.kraus.ERP.DTO.Clientes;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

public record  ClienteResponse(
         Long id,
         String nome,
         String nomeFantasia,
         String razaoSocial,
         String cnpjCpf,
         String telefone,
         String celular,
         String email,
         String cep,
         String rua,
         String bairro,
         String numeroCasa,
         LocalDateTime criadoEm,
         LocalDateTime editadoEm,
         LocalDateTime excluidoEm
) {
}
