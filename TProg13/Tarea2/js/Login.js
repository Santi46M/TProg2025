document.addEventListener("DOMContentLoaded", () => {
  const userNav = document.getElementById("userNav");
  const usuario = JSON.parse(sessionStorage.getItem("usuarioActivo") || "null");

  if (usuario) {
    let html = `
      <span>Hola, ${usuario.nombre}</span>
      <div class="dropdown">
        <button class="dropbtn">
          Mi Perfil <i class='bx bxs-down-arrow'></i>
        </button>
        <div class="dropdown-content">
          <a href="ConsultaUsuario.html?usuario=${usuario.nick}">
            <i class='bx bxs-id-card'></i> Ver Datos
          </a>
          <a href="ModificarUsuario.html?usuario=${usuario.nick}">
            <i class='bx bxs-edit'></i> Modificar Datos
          </a>
          <a href="CierreSesion.html">
            <i class='bx bxs-log-out'></i> Cerrar sesión
          </a>
        </div>
      </div>
    `;
    userNav.innerHTML = html;
  }
});