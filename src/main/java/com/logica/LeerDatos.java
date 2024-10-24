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

@WebServlet("/LeerDatos")
public class LeerDatos extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        Connection conexion = null;
        PreparedStatement declaracionPreparada = null;
        ResultSet conjuntoResultados = null;

        try {
            conexion = ConexionBD.obtenerConexion(); // Método para obtener la conexión

            // Consultar todos los datos de la tabla "imagenes"
            String sql = "SELECT id, ruta_imagen, titulo, descripcion, fecha_subida FROM imagenes";
            declaracionPreparada = conexion.prepareStatement(sql);
            conjuntoResultados = declaracionPreparada.executeQuery();
            
            // Enviar los resultados a la JSP
            solicitud.setAttribute("conjuntoResultados", conjuntoResultados);
            solicitud.getRequestDispatcher("vistas/index.jsp").forward(solicitud, respuesta);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar recursos
            try {
                if (conjuntoResultados != null) conjuntoResultados.close();
                if (declaracionPreparada != null) declaracionPreparada.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        doGet(solicitud, respuesta); // Reutilizamos la lógica de doGet
    }
}
