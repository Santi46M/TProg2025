// ============================
// Usuarios de ejemplo
// ============================
const defaultUsers = [
  {
    nick: "JuanPerez",
    email: "JPerez@eventos.uy",
    pass: "1234",
    rol: "asistente",
    nombre: "Juan",
    apellido: "Pérez",
    nac: "1990-01-01",
    instId: "inst1"
  },
  {
    nick: "INCO",
    email: "inco@eventos.uy",
    pass: "abcd",
    rol: "organizador",
    nombre: "Instituto de Computacion",
    desc: "Instituto mas duro de la Fing",
    web: "https://organizador1.com"
  }
];

// ============================
// Eventos y ediciones de ejemplo
// ============================
const defaultEventos = [
  {
    id: "ev1", nombre: "JSConf",
    ediciones: [
      {
        id:"ed1", nombre:"JSConf 2025", estado:"Aprobada",
        ciudad:"Montevideo", pais:"Uruguay", organizador:"Ana Pérez",
        tiposRegistros:["General","VIP"],
        patrocinios:["Platino — ACME", "Oro — DevCorp"],
        imagen:"https://picsum.photos/seed/jsconf/640/360"
      },
      {
        id:"ed2", nombre:"JSConf 2026", estado:"Ingresada",
        ciudad:"Colonia", pais:"Uruguay", organizador:"Ana Pérez",
        tiposRegistros:["General"], patrocinios:[]
      }
    ]
  },
  {
    id: "ev2", nombre: "UX Day",
    ediciones: [
      {
        id:"ed3", nombre:"UX Day 2025", estado:"Aprobada",
        ciudad:"Buenos Aires", pais:"Argentina", organizador:"Beto García",
        tiposRegistros:["General","Estudiante"],
        patrocinios:["Plata — Pixel SA"],
        imagen:"https://picsum.photos/seed/uxday/640/360"
      }
    ]
  },
  {
    id: "ev3", nombre: "Data Summit",
    ediciones: [
      {
        id:"ed4", nombre:"Data Summit 2024", estado:"Aprobada",
        ciudad:"Madrid", pais:"España", organizador:"Carla López",
        tiposRegistros:["General","Premium"],
        patrocinios:["Oro — BigData Corp", "Plata — CloudX"],
        imagen:"https://picsum.photos/seed/datasummit/640/360"
      }
    ]
  }
];

// ============================
// Guardar en localStorage solo si no existe
// ============================

// Usuarios
if(!localStorage.getItem("usuarios")){
  localStorage.setItem("usuarios", JSON.stringify(defaultUsers));
}

// Eventos
if(!localStorage.getItem("eventos")){
  localStorage.setItem("eventos", JSON.stringify(defaultEventos));
}
