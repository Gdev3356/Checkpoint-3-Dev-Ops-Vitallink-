package br.com.fiap.vitallink.repositories;

import br.com.fiap.vitallink.entities.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    boolean existsByCrm(String crm);

    boolean existsByEmail(String email);

    Page<Medico> findByAtivoAndNomeContainingIgnoreCase(String ativo, String nome, Pageable pageable);

    Page<Medico> findByAtivo(String ativo, Pageable pageable);

    Page<Medico> findByAtivoAndEspecialidadeId(String ativo, Long especialidadeId, Pageable pageable);
}