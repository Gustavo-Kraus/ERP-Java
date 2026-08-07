package dev.kraus.ERP.Model.Produtos.Dimensoes;

import dev.kraus.ERP.Model.Filial.Filiais;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "almoxarifado")
@Entity
@Getter
@Setter
public class Almoxarifado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String almoxarifado;

    @ManyToOne(optional = false)
    private Filiais filiais;

}