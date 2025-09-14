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

// Guardar en localStorage solo si no existe
if(!localStorage.getItem("usuarios")){
  localStorage.setItem("usuarios", JSON.stringify(defaultUsers));
}