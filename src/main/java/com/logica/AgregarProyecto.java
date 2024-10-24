package com.logica;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet("/AgregarProyecto")
@MultipartConfig
public class AgregarProyecto extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Obtener parámetros del formulario
		String titulo = request.getParameter("titulo");
		String descripcion = request.getParameter("descripcion");
		Part filePart = request.getPart("imagen");

		// Validar parámetros
		if (descripcion == null || descripcion.trim().isEmpty() || filePart == null || filePart.getSize() == 0) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Todos los campos son obligatorios.");
			return;
		}

		// Definir la ruta de la imagen en el directorio de la aplicación
		String rutaRelativa = "img/" + filePart.getSubmittedFileName();
		String rutaImagen = getServletContext().getRealPath("/") + rutaRelativa; // Ruta absoluta en el servidor

		// Guardar la imagen en el servidor
		try (InputStream fileContent = filePart.getInputStream();
				FileOutputStream fos = new FileOutputStream(new File(rutaImagen))) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = fileContent.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead);
			}
		}

		// Insertar datos en la base de datos
		try (Connection conexion = ConexionBD.obtenerConexion()) {
			String sql = "INSERT INTO imagenes (ruta_imagen, titulo,descripcion) VALUES (?, ? ,?)";
			try (PreparedStatement statement = conexion.prepareStatement(sql)) {
				statement.setString(1, rutaRelativa); // Guardar la ruta relativa de la imagen
				statement.setString(3, titulo);
				statement.setString(2, descripcion);
				int filasAfectadas = statement.executeUpdate();
				if (filasAfectadas > 0) {
					request.setAttribute("mensajeExito", "¡Imagen y descripción guardadas correctamente!");
					request.getRequestDispatcher("/LeerDatos").forward(request, response);
				} else {
					response.sendRedirect(request.getContextPath() + "/vistas/error.jsp");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
