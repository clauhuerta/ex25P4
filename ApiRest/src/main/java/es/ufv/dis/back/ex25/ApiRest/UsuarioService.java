package es.ufv.dis.back.ex25.ApiRest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Element;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

@Service
public class UsuarioService {

    private static final String FILE_PATH = "src/main/resources/usuarios.json";
    private final Gson gson = new Gson();

    public List<Usuario> getAllUsuarios() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Usuario>>(){}.getType();
            List<Usuario> usuarios = gson.fromJson(reader, listType);
            return usuarios != null ? usuarios : new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Optional<Usuario> getUsuarioById(String id) {
        return getAllUsuarios().stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public void addUsuario(Usuario usuario) {
        List<Usuario> usuarios = getAllUsuarios();
        usuarios.add(usuario);
        writeToFile(usuarios);
    }

    public void updateUsuario(String id, Usuario usuario) {
        List<Usuario> usuarios = getAllUsuarios();
        usuarios.replaceAll(u -> u.getId().equals(id) ? usuario : u);
        writeToFile(usuarios);
    }

    private void writeToFile(List<Usuario> usuarios) {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(usuarios, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generatePdf() throws Exception {
        List<Usuario> usuarios = getAllUsuarios();
        Document doc = new Document(PageSize.A4, 50, 50, 100, 72);
        PdfWriter.getInstance(doc, new FileOutputStream("info.pdf"));
        doc.open();
        for (Usuario u : usuarios) {
            doc.add(new Paragraph("ID: " + u.getId()));
            doc.add(new Paragraph("Nombre: " + u.getNombre() + " " + u.getApellidos()));
            doc.add(new Paragraph("NIF: " + u.getNif()));
            doc.add(new Paragraph("Email: " + u.getEmail()));
            doc.add(new Paragraph("Dirección: " + u.getDireccion().getCalle() + ", " +
                    u.getDireccion().getNumero() + ", " +
                    u.getDireccion().getPisoLetra() + ", " +
                    u.getDireccion().getCodigoPostal() + ", " +
                    u.getDireccion().getCiudad()));
            doc.add(new Paragraph("Método de pago: " + u.getMetodoPago().getNumeroTarjeta() +
                    ", " + u.getMetodoPago().getNombreAsociado()));
            doc.add(new Paragraph("-------------------------------"));
        }
        doc.close();
    }
}
