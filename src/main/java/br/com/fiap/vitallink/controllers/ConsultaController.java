package br.com.fiap.vitallink.controllers;

import br.com.fiap.vitallink.dto.ConsultaRequestDTO;
import br.com.fiap.vitallink.dto.ConsultaResponseDTO;
import br.com.fiap.vitallink.enums.StatusConsulta;
import br.com.fiap.vitallink.services.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/consultas")
@RequiredArgsConstructor
@Tag(name = "Consultas", description = "Gerenciamento de consultas médicas")
public class ConsultaController {

    private final ConsultaService service;

    @GetMapping
    @Operation(summary = "Listar consultas. Filtros opcionais: ?status= | ?medicoId= | ?pacienteId=")
    public Page<ConsultaResponseDTO> listar(
            @RequestParam(required = false) StatusConsulta status,
            @RequestParam(required = false) Long medicoId,
            @RequestParam(required = false) Long pacienteId,
            @PageableDefault(size = 10, sort = "dataHora") Pageable pageable) {
        return service.listar(status, medicoId, pacienteId, pageable);
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar consultas por período. Ex: ?inicio=2025-01-01T00:00:00&fim=2025-12-31T23:59:59")
    public Page<ConsultaResponseDTO> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @PageableDefault(size = 10, sort = "dataHora") Pageable pageable) {
        return service.listarPorPeriodo(inicio, fim, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar consulta por ID")
    public ConsultaResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Agendar nova consulta")
    public ConsultaResponseDTO criar(@RequestBody @Valid ConsultaRequestDTO dto) {
        return service.criar(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar consulta (reagendar, alterar status, etc.)")
    public ConsultaResponseDTO atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ConsultaRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Cancelar consulta (muda status para CANCELADA)")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}