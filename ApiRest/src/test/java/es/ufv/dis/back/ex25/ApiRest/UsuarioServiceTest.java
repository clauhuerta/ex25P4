package es.ufv.dis.back.ex25.ApiRest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

// Esto es solo un ejemplo. Idealmente mockearías la lectura/escritura de archivos para tests "puros".
class UsuarioServiceTest {

    private UsuarioService service;

    @BeforeEach
    void setUp() {
        service = new UsuarioService();
    }

    @Test
    void testAddAndGetUsuario() {
        Usuario u = new Usuario("testId", "Test", "User", "00000000T",
                new Direccion("Prueba", 1, "10000", "A", "Ciudad"),
                "test@user.com",
                new MetodoPago(1234567890L, "Test User"));
        service.addUsuario(u);

        Optional<Usuario> found = service.getUsuarioById("testId");
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getNombre());
    }

    // Puedes añadir más tests para updateUsuario, getAllUsuarios, etc.
}
