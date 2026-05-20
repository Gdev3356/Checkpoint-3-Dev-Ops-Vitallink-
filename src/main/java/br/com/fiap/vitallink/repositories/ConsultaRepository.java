package br.com.fiap.vitallink.repositories;

import br.com.fiap.vitallink.entities.Consulta;
import br.com.fiap.vitallink.enums.StatusConsulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    Page<Consulta> findByStatus(StatusConsulta status, Pageable pageable);

    Page<Consulta> findByMedicoId(Long medicoId, Pageable pageable);

    Page<Consulta> findByPacienteId(Long pacienteId, Pageable pageable);

    @Query("SELECT c FROM Consulta c WHERE c.dataHora BETWEEN :inicio AND :fim ORDER BY c.dataHora ASC")
    Page<Consulta> findByPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            Pageable pageable
    );
}
