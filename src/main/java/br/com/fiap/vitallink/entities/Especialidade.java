package br.com.fiap.vitallink.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TBL_ESPECIALIDADE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Especialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESPECIALIDADE")
    private Long id;

    @Column(name = "NOME", nullable = false, unique = true, length = 100)
    private String nome;

    @Column(name = "DESCRICAO", length = 300)
    private String descricao;
}
