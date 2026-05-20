package br.com.fiap.vitallink.services;

import br.com.fiap.vitallink.dto.EspecialidadeRequestDTO;
import br.com.fiap.vitallink.dto.EspecialidadeResponseDTO;
import br.com.fiap.vitallink.entities.Especialidade;
import br.com.fiap.vitallink.exceptions.BusinessException;
import br.com.fiap.vitallink.exceptions.ResourceNotFoundException;
import br.com.fiap.vitallink.repositories.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EspecialidadeService {

    private final EspecialidadeRepository repository;

    @Cacheable("especialidades")
    public Page<EspecialidadeResponseDTO> listar(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponse);
    }

    public EspecialidadeResponseDTO buscarPorId(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    @CacheEvict(value = "especialidades", allEntries = true)
    public EspecialidadeResponseDTO criar(EspecialidadeRequestDTO dto) {
        if (repository.existsByNomeIgnoreCase(dto.nome())) {
            throw new BusinessException("Especialidade '" + dto.nome() + "' já está cadastrada.");
        }
        var entity = Especialidade.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "especialidades", allEntries = true)
    public EspecialidadeResponseDTO atualizar(Long id, EspecialidadeRequestDTO dto) {
        var entity = findOrThrow(id);
        entity.setNome(dto.nome());
        entity.setDescricao(dto.descricao());
        return toResponse(repository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "especialidades", allEntries = true)
    public void deletar(Long id) {
        findOrThrow(id);
        repository.deleteById(id);
    }

    private Especialidade findOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Especialidade não encontrada com id: " + id));
    }

    private EspecialidadeResponseDTO toResponse(Especialidade e) {
        return new EspecialidadeResponseDTO(e.getId(), e.getNome(), e.getDescricao());
    }
}
