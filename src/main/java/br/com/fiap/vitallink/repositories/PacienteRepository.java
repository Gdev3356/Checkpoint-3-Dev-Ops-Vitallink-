package br.com.fiap.vitallink.repositories;

import br.com.fiap.vitallink.entities.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Page<Paciente> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}