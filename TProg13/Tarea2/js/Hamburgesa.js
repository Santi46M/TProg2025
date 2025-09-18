// === Hamburguesa ===
function toggleMenu() {
	const sidebar = document.getElementById("sidebar");       // páginas normales
	const dropdown = document.getElementById("dropdownMenu"); // index
  
	if (sidebar) {
	  // Sidebar lateral
	  sidebar.classList.toggle("active");
	  return;
	}
  
	if (dropdown) {
	  // Listita desplegable (index)
	  dropdown.style.display = (dropdown.style.display === "block") ? "none" : "block";
	  return;
	}
  }
  
  // === Menú dinámico por rol ===
  document.addEventListener("DOMContentLoaded", () => {
	let usuario = null;
	try { usuario = JSON.parse(sessionStorage.getItem("usuarioActivo") || "null"); } catch {}
  
	const menuList = document.getElementById("menuList");
	if (!menuList) return;
  
	if (!usuario) {
	  menuList.innerHTML = `
		<li><a href="ConsultaEvento.html"><i class='bx bx-search'></i> Consultar Evento</a></li>
		<li><a href="ConsultaEdicionEvento.html"><i class='bx bx-calendar'></i> Consultar Edición</a></li>
		<li><a href="ConsultaTipoRegistro.html"><i class='bx bx-list-ul'></i> Tipos de Registro</a></li>
		<li><a href="ConsultaUsuario.html"><i class='bx bx-list-ul'></i> Consultar Usuario</a></li>
	  `;
	} else if (usuario.rol === "organizador") {
	  menuList.innerHTML = `
		<li><a href="AltaEvento.html"><i class='bx bx-plus-circle'></i> Crear Evento</a></li>
		<li><a href="AltaEdicionEvento.html"><i class='bx bx-calendar-plus'></i> Crear Edición</a></li>
		<li><a href="AltaTipoRegistro.html"><i class='bx bx-detail'></i> Crear Tipo Registro</a></li>
		<li><a href="ConsultaEvento.html"><i class='bx bx-search'></i> Consultar Evento</a></li>
		<li><a href="ConsultaEdicionEvento.html"><i class='bx bx-calendar'></i> Consultar Edición</a></li>
		<li><a href="ConsultaTipoRegistro.html"><i class='bx bx-list-ul'></i> Tipos de Registro</a></li>
		<li><a href="ConsultaUsuario.html"><i class='bx bx-list-ul'></i> Consultar Usuario</a></li>
	  `;
	} else if (usuario.rol === "asistente") {
	  menuList.innerHTML = `
		<li><a href="ConsultaEvento.html"><i class='bx bx-search'></i> Consultar Evento</a></li>
		<li><a href="ConsultaEdicionEvento.html"><i class='bx bx-calendar'></i> Consultar Edición</a></li>
		<li><a href="ConsultaRegistro.html"><i class='bx bx-user-check'></i> Mis Registros</a></li>
		<li><a href="ConsultaTipoRegistro.html"><i class='bx bx-list-ul'></i> Tipos de Registro</a></li>
		<li><a href="RegistroEdicionEvento.html"><i class='bx bx-list-ul'></i> Registrarse a Edicion</a></li>
		<li><a href="ConsultaUsuario.html"><i class='bx bx-list-ul'></i> Consultar Usuario</a></li>
	  `;
	}
  });