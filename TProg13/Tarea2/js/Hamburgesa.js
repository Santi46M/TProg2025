function toggleMenu() {
	  const sidebar = document.getElementById("sidebar");
	  sidebar.classList.toggle("active");
	}
	
	
	document.addEventListener("DOMContentLoaded", () => {
	  const usuario = JSON.parse(sessionStorage.getItem("usuarioActivo"));
	  const menuList = document.getElementById("menuList");

	  if (!usuario) {
	    // 🔹 Si NO está logueado → menú básico
	    menuList.innerHTML = `
	      <li><a href="ConsultaEvento.html"><i class='bx bx-search'></i> Consultar Evento</a></li>
	      <li><a href="ConsultaEdicionEvento.html"><i class='bx bx-calendar'></i> Consultar Edición</a></li>
	      <li><a href="ConsultaTipoRegistro.html"><i class='bx bx-list-ul'></i> Tipos de Registro</a></li>
		  <li><a href="ConsultaUsuario.html"><i class='bx bx-list-ul'></i> Consultar Usuario</a></li>
	    `;
	  } else if (usuario.rol === "organizador") {
	    // 🔹 Menú para ORGANIZADOR
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
	    // 🔹 Menú para ASISTENTE
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
	
	function toggleMenu() {
	  const dropdown = document.getElementById("dropdownMenu");
	  if (dropdown.style.display === "none" || dropdown.style.display === "") {
	    dropdown.style.display = "block";
	    document.addEventListener('click', closeDropdownOnClickOutside);
	  } else {
	    dropdown.style.display = "none";
	    document.removeEventListener('click', closeDropdownOnClickOutside);
	  }
	}