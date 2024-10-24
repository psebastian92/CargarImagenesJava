const dropArea = document.getElementById('drop-area');
const fileBtn = document.getElementById('fileBtn');
const fileElem = document.getElementById('fileElem');
const vistaPrevia = document.getElementById('vistaPrevia');
const form = document.querySelector('form'); // Obtener el formulario

// Manejador para arrastrar y soltar archivos
dropArea.addEventListener('dragover', (event) => {
    event.preventDefault(); // Evita que se abra el archivo al soltar
    dropArea.classList.add('highlight'); // Añadir clase para resaltado
});

dropArea.addEventListener('dragleave', () => {
    dropArea.classList.remove('highlight'); // Eliminar clase al salir
});

dropArea.addEventListener('drop', (event) => {
    event.preventDefault();
    dropArea.classList.remove('highlight'); // Eliminar resaltado
    const files = event.dataTransfer.files; // Obtener archivos arrastrados
    handleFiles(files); // Manejar los archivos
});

// Manejador para el botón "Seleccionar imagen"
fileBtn.addEventListener('click', (event) => {
    event.preventDefault(); // Prevenir la acción por defecto del botón
    fileElem.click(); // Abrir el selector de archivos
});

// Manejador para cuando se selecciona un archivo a través del input
fileElem.addEventListener('change', () => {
    const files = fileElem.files; // Obtener archivos seleccionados
    handleFiles(files); // Manejar los archivos seleccionados
});

function handleFiles(files) {
    if (files.length > 0) {
        const archivo = files[0];
        const lector = new FileReader();

        lector.onload = function(e) {
            // Crear la miniatura
            const img = document.createElement('img');
            img.src = e.target.result;
            img.classList.add('miniatura'); // Añadir clase para los estilos de miniatura
            vistaPrevia.innerHTML = ''; // Limpiar cualquier imagen previa
            vistaPrevia.appendChild(img); // Añadir la nueva miniatura
        };

        lector.readAsDataURL(archivo); // Leer el archivo como URL de datos

        // Asignar el archivo al input en caso de que haya sido arrastrado
        if (!fileElem.files.length) {
            const dataTransfer = new DataTransfer();
            dataTransfer.items.add(archivo);
            fileElem.files = dataTransfer.files;
        }
    }
}

// Validar antes de enviar el formulario
form.addEventListener('submit', (event) => {
    if (fileElem.files.length === 0 || !fileElem.files[0].type.startsWith('image/')) {
        event.preventDefault(); // Prevenir el envío del formulario
        alert('Por favor, debés cargar una imagen.'); // Mensaje de error
    }
});
