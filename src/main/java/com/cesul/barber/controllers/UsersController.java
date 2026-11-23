package com.cesul.barber.controllers;

import com.cesul.barber.dtos.UserCreationDTO;
import com.cesul.barber.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Gerenciamento de usuários da barbearia")
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    @Operation(summary = "Listar todos", description = "Retorna a lista de todos os usuários cadastrados.")
    public ResponseEntity<List<UserCreationDTO>> getAll() {
        return ResponseEntity.ok(this.usersService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna o utilizador correspondente ao ID.")
    public ResponseEntity<UserCreationDTO> getById(@PathVariable UUID id) {
        return this.usersService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Criar utilizador", description = "Gera um ID automático, valida os dados (nome e e-mail) e salva na lista.")
    public ResponseEntity<UserCreationDTO> create(@RequestBody UserCreationDTO user) {
        UserCreationDTO newUser = this.usersService.create(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar utilizador", description = "Atualiza nome, e-mail e/ou avatar do utilizador.")
    public ResponseEntity<UserCreationDTO> update(@PathVariable UUID id, @RequestBody Map<String, String> request) {
        return this.usersService.update(id, request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/avatar")
    @Operation(
            summary = "Atualizar Avatar",
            description = "Atualiza exclusivamente a imagem de perfil do utilizador. Ignora outros campos."
    )
    public ResponseEntity<UserCreationDTO> patchUser(@PathVariable UUID id, @RequestBody Map<String, String> avatar) {
        return this.usersService.updateAvatar(id, avatar)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar utilizador", description = "Remove o utilizador correspondente ao ID da lista.")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (this.usersService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/clear")
    @Operation(summary = "Limpar utilizadores", description = "Limpa a lista em memória (Útil para reiniciar os testes durante a aula).")
    public ResponseEntity<Void> clearUsers() {
        this.usersService.clear();
        return ResponseEntity.noContent().build();
    }
}
