// ============================
// TProg - Datos de Prueba T2 Parte 1 (front-end)
// Formato compatible con tu CargaUsers.js
// ============================

// ============================
// Usuarios de ejemplo
// ============================
const defaultUsers = [
  {
    // US01 (Asistente)
    nick: "atorres",
    email: "atorres@gmail.com",
    pass: "123.torres",
    rol: "asistente",
    nombre: "Ana",
    apellido: "Torres",
    nac: "1990-05-12",      // 12/05/1990 -> ISO
    instId: "INS01",
    img: "../img/IMG-US01.jpg" 
  },
  {
    // US04 (Organizador)
    nick: "miseventos",
    email: "contacto@miseventos.com",
    pass: "22miseventos",
    rol: "organizador",
    nombre: "MisEventos",
    desc: "Empresa de organización de eventos.",
    web: "https://miseventos.com",
    img: "../img/IMG-US04.jpeg"
  }
];

// ============================
// Catálogo de categorías
// ============================
const defaultCategorias = [
  { id: "CA01", key: "tecnologia", nombre: "Tecnología" },
  { id: "CA02", key: "innovacion", nombre: "Innovación" },
  { id: "CA06", key: "deporte",    nombre: "Deporte" },
  { id: "CA07", key: "salud",      nombre: "Salud" }
];

window.CAT_LABEL = {
  tecnologia: "Tecnología",
  innovacion: "Innovación",
  deporte:    "Deporte",
  salud:      "Salud"
};

// ============================
// Instituciones
// ============================
const defaultInstituciones = [
  {
    id: "INS01",
    nombre: "Facultad de Ingeniería",
    descripcion: "Facultad de Ingeniería de la Universidad de la República",
    web: "https://www.fing.edu.uy"
  }
];

// ============================
// Eventos + Ediciones
// ============================
const defaultEventos = [
  {
    id: "EV01",
    nombre: "Conferencia de Tecnología",
    descripcion: "Evento sobre innovación tecnológica",
    sigla: "CONFTEC",
    alta: "2025-01-10",
    categorias: ["tecnologia", "innovacion"],
    imagen: null,
    ediciones: [
      {
        id: "EDEV08",
        nombre: "Tecnología Punta del Este 2026",
        sigla: "CONFTECH26",
        estado: "Aceptada",
        ciudad: "Punta del Este",
        pais: "Uruguay",
        inicio: "2026-04-06",
        fin:    "2026-04-10",
        alta:   "2025-08-01",
        organizador: "udelar",
        tiposRegistros: [
          { id: "TR19", nombre: "Estudiante", descripcion: "Acceso para estudiantes", costo: 1000, cupo: 50 }
        ],
        patrocinios: [
          { id:"PAT1", institucion:"INS01", nivel:"Oro", tipo:"TR19", aporte:20000, fecha:"2025-08-21", cantidad:4, codigo:"TECHUDELAR" }
        ],
        imagen: null // ❓ no hay en el zip
      },
      {
        id: "EDEV09",
        nombre: "Mobile World Congress 2025",
        sigla: "MWC",
        estado: "Ingresada",
        ciudad: "Barcelona",
        pais: "España",
        inicio: "2025-12-12",
        fin:    "2025-12-15",
        alta:   "2025-08-21",
        organizador: "techcorp",
        tiposRegistros: [
          { id: "TR20", nombre: "Full", descripcion: "Acceso ilimitado + Cena de gala", costo: 750, cupo: 550 }
        ],
        imagen: "../img/IMG-EDEV09.png" 
      },
      {
        id: "EDEV10",
        nombre: "Web Summit 2026",
        sigla: "WS26",
        estado: "Aceptada",
        ciudad: "Lisboa",
        pais: "Portugal",
        inicio: "2026-01-13",
        fin:    "2026-02-01",
        alta:   "2025-06-04",
        organizador: "techcorp",
        tiposRegistros: [
          { id: "TR25", nombre: "Estudiante", descripcion: "Acceso para estudiantes", costo: 300, cupo: 1 }
        ],
        imagen: null // ❓ no hay en el zip
      }
    ]
  },
  {
    id: "EV04",
    nombre: "Maratón de Montevideo",
    descripcion: "Competencia deportiva anual en la capital",
    sigla: "MARATON",
    alta: "2022-01-01",
    categorias: ["deporte", "salud"],
    imagen: "img/IMG-EV04.png", 
    ediciones: [
      {
        id: "EDEV03",
        nombre: "Maratón de Montevideo 2024",
        sigla: "MARATON24",
        estado: "Aceptada",
        ciudad: "Montevideo",
        pais: "Uruguay",
        inicio: "2024-09-14",
        fin:    "2024-09-14",
        alta:   "2024-04-21",
        organizador: "miseventos",
        tiposRegistros: [
          { id: "TR07", nombre: "Corredor 21K", descripcion: "Inscripción a la media maratón", costo: 500, cupo: 500 }
        ],
        imagen: "../img/IMG-EDEV03.jpeg" 
      },
      {
        id: "EDEV04",
        nombre: "Maratón de Montevideo 2022",
        sigla: "MARATON22",
        estado: "Rechazada",
        ciudad: "Montevideo",
        pais: "Uruguay",
        inicio: "2022-09-14",
        fin:    "2022-09-14",
        alta:   "2022-05-21",
        organizador: "imm",
        tiposRegistros: [],
        imagen: "../img/IMG-EDEV04.jpeg" 
      }
    ]
  }
];

// ============================
// Tipos de Registro (agregado según tabla del usuario)
// ============================
const defaultTiposRegistro = [
  { id: "TR07", edicion: "EDEV03", nombre: "Corredor 21K", descripcion: "Inscripción a la media maratón", costo: 500, cupo: 500 },
  { id: "TR19", edicion: "EDEV08", nombre: "Estudiante", descripcion: "Acceso para estudiantes", costo: 1000, cupo: 50 },
  { id: "TR20", edicion: "EDEV09", nombre: "Full", descripcion: "Acceso ilimitado + Cena de gala", costo: 750, cupo: 550 },
  { id: "TR25", edicion: "EDEV10", nombre: "Estudiante", descripcion: "Acceso para estudiantes", costo: 300, cupo: 1 }
];

// ============================
// Registros (agregado según tabla del usuario)
// ============================
const defaultRegistros = [
  { id: "RE02", usuario: "atorres", edicion: "EDEV03", tipo: "TR07", fecha: "2024-07-30", costo: 500 },
  { id: "RE03", usuario: "atorres", edicion: "EDEV10", tipo: "TR25", fecha: "2025-08-21", costo: 300 }
];

// ============================
// Guardar en localStorage solo si no existe
// ============================
if(!localStorage.getItem("usuarios")){
  localStorage.setItem("usuarios", JSON.stringify(defaultUsers));
}
if(!localStorage.getItem("eventos")){
  localStorage.setItem("eventos", JSON.stringify(defaultEventos));
}
if(!localStorage.getItem("categorias")){
  localStorage.setItem("categorias", JSON.stringify(defaultCategorias));
}
if(!localStorage.getItem("instituciones")){
  localStorage.setItem("instituciones", JSON.stringify(defaultInstituciones));
}
if(!localStorage.getItem("registros")){
  localStorage.setItem("registros", JSON.stringify(defaultRegistros));
}
if(!localStorage.getItem("tiposRegistro")){
  localStorage.setItem("tiposRegistro", JSON.stringify(defaultTiposRegistro));
}



// ============================
// Compatibilidad para navbar por categorías
// ============================
window.EVENTS = defaultEventos.map(ev => ({
  id: ev.id,
  nombre: ev.nombre,
  categorias: ev.categorias || [],
  cover: ev.imagen || (ev.ediciones?.find(e => !!e.imagen)?.imagen || "")
}));