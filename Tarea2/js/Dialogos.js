// Utilidad para mostrar un modal de éxito reutilizable en todos los formularios
function mostrarDialogoExito(texto) {
  const modal = document.createElement('div');
  modal.style.position = 'fixed';
  modal.style.top = '0';
  modal.style.left = '0';
  modal.style.width = '100vw';
  modal.style.height = '100vh';
  modal.style.background = 'rgba(0,0,0,0.25)';
  modal.style.display = 'flex';
  modal.style.alignItems = 'center';
  modal.style.justifyContent = 'center';
  modal.style.zIndex = '9999';

  const box = document.createElement('div');
  box.style.background = '#fff';
  box.style.padding = '2rem';
  box.style.borderRadius = '1rem';
  box.style.boxShadow = '0 2px 12px rgba(0,0,0,0.15)';
  box.style.textAlign = 'center';
  box.innerHTML = `<h3>${texto}</h3><button id='btnAceptarModal' class='btn' style='margin-top:1.5rem;'>Aceptar</button>`;

  modal.appendChild(box);
  document.body.appendChild(modal);
  document.getElementById('btnAceptarModal').onclick = function() {
    window.location.href = 'index.html';
  };
}
