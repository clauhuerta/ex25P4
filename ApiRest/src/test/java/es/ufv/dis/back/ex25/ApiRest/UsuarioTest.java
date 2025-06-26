package es.ufv.dis.back.ex25.ApiRest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testConstructorYGetters() {
        Direccion d = new Direccion("Calle", 10, "12345", "3C", "Toledo");
        MetodoPago m = new MetodoPago(1111222233334444L, "Pedro Sánchez");
        Usuario u = new Usuario("id123", "Pedro", "Sánchez", "12345678A", d, "pedro@email.com", m);

        assertEquals("id123", u.getId());
        assertEquals("Pedro", u.getNombre());
        assertEquals("Sánchez", u.getApellidos());
        assertEquals("12345678A", u.getNif());
        assertEquals(d, u.getDireccion());
        assertEquals("pedro@email.com", u.getEmail());
        assertEquals(m, u.getMetodoPago());
    }

    @Test
    void testSetters() {
        Usuario u = new Usuario();
        Direccion d = new Direccion("Paseo", 5, "54321", "1A", "Sevilla");
        MetodoPago m = new MetodoPago(4444333322221111L, "Laura Gómez");

        u.setId("abc");
        u.setNombre("Laura");
        u.setApellidos("Gómez");
        u.setNif("87654321B");
        u.setDireccion(d);
        u.setEmail("laura@email.com");
        u.setMetodoPago(m);

        assertEquals("abc", u.getId());
        assertEquals("Laura", u.getNombre());
        assertEquals("Gómez", u.getApellidos());
        assertEquals("87654321B", u.getNif());
        assertEquals(d, u.getDireccion());
        assertEquals("laura@email.com", u.getEmail());
        assertEquals(m, u.getMetodoPago());
    }
}
