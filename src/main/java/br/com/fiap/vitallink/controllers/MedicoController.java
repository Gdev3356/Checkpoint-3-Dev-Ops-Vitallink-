package br.com.fiap.vitallink.controllers;

import br.com.fiap.vitallink.dto.MedicoRequestDTO;
import br.com.fiap.vitallink.dto.MedicoResponseDTO;
import br.com.fiap.vitallink.services.MedicoService;
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
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
@Tag(name = "Médicos", description = "Gerenciamento de médicos")
public class MedicoController {

    private final MedicoService service;

    @GetMapping
    @Operation(summary = "Listar médicos ativos. Filtro opcional por ?nome=")
    public Page<MedicoResponseDTO> listar(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return service.listar(nome, pageable);
    }

    @GetMapping("/especialidade/{especialidadeId}")
    @Operation(summary = "Listar médicos ativos por especialidade")
    public Page<MedicoResponseDTO> listarPorEspecialidade(
            @PathVariable Long especialidadeId,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return service.listarPorEspecialidade(especialidadeId, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar médico por ID")
    public MedicoResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar novo médico")
    public MedicoResponseDTO criar(@RequestBody @Valid MedicoRequestDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do médico")
    public MedicoResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid MedicoRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Inativar médico (soft delete - preserva histórico de consultas)")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}