package dev.kraus.ERP.Model.Produtos.Dimensoes;

import dev.kraus.ERP.Model.Filial.Filiais;
import jakarta.persistence.*;

@Table(name = "produtosCategoria")
@Entity
public class Almoxarifado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String almoxarifado;

    @ManyToOne(optional = false)
    private Filiais filiais;

}