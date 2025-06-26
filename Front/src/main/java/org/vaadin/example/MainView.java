package org.vaadin.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Route("")
public class MainView extends VerticalLayout {
    private final Grid<Usuario> grid = new Grid<>(Usuario.class, false);
    private final Gson gson = new Gson();

    private final String API_URL = "http://localhost:8083/api/usuarios";

    public MainView() {
        setSizeFull();

        // GRID - Configura columnas
        grid.addColumn(Usuario::getNombre).setHeader("Nombre");
        grid.addColumn(Usuario::getApellidos).setHeader("Apellidos");
        grid.addColumn(Usuario::getNif).setHeader("NIF");
        grid.addColumn(Usuario::getEmail).setHeader("Email");
        grid.addComponentColumn(usuario -> {
            Button btn = new Button("Editar", click -> openEditDialog(usuario));
            return btn;
        }).setHeader("Editar");

        grid.setItems(fetchUsuarios());

        // Doble click muestra diálogo con todos los datos
        grid.addItemDoubleClickListener(event -> openDetailDialog(event.getItem()));

        add(grid);

        // BOTÓN AÑADIR USUARIO
        Button addBtn = new Button("Añadir usuario", event -> openAddDialog());
        // BOTÓN GENERAR PDF
        Button pdfBtn = new Button("Generar PDF", event -> {
            try {
                generatePdf();
                Notification.show("PDF generado correctamente");
            } catch (Exception e) {
                Notification.show("Error generando PDF: " + e.getMessage());
            }
        });
        add(addBtn, pdfBtn);
    }

    // Fetch de usuarios del backend
    private List<Usuario> fetchUsuarios() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            try (Reader reader = new InputStreamReader(con.getInputStream())) {
                Type listType = new TypeToken<List<Usuario>>(){}.getType();
                List<Usuario> usuarios = gson.fromJson(reader, listType);
                return usuarios != null ? usuarios : new ArrayList<>();
            }
        } catch (Exception e) {
            Notification.show("Error obteniendo usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Diálogo de detalles
    private void openDetailDialog(Usuario usuario) {
        Dialog dialog = new Dialog();
        VerticalLayout layout = new VerticalLayout(
                new Span("ID: " + usuario.getId()),
                new Span("Nombre: " + usuario.getNombre()),
                new Span("Apellidos: " + usuario.getApellidos()),
                new Span("NIF: " + usuario.getNif()),
                new Span("Email: " + usuario.getEmail()),
                new Span("Dirección: " +
                        usuario.getDireccion().getCalle() + ", " +
                        usuario.getDireccion().getNumero() + ", " +
                        usuario.getDireccion().getPisoLetra() + ", " +
                        usuario.getDireccion().getCodigoPostal() + ", " +
                        usuario.getDireccion().getCiudad()),
                new Span("Método de pago: " +
                        usuario.getMetodoPago().getNumeroTarjeta() + ", " +
                        usuario.getMetodoPago().getNombreAsociado())
        );
        dialog.add(layout);
        dialog.setWidth("400px");
        dialog.open();
    }

    // Diálogo de edición
    private void openEditDialog(Usuario usuario) {
        Dialog dialog = new Dialog();
        FormLayout form = new FormLayout();
        TextField nombre = new TextField("Nombre", usuario.getNombre());
        TextField apellidos = new TextField("Apellidos", usuario.getApellidos());
        TextField nif = new TextField("NIF", usuario.getNif());
        TextField email = new TextField("Email", usuario.getEmail());

        TextField calle = new TextField("Calle", usuario.getDireccion().getCalle());
        TextField numero = new TextField("Número", String.valueOf(usuario.getDireccion().getNumero()));
        TextField pisoLetra = new TextField("Piso/Letra", usuario.getDireccion().getPisoLetra());
        TextField codigoPostal = new TextField("Código Postal", usuario.getDireccion().getCodigoPostal());
        TextField ciudad = new TextField("Ciudad", usuario.getDireccion().getCiudad());

        TextField tarjeta = new TextField("Nº Tarjeta", String.valueOf(usuario.getMetodoPago().getNumeroTarjeta()));
        TextField nombreAsociado = new TextField("Nombre Asociado", usuario.getMetodoPago().getNombreAsociado());

        Button saveBtn = new Button("Guardar", ev -> {
            usuario.setNombre(nombre.getValue());
            usuario.setApellidos(apellidos.getValue());
            usuario.setNif(nif.getValue());
            usuario.setEmail(email.getValue());
            usuario.setDireccion(new Direccion(
                    calle.getValue(),
                    Integer.parseInt(numero.getValue()),
                    codigoPostal.getValue(),
                    pisoLetra.getValue(),
                    ciudad.getValue()
            ));
            usuario.setMetodoPago(new MetodoPago(
                    Long.parseLong(tarjeta.getValue()),
                    nombreAsociado.getValue()
            ));

            try {
                updateUsuario(usuario);
                grid.setItems(fetchUsuarios());
                Notification.show("Usuario actualizado");
            } catch (Exception e) {
                Notification.show("Error guardando usuario: " + e.getMessage());
            }
            dialog.close();
        });

        form.add(nombre, apellidos, nif, email, calle, numero, pisoLetra, codigoPostal, ciudad, tarjeta, nombreAsociado, saveBtn);
        dialog.add(form);
        dialog.open();
    }

    // Diálogo para añadir usuario
    private void openAddDialog() {
        Dialog dialog = new Dialog();
        FormLayout form = new FormLayout();
        TextField nombre = new TextField("Nombre");
        TextField apellidos = new TextField("Apellidos");
        TextField nif = new TextField("NIF");
        TextField email = new TextField("Email");

        TextField calle = new TextField("Calle");
        TextField numero = new TextField("Número");
        TextField pisoLetra = new TextField("Piso/Letra");
        TextField codigoPostal = new TextField("Código Postal");
        TextField ciudad = new TextField("Ciudad");

        TextField tarjeta = new TextField("Nº Tarjeta");
        TextField nombreAsociado = new TextField("Nombre Asociado");

        Button saveBtn = new Button("Añadir", ev -> {
            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre.getValue());
            nuevo.setApellidos(apellidos.getValue());
            nuevo.setNif(nif.getValue());
            nuevo.setEmail(email.getValue());
            nuevo.setDireccion(new Direccion(
                    calle.getValue(),
                    Integer.parseInt(numero.getValue()),
                    codigoPostal.getValue(),
                    pisoLetra.getValue(),
                    ciudad.getValue()
            ));
            nuevo.setMetodoPago(new MetodoPago(
                    Long.parseLong(tarjeta.getValue()),
                    nombreAsociado.getValue()
            ));

            try {
                addUsuario(nuevo);
                grid.setItems(fetchUsuarios());
                Notification.show("Usuario añadido");
            } catch (Exception e) {
                Notification.show("Error añadiendo usuario: " + e.getMessage());
            }
            dialog.close();
        });

        form.add(nombre, apellidos, nif, email, calle, numero, pisoLetra, codigoPostal, ciudad, tarjeta, nombreAsociado, saveBtn);
        dialog.add(form);
        dialog.open();
    }

    // PUT para actualizar usuario
    private void updateUsuario(Usuario usuario) throws IOException {
        URL url = new URL(API_URL + "/" + usuario.getId());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        String json = gson.toJson(usuario);
        try (OutputStream os = con.getOutputStream()) {
            os.write(json.getBytes());
        }
        con.getResponseCode(); // Para ejecutar la petición
        con.disconnect();
    }

    // POST para añadir usuario
    private void addUsuario(Usuario usuario) throws IOException {
        URL url = new URL(API_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);
        String json = gson.toJson(usuario);
        try (OutputStream os = con.getOutputStream()) {
            os.write(json.getBytes());
        }
        con.getResponseCode();
        con.disconnect();
    }

    // GET para generar PDF
    private void generatePdf() throws IOException {
        URL url = new URL(API_URL + "/pdf");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.getInputStream().close();
        con.disconnect();
    }
}
