document.addEventListener("DOMContentLoaded", () => {
  const userNav = document.getElementById("userNav");
  const usuario = JSON.parse(sessionStorage.getItem("usuarioActivo"));

  if (usuario) {
    // Reemplaza contenido por info del usuario logueado
    let html = `
      <span>Hola, ${usuario.nombre}</span>
      <a class="btn" { href: ConsultaUsuario.html?usuario=${usuario.nick}}, class: 'link-reg' }"><i class='bx bxs-log-out'></i> Mi Perfil</a>
      <a class="btn" href="CierreSesion.html"><i class='bx bxs-log-out'></i> Cerrar sesión</a>
    `;

    userNav.innerHTML = html;
  }
});