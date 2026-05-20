package br.com.fiap.vitallink.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TBL_MEDICO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MEDICO")
    private Long id;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "CRM", nullable = false, unique = true, length = 20)
    private String crm;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 150)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ESPECIALIDADE", nullable = false)
    private Especialidade especialidade;

    /**
     * S = ativo, N = inativo (soft delete)
     */
    @Column(name = "ATIVO", nullable = false, length = 1)
    @Builder.Default
    private String ativo = "S";
}