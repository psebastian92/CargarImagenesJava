package com.logica;

// Importación de clases necesarias para manejar errores y conexiones a la base de datos
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Importación de clases para crear un servlet
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Anotación que define la URL del servlet
@WebServlet("/EliminarDatos")
public class EliminarProyecto extends HttpServlet {
    // Método que se ejecuta cuando se recibe una solicitud POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Se obtienen los IDs seleccionados desde el formulario
        String[] idsSeleccionados = request.getParameterValues("idsSeleccionados");
        // Se obtiene un ID único en caso de que se elimine solo uno
        String idUnico = request.getParameter("idUnico");

        // Verifica si se han seleccionado IDs para eliminar
        if (idsSeleccionados != null && idsSeleccionados.length > 0) {
            // Conectar a la base de datos
            try (Connection conexion = ConexionBD.obtenerConexion()) {
                // SQL para eliminar un registro por ID
                String sql = "DELETE FROM datospersona WHERE id = ?";
                // Preparar la declaración SQL
                try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                    // Recorrer todos los IDs seleccionados
                    for (String idStr : idsSeleccionados) {
                        // Convertir el ID a un número entero
                        int id = Integer.parseInt(idStr);
                        // Establecer el valor del ID en la consulta SQL
                        statement.setInt(1, id);
                        // Agregar la consulta a un lote (batch)
                        statement.addBatch(); // Agregar a batch
                    }
                    // Ejecutar todas las consultas en el batch
                    int[] filasAfectadas = statement.executeBatch(); // Ejecutar batch
                    boolean algunaEliminacionExitosa = false;
                    // Verificar si al menos una fila fue eliminada
                    for (int count : filasAfectadas) {
                        if (count > 0) {
                            algunaEliminacionExitosa = true; // Al menos una eliminación fue exitosa
                            break; // Salir del bucle si hay éxito
                        }
                    }
                    // Redirigir según si hubo eliminaciones exitosas o no
                    if (algunaEliminacionExitosa) {
                        response.sendRedirect(request.getContextPath() + "/LeerDatos"); // Redirigir a la lista de datos
                    } else {
                        response.sendRedirect(request.getContextPath() + "/vistas/error.jsp"); // Redirigir a una página de error
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Imprimir el error en la consola
                response.sendRedirect(request.getContextPath() + "/vistas/error.jsp"); // Redirigir a una página de error
            }
        } else {
            // Si no hay IDs seleccionados, verifica si se proporcionó un ID único
            if (idUnico != null && !idUnico.trim().isEmpty()) {
                // Conectar a la base de datos
                try (Connection conexion = ConexionBD.obtenerConexion()) {
                    // SQL para eliminar un registro por ID
                    String sql = "DELETE FROM datospersona WHERE id = ?";
                    // Preparar la declaración SQL
                    try (PreparedStatement statement = conexion.prepareStatement(sql)) {
                        // Convertir el ID único a un número entero
                        int id = Integer.parseInt(idUnico);
                        // Establecer el valor del ID en la consulta SQL
                        statement.setInt(1, id);
                        // Ejecutar la consulta de eliminación
                        int filasAfectadas = statement.executeUpdate();
                        // Verificar si la eliminación fue exitosa
                        if (filasAfectadas > 0) {
                            // Redirigir a la lista de datos después de eliminar
                            response.sendRedirect(request.getContextPath() + "/LeerDatos");
                        } else {
                            response.sendRedirect(request.getContextPath() + "/vistas/error.jsp"); // Redirigir a una página de error
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace(); // Imprimir el error en la consola
                    response.sendRedirect(request.getContextPath() + "/vistas/error.jsp"); // Redirigir a una página de error
                }
            }
        }
    }
}
