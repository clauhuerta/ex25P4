package es.ufv.dis.back.ex25.ApiRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable String id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> addUsuario(@RequestBody Usuario usuario) {
        usuario.setId(UUID.randomUUID().toString());
        usuarioService.addUsuario(usuario);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable String id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        usuarioService.updateUsuario(id, usuario);
        return ResponseEntity.ok(usuario);
    }

    // Endpoint para generar el PDF
    @GetMapping("/pdf")
    public ResponseEntity<String> generatePdf() {
        try {
            usuarioService.generatePdf();
            return ResponseEntity.ok("PDF generado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error generando PDF: " + e.getMessage());
        }
    }
}
