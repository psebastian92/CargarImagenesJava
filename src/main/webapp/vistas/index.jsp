<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <link rel="stylesheet" type="text/css"
        href="${pageContext.request.contextPath}/css/estilos-index.css">
    <title>Proyectos Secundario Diurno</title>
</head>
<body>
    <div class="contenedor-titulo">
        <h1 class="titulo">Proyectos Secundario Diurno</h1>
    </div>

    <div>
        <a class="boton-cargar" href="${pageContext.request.contextPath}/vistas/paginaParaCargar.jsp">Añadir proyecto</a>
    </div>

    <div class="galeria">
        <% 
            ResultSet conjuntoResultados = (ResultSet) request.getAttribute("conjuntoResultados");
            while (conjuntoResultados.next()) {
                String rutaImagen = conjuntoResultados.getString("ruta_imagen");
                String descripcion = conjuntoResultados.getString("descripcion");
        %>
                <div class="item">
                    <img src="<%= rutaImagen %>" alt="Imagen de proyecto">
                    <div class="descripcion">
                        <p><%= descripcion %></p>
                    </div>
                </div>
        <% 
            } 
        %>
    </div>
</body>
</html>
