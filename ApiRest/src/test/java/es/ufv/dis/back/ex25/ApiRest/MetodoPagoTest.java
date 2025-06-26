package es.ufv.dis.back.ex25.ApiRest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MetodoPagoTest {

    @Test
    void testConstructorYGetters() {
        MetodoPago m = new MetodoPago(1234567890123456L, "Juan Perez");
        assertEquals(1234567890123456L, m.getNumeroTarjeta());
        assertEquals("Juan Perez", m.getNombreAsociado());
    }

    @Test
    void testSetters() {
        MetodoPago m = new MetodoPago();
        m.setNumeroTarjeta(9876543210123456L);
        m.setNombreAsociado("Ana Lopez");
        assertEquals(9876543210123456L, m.getNumeroTarjeta());
        assertEquals("Ana Lopez", m.getNombreAsociado());
    }
}
