package dev.kraus.ERP.Model.Produtos.Dimensoes;

import jakarta.persistence.*;


@Table(name = "produtosCategoria")
@Entity
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String marca;
}
