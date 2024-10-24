package com.logica;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/EditarDatos")
public class EditarProyecto extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String idStr = request.getParameter("id");

		// Validar que se haya pasado el id
		if (idStr == null || idStr.isEmpty()) {
			response.sendRedirect(request.getContextPath() + "/vistas/error.jsp?error=missing_id");
			return;
		}

		int id = Integer.parseInt(idStr);
		try (Connection conexion = ConexionBD.obtenerConexion()) {
			String sql = "SELECT nombre, apellido, edad FROM datospersona WHERE id = ?";
			try (PreparedStatement statement = conexion.prepareStatement(sql)) {
				statement.setInt(1, id);
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						String nombre = resultSet.getString("nombre");
						String apellido = resultSet.getString("apellido");
						int edad = resultSet.getInt("edad");

						// Pasar los datos como atributos a la página JSP
						request.setAttribute("id", id);
						request.setAttribute("nombre", nombre);
						request.setAttribute("apellido", apellido);
						request.setAttribute("edad", edad);

						request.getRequestDispatcher("vistas/PaginaEditarDatos.jsp").forward(request, response);
					} else {
						response.sendRedirect(request.getContextPath() + "/vistas/error.jsp?error=no_such_record");
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/vistas/error.jsp?error=database_error");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Obtener parámetros del formulario
		String idStr = request.getParameter("idDatos");
		String nombre = request.getParameter("nombreDatos"); // Mismo nombre que en el label de "PaginaCrearDatos":
																// nombreDatos
		String apellido = request.getParameter("apellidoDatos"); // Mismo nombre que en el label de "PaginaCrearDatos":
																	// apellidoDatos
		String edadStr = request.getParameter("edadDatos"); // Mismo nombre que en el label de "PaginaCrearDatos":

		if (idStr == null || idStr.isEmpty()) {

			return;
		}
		// edadDatos
		// Validar parámetros
		if (nombre == null || nombre.trim().isEmpty() || apellido == null || apellido.trim().isEmpty()
				|| edadStr == null || edadStr.trim().isEmpty()) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Todos los campos son obligatorios.");
			return;
		}

		// Convertir edad a un entero, ya que del formulario se recibio como String.
		int edad;
		try {
			edad = Integer.parseInt(edadStr);
		} catch (NumberFormatException e) {
			request.setAttribute("errorEdad", "La edad debe ser un número entero.");
			request.setAttribute("nombre", nombre);
			request.setAttribute("apellido", apellido);
			request.setAttribute("edad", edadStr);
			request.setAttribute("id", idStr);
			request.getRequestDispatcher("/vistas/PaginaEditarDatos.jsp").forward(request, response);
			return;
		}

		int id;
		try {
			id = Integer.parseInt(idStr);
		} catch (NumberFormatException e) {
			response.sendRedirect(request.getContextPath() + "/vistas/error.jsp?error=id");
			// response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error al obtener
			// ID.");
			return;
		}

		// Actualizar datos en la base de datos
		try (Connection conexion = ConexionBD.obtenerConexion()) {
			String sql = "UPDATE datospersona SET nombre = ?, apellido = ?, edad = ? WHERE id = ?";
			try (PreparedStatement statement = conexion.prepareStatement(sql)) {
				statement.setString(1, nombre);
				statement.setString(2, apellido);
				statement.setInt(3, edad);
				statement.setInt(4, id);
				int filasAfectadas = statement.executeUpdate();
				if (filasAfectadas > 0) {
					request.getRequestDispatcher("/LeerDatos").forward(request, response); // Redirige de
					// éxito
				} else {
					response.sendRedirect(request.getContextPath() + "/LeerDatos"); // Redirige a una página de
																							// error
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();

		}

	}
}
