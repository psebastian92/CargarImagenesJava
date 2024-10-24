<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Carga de Imágenes</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/estilos-cargar.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script src="${pageContext.request.contextPath}/js/cargar-imagen.js" defer></script>
</head>
<body>
    <div class="contenedor-titulo">
        <h1 class="titulo">Carga de proyectos</h1>
    </div>

    <form action="${pageContext.request.contextPath}/AgregarProyecto" method="post" enctype="multipart/form-data">
        <div class="contenedor-carga-proyectos">
            <div class="drop-area" id="drop-area">
                <h2>Arrastra y suelta una imagen aquí</h2>
                <p>O</p>
                <button type="button" class="boton-cargar" id="fileBtn">Seleccionar imagen</button>
                <input type="file" id="fileElem" name="imagen" accept="image/*" style="display: none;">
                <div class="vista-previa" id="vistaPrevia"></div>
                <!-- Contenedor para la miniatura -->
            </div>

            <div class="descripcion-area">
                <div>
                    <label for="titulo-etiqueta" class="form-label">Titulo del proyecto:</label>
                    <input type="text" id="titulo" name="titulo" required>
                </div>
                <label for="descripcion">Descripción:</label>
                <textarea id="descripcion" name="descripcion" rows="15" cols="100" required></textarea>
                <!-- Campo de texto para la descripción -->
            </div>
        </div>
        <div class="contenedor-subir">
            <button type="submit" class="boton-subir">Subir Proyecto</button>
            <!-- Botón para subir -->
        </div>
    </form>

    <div class="contenedor-boton-regresar">
        <a href="${pageContext.request.contextPath}/LeerDatos" class="boton-regresar">
            <i class="fas fa-undo"></i> Regresar
        </a>
    </div>
</body>
</html>
