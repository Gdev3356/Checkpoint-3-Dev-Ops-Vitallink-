package br.com.fiap.vitallink.controllers;

import br.com.fiap.vitallink.dto.EspecialidadeRequestDTO;
import br.com.fiap.vitallink.dto.EspecialidadeResponseDTO;
import br.com.fiap.vitallink.services.EspecialidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
@Tag(name = "Especialidades", description = "Gerenciamento de especialidades médicas")
public class EspecialidadeController {

    private final EspecialidadeService service;

    @GetMapping
    @Operation(summary = "Listar especialidades (paginado, ordenado por nome)")
    public Page<EspecialidadeResponseDTO> listar(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return service.listar(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar especialidade por ID")
    public EspecialidadeResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar nova especialidade")
    public EspecialidadeResponseDTO criar(@RequestBody @Valid EspecialidadeRequestDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar especialidade")
    public EspecialidadeResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid EspecialidadeRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar especialidade")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}