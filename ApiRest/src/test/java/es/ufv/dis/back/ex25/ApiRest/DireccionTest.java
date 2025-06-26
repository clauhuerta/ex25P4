package es.ufv.dis.back.ex25.ApiRest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DireccionTest {

    @Test
    void testConstructorYGetters() {
        Direccion d = new Direccion("Calle Falsa", 123, "28080", "2B", "Madrid");
        assertEquals("Calle Falsa", d.getCalle());
        assertEquals(123, d.getNumero());
        assertEquals("28080", d.getCodigoPostal());
        assertEquals("2B", d.getPisoLetra());
        assertEquals("Madrid", d.getCiudad());
    }

    @Test
    void testSetters() {
        Direccion d = new Direccion();
        d.setCalle("Gran Via");
        d.setNumero(55);
        d.setCodigoPostal("28013");
        d.setPisoLetra("1A");
        d.setCiudad("Madrid");

        assertEquals("Gran Via", d.getCalle());
        assertEquals(55, d.getNumero());
        assertEquals("28013", d.getCodigoPostal());
        assertEquals("1A", d.getPisoLetra());
        assertEquals("Madrid", d.getCiudad());
    }
}
