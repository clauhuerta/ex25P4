package es.ufv.dis.back.ex25.ApiRest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void getAllUsuarios_returnsOkAndJson() throws Exception {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("id1", "Ana", "Ruiz", "00000001A",
                new Direccion("Calle 1", 1, "10001", "A", "Madrid"),
                "ana@correo.com",
                new MetodoPago(1111222233334444L, "Ana Ruiz")
        ));
        given(usuarioService.getAllUsuarios()).willReturn(usuarios);

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].nombre").value("Ana"));
    }

    @Test
    void addUsuario_returnsOkAndCreatedUser() throws Exception {
        Usuario usuario = new Usuario("id2", "Juan", "Pérez", "00000002B",
                new Direccion("Calle 2", 2, "20002", "B", "Sevilla"),
                "juan@correo.com",
                new MetodoPago(2222333344445555L, "Juan Pérez")
        );
        // Simula que el usuario es añadido correctamente
        Mockito.doNothing().when(usuarioService).addUsuario(any(Usuario.class));

        String nuevoUsuarioJson = """
        {
          "nombre": "Juan",
          "apellidos": "Pérez",
          "nif": "00000002B",
          "direccion": {
            "calle": "Calle 2",
            "numero": 2,
            "codigoPostal": "20002",
            "pisoLetra": "B",
            "ciudad": "Sevilla"
          },
          "email": "juan@correo.com",
          "metodoPago": {
            "numeroTarjeta": 2222333344445555,
            "nombreAsociado": "Juan Pérez"
          }
        }
        """;

        mockMvc.perform(post("/api/usuarios")
                        .contentType("application/json")
                        .content(nuevoUsuarioJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void getUsuarioById_notFound() throws Exception {
        given(usuarioService.getUsuarioById("no_existe")).willReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/no_existe"))
                .andExpect(status().isNotFound());
    }
}
