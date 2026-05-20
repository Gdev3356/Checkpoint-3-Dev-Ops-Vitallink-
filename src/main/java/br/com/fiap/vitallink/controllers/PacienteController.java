package br.com.fiap.vitallink.controllers;

import br.com.fiap.vitallink.dto.PacienteRequestDTO;
import br.com.fiap.vitallink.dto.PacienteResponseDTO;
import br.com.fiap.vitallink.services.PacienteService;
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
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Gerenciamento de pacientes")
public class PacienteController {

    private final PacienteService service;

    @GetMapping
    @Operation(summary = "Listar pacientes. Filtro opcional por ?nome=")
    public Page<PacienteResponseDTO> listar(
            @RequestParam(required = false) String nome,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return service.listar(nome, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID")
    public PacienteResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar novo paciente")
    public PacienteResponseDTO criar(@RequestBody @Valid PacienteRequestDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do paciente")
    public PacienteResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid PacienteRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar paciente")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}