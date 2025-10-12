package logica;

import excepciones.CostoTipoRegistroInvalidoException;
import excepciones.CupoTipoRegistroInvalidoException;
import excepciones.EdicionYaExisteException;
import excepciones.EventoYaExisteException;
import excepciones.InstitucionYaExisteException;
import excepciones.TipoRegistroYaExisteException;
import excepciones.UsuarioYaExisteException;
import excepciones.ValorPatrocinioExcedidoException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import logica.clases.Ediciones;
import logica.clases.Eventos;
import logica.datatypes.DTCategorias;
import logica.enumerados.DTEstado;
import logica.enumerados.DTNivel;
import logica.manejadores.ManejadorEvento;

/**
 * Carga de datos hardcodeada (estilo original) usando los datos nuevos
 * e incluyendo el ESTADO en las ediciones.
 */
public class CargaDatosPrueba {

	public static void cargar() {
	    try {
	        cargarCategorias();
	        cargarInstitucionesEjemplo();
	        cargarEventosEjemplo();
	        cargarUsuariosEjemplo();
	        cargarEdicionesEjemplo();
	        cargarTipoRegistroEjemplo();
	        cargarRegistrosEjemplo();
	        cargarPatrociniosEjemplo();
	        logResumenDatos();
	    } catch (InstitucionYaExisteException
	           | excepciones.EventoYaExisteException
	           | excepciones.UsuarioYaExisteException
	           | excepciones.TipoRegistroYaExisteException
	           | excepciones.CupoTipoRegistroInvalidoException
	           | excepciones.CostoTipoRegistroInvalidoException
	           | excepciones.ValorPatrocinioExcedidoException | EdicionYaExisteException ex) {
	        throw new IllegalStateException("Error al cargar datos de prueba: " + ex.getMessage(), ex);
	    }
	}


	public static void logResumenDatos() { }
	

    

    // --- Utilidades ---
    private static LocalDate parseFecha(String f) {
        String[] p = f.split("/");
        return LocalDate.of(Integer.parseInt(p[2]), Integer.parseInt(p[1]), Integer.parseInt(p[0]));
    }

    // =========================
    // CATEGORÍAS
    // =========================
    public static void cargarCategorias() {
        var ce = new logica.controladores.ControladorEvento();
        ce.altaCategoria("Tecnología");
        ce.altaCategoria("Innovación");
        ce.altaCategoria("Literatura");
        ce.altaCategoria("Cultura");
        ce.altaCategoria("Música");
        ce.altaCategoria("Deporte");
        ce.altaCategoria("Salud");
        ce.altaCategoria("Entretenimiento");
        ce.altaCategoria("Agro");
        ce.altaCategoria("Negocios");
        ce.altaCategoria("Moda");
        ce.altaCategoria("Investigación");
    }

    // =========================
    // INSTITUCIONES
    // =========================
    public static void cargarInstitucionesEjemplo() throws InstitucionYaExisteException {
        var cu = new logica.controladores.ControladorUsuario();
        cu.altaInstitucion("Facultad de Ingeniería", "Facultad de Ingeniería de la Universidad de la República", "https://www.fing.edu.uy");
        cu.altaInstitucion("ORT Uruguay", "Universidad privada enfocada en tecnología y gestión", "https://ort.edu.uy");
        cu.altaInstitucion("Universidad Católica del Uruguay", "Institución de educación superior privada", "https://ucu.edu.uy");
        cu.altaInstitucion("Antel", "Empresa estatal de telecomunicaciones", "https://antel.com.uy");
        cu.altaInstitucion("Agencia Nacional de Investigación e Innovación (ANII)", "Fomenta la investigación y la innovación en Uruguay", "https://anii.org.uy");
    }

    // =========================
    // EVENTOS
    // =========================
    public static void cargarEventosEjemplo() throws EventoYaExisteException {
        var ce = new logica.controladores.ControladorEvento();

        List<String> catEv01 = Arrays.asList("Tecnología", "Innovación");
        List<String> catEv02 = Arrays.asList("Literatura", "Cultura");
        List<String> catEv03 = Arrays.asList("Música");
        List<String> catEv04 = Arrays.asList("Deporte", "Salud");
        List<String> catEv05 = Arrays.asList("Entretenimiento");
        List<String> catEv06 = Arrays.asList("Agro", "Negocios");
        List<String> catEv07 = Arrays.asList("Moda", "Investigación");

        // Ref 2025 — Conferencia de Tecnología
        ce.altaEvento(
            "Conferencia de Tecnología",
            "Evento sobre innovación tecnológica",
            LocalDate.of(2025, 1, 10),
            "CONFTEC",
            new DTCategorias(catEv01),
            null
        );

        // Ref 2024 — Feria del Libro
        ce.altaEvento(
            "Feria del Libro",
            "Encuentro anual de literatura",
            LocalDate.of(2025, 2, 1),
            "FERLIB",
            new DTCategorias(catEv02),
            "IMG-EV02.jpeg"
        );

        // Ref 2023 — Montevideo Rock
        ce.altaEvento(
            "Montevideo Rock",
            "Festival de rock con artistas nacionales e internacionales",
            LocalDate.of(2023, 3, 15),
            "MONROCK",
            new DTCategorias(catEv03),
            "IMG-EV03.jpeg"
        );

        // Ref 2022 — Maratón de Montevideo
        ce.altaEvento(
            "Maratón de Montevideo",
            "Competencia deportiva anual en la capital",
            LocalDate.of(2022, 1, 1),
            "MARATON",
            new DTCategorias(catEv04),
            "IMG-EV04.png"
        );

        // Ref 2024b — Montevideo Comics
        ce.altaEvento(
            "Montevideo Comics",
            "Convención de historietas, cine y cultura geek",
            LocalDate.of(2024, 4, 10),
            "COMICS",
            new DTCategorias(catEv05),
            "IMG-EV05.png"
        );

        // Ref 2024c — Expointer Uruguay
        ce.altaEvento(
            "Expointer Uruguay",
            "Exposición internacional agropecuaria y ganadera",
            LocalDate.of(2024, 12, 12),
            "EXPOAGRO",
            new DTCategorias(catEv06),
            "IMG-EV06.png"
        );

        // Ref 2025b — Montevideo Fashion Week
        ce.altaEvento(
            "Montevideo Fashion Week",
            "Pasarela de moda uruguaya e internacional",
            LocalDate.of(2025, 7, 20),
            "MFASHION",
            new DTCategorias(catEv07),
            null
        );
    }

    // =========================
    // USUARIOS
    // =========================
    public static void cargarUsuariosEjemplo() throws UsuarioYaExisteException {
        var cu = new logica.controladores.ControladorUsuario();

        // Asistentes
        cu.altaUsuario("atorres", "Ana", "atorres@gmail.com", null, null, "Torres",
            LocalDate.of(1990, 5, 12), "Facultad de Ingeniería", false, "123.torres", "IMG-US01.jpg");

        cu.altaUsuario("msilva", "Martin", "martin.silva@fing.edu.uy", null, null, "Silva",
            LocalDate.of(1987, 8, 21), "Facultad de Ingeniería", false, "msilva2025", "IMG-US02.jpg");

        cu.altaUsuario("sofirod", "Sofia", "srodriguez@outlook.com", null, null, "Rodriguez",
            LocalDate.of(1995, 2, 3), "Universidad Católica del Uruguay", false, "srod.abc1", "IMG-US03.jpeg");

        cu.altaUsuario("vale23", "Valentina", "valentina.costa@mail.com", null, null, "Costa",
            LocalDate.of(1992, 12, 1), null, false, "valen11c", "IMG-US07.jpeg");

        cu.altaUsuario("luciag", "Lucía", "lucia.garcia@mail.com", null, null, "García",
            LocalDate.of(1993, 11, 9), null, false, "garcia.22l", "IMG-US08.jpeg");

        cu.altaUsuario("andrearod", "Andrea", "andrea.rod@mail.com", null, null, "Rodríguez",
            LocalDate.of(2000, 6, 10), "Agencia Nacional de Investigación e Innovación (ANII)", false, "rod77and", "IMG-US09.jpeg");

        cu.altaUsuario("AnaG", "Ana", "ana.gomez@hotmail.com", null, null, "Gómez",
            LocalDate.of(1998, 3, 15), null, false, "gomez88a", "IMG-US12.png");

        cu.altaUsuario("JaviL", "Javier", "javier.lopez@outlook.com", null, null, "López",
            LocalDate.of(1995, 7, 22), null, false, "jl99lopez", "IMG-US13.jpeg");

        cu.altaUsuario("MariR", "María", "maria.rodriguez@gmail.com", null, null, "Rodríguez",
            LocalDate.of(2000, 11, 10), null, false, "maria55r", "IMG-US14.jpeg");

        cu.altaUsuario("SofiM", "Sofía", "sofia.martinez@yahoo.com", null, null, "Martínez",
            LocalDate.of(1997, 2, 5), null, false, "smarti99z", "IMG-US15.jpeg");

        // Organizadores
        cu.altaUsuario("miseventos", "MisEventos", "contacto@miseventos.com",
            "Empresa de organización de eventos.", "https://miseventos.com", null, null, null, true, "22miseventos", "IMG-US04.jpeg");

        cu.altaUsuario("techcorp", "Corporación Tecnológica", "info@techcorp.com",
            "Empresa líder en tecnologías de la información.", null, null, null, null, true, "tech25corp", "IMG-US05.jpeg");

        cu.altaUsuario("imm", "Intendencia de Montevideo", "contacto@imm.gub.uy",
            "Gobierno departamental de Montevideo.", "https://montevideo.gub.uy", null, null, null, true, "imm2025", "IMG-US06.png");

        cu.altaUsuario("udelar", "Universidad de la República", "contacto@udelar.edu.uy",
            "Universidad pública de Uruguay.", "https://udelar.edu.uy", null, null, null, true, "25udelar", "IMG-US10.jpeg");

        cu.altaUsuario("mec", "Ministerio de Educación y Cultura", "mec@mec.gub.uy",
            "Institución pública promotora de cultura.", "https://mec.gub.uy", null, null, null, true, "mec2025ok", "IMG-US11.png");
    }

    // =========================
    // EDICIONES (con ESTADO)
    // =========================
    public static void cargarEdicionesEjemplo() throws EdicionYaExisteException {
        var ce = new logica.controladores.ControladorEvento();
        var mEv = ManejadorEvento.getInstancia();
        var mUs = logica.manejadores.manejadorUsuario.getInstancia();

        // Helper: crea la edición y le setea estado (si tu Controlador no recibe estado)
        java.util.function.BiConsumer<String, DTEstado> setEstadoEdicion = (sigla, estado) -> {
            Ediciones ed = mEv.obtenerEdicion(sigla);
            if (ed != null) ed.setEstado(estado);
        };

        // 1) Montevideo Rock 2025 (evento: Montevideo Rock) — Estado: Aceptada
        {
            Eventos ev = mEv.obtenerEvento("Montevideo Rock");
            if (ev != null) {
                try {
                	ce.altaEdicionEvento(
                            ev,
                            mUs.getUsuarios().get("imm"),
                            "Montevideo Rock 2025",
                            "MONROCK25",
                            "",
                            parseFecha("20/11/2025"),
                            parseFecha("22/11/2025"),
                            parseFecha("12/03/2025"),
                            "Montevideo",
                            "Uruguay",
                            "IMG-EDEV01.jpeg"
                        );
                        setEstadoEdicion.accept("MONROCK25", DTEstado.Aceptada);
                }catch (excepciones.EventoYaExisteException | excepciones.FechasCruzadasException ex) {
                    System.err.println("No se pudo crear la edición 'Montevideo Rock 2025': " + ex.getMessage());
                }
            	
            }
        }

        // 2) Maratón de Montevideo (2025/2024/2022)
        {
            Eventos ev = mEv.obtenerEvento("Maratón de Montevideo");
            if (ev != null) {
                try {
                // 2025 — Aceptada
                ce.altaEdicionEvento(
                    ev, mUs.getUsuarios().get("imm"),
                    "Maratón de Montevideo 2025", "MARATON25", "",
                    parseFecha("14/09/2025"), parseFecha("14/09/2025"), parseFecha("05/02/2025"),
                    "Montevideo", "Uruguay", "IMG-EDEV02.png"
                );
                setEstadoEdicion.accept("MARATON25", DTEstado.Aceptada);

                // 2024 — Rechazada
                ce.altaEdicionEvento(
                    ev, mUs.getUsuarios().get("imm"),
                    "Maratón de Montevideo 2024", "MARATON24", "",
                    parseFecha("14/09/2024"), parseFecha("14/09/2024"), parseFecha("21/04/2024"),
                    "Montevideo", "Uruguay", "IMG-EDEV03.jpeg"
                );
                setEstadoEdicion.accept("MARATON24", DTEstado.Aceptada);

                // 2022 — Ingresada
                ce.altaEdicionEvento(
                    ev, mUs.getUsuarios().get("imm"),
                    "Maratón de Montevideo 2022", "MARATON22", "",
                    parseFecha("14/09/2022"), parseFecha("14/09/2022"), parseFecha("21/05/2022"),
                    "Montevideo", "Uruguay", "IMG-EDEV04.jpeg"
                );
                setEstadoEdicion.accept("MARATON22", DTEstado.Rechazada);
                }catch (excepciones.EventoYaExisteException | excepciones.FechasCruzadasException ex) {
                    System.err.println("No se pudo crear la edición 'Montevideo Rock 2025': " + ex.getMessage());
                }
            }
        }

        // 3) Montevideo Comics (2024/2025)
        {
            Eventos ev = mEv.obtenerEvento("Montevideo Comics");
            if (ev != null) {
                try {
                	// 2024 — Aceptada
                
                ce.altaEdicionEvento(
                    ev, mUs.getUsuarios().get("miseventos"),
                    "Montevideo Comics 2024", "COMICS24", "",
                    parseFecha("18/07/2024"), parseFecha("21/07/2024"), parseFecha("20/06/2024"),
                    "Montevideo", "Uruguay", "IMG-EDEV05.jpeg"
                );
                setEstadoEdicion.accept("COMICS24", DTEstado.Aceptada);

                // 2025 — Ingresada
                ce.altaEdicionEvento(
                    ev, mUs.getUsuarios().get("miseventos"),
                    "Montevideo Comics 2025", "COMICS25", "",
                    parseFecha("04/08/2025"), parseFecha("06/08/2025"), parseFecha("04/07/2025"),
                    "Montevideo", "Uruguay", "IMG-EDEV06.jpeg"
                );
                setEstadoEdicion.accept("COMICS25", DTEstado.Aceptada);
            }catch (excepciones.EventoYaExisteException | excepciones.FechasCruzadasException ex) {
                System.err.println("No se pudo crear la edición 'Montevideo Rock 2025': " + ex.getMessage());
            }
            }      
        }

        // 4) Expointer Uruguay 2025 — Aceptada
        {
            Eventos ev = mEv.obtenerEvento("Expointer Uruguay");
            if (ev != null) {
                try {
                	ce.altaEdicionEvento(
                            ev, mUs.getUsuarios().get("miseventos"),
                            "Expointer Uruguay 2025", "EXPOAGRO25", "",
                            parseFecha("11/09/2025"), parseFecha("17/09/2025"), parseFecha("01/02/2025"),
                            "Durazno", "Uruguay", "IMG-EDEV07.jpeg"
                        );
                        setEstadoEdicion.accept("EXPOAGRO25", DTEstado.Ingresada);
                }catch (excepciones.EventoYaExisteException | excepciones.FechasCruzadasException ex) {
                    System.err.println("No se pudo crear la edición 'Montevideo Rock 2025': " + ex.getMessage());
                }

            }
        }

        // 5) Conferencia de Tecnología (ediciones futuras/otras marcas)
        {
            Eventos ev = mEv.obtenerEvento("Conferencia de Tecnología");
            if (ev != null) {
                try {
                	// CONFTECH26 — Aceptada
                    ce.altaEdicionEvento(
                        ev, mUs.getUsuarios().get("udelar"),
                        "Tecnología Punta del Este 2026", "CONFTECH26", "",
                        parseFecha("06/04/2026"), parseFecha("10/04/2026"), parseFecha("01/08/2025"),
                        "Punta del Este", "Uruguay", "IMG-EDEV08.jpeg"
                    );
                    setEstadoEdicion.accept("CONFTECH26", DTEstado.Aceptada);

                    // MWC — Ingresada
                    ce.altaEdicionEvento(
                        ev, mUs.getUsuarios().get("techcorp"),
                        "Mobile World Congress 2025", "MWC", "",
                        parseFecha("12/12/2025"), parseFecha("15/12/2025"), parseFecha("21/08/2025"),
                        "Barcelona", "España", null
                    );
                    setEstadoEdicion.accept("MWC", DTEstado.Aceptada);

                    // WS26 — Ingresada
                    ce.altaEdicionEvento(
                        ev, mUs.getUsuarios().get("techcorp"),
                        "Web Summit 2026", "WS26", "",
                        parseFecha("13/01/2026"), parseFecha("01/02/2026"), parseFecha("04/06/2025"),
                        "Lisboa", "Portugal", null
                    );
                    setEstadoEdicion.accept("WS26", DTEstado.Aceptada);
                }catch (excepciones.EventoYaExisteException | excepciones.FechasCruzadasException ex) {
                    System.err.println("No se pudo crear la edición 'Montevideo Rock 2025': " + ex.getMessage());
                }

            }
        }
        
        {
            Eventos ev = mEv.obtenerEvento("Montevideo Fashion Week");
            if (ev != null) {
                try {
                	ce.altaEdicionEvento(
                            ev,
                            mUs.getUsuarios().get("mec"),
                            "Montevideo Fashion Week 2026",
                            "MFW26",
                            "",
                            parseFecha("16/02/2026"),
                            parseFecha("20/02/2026"),
                            parseFecha("02/10/2025"),
                            "Nueva York",
                            "Estados Unidos",
                            "IMG-EDEV11.jpeg"
                        );
                        setEstadoEdicion.accept("MFW26", DTEstado.Ingresada);
                }catch (excepciones.EventoYaExisteException | excepciones.FechasCruzadasException ex) {
                    System.err.println("No se pudo crear la edición 'Montevideo Fashion Week 2026': " + ex.getMessage());
                }
            	
            }
        }
    }

    // =========================
    // TIPOS DE REGISTRO
    // =========================
    public static void cargarTipoRegistroEjemplo() throws TipoRegistroYaExisteException, CupoTipoRegistroInvalidoException, CostoTipoRegistroInvalidoException {
        var ce = new logica.controladores.ControladorEvento();
        var mEv = ManejadorEvento.getInstancia();

        ce.altaTipoRegistro(mEv.obtenerEdicion("MONROCK25"), "General", "Acceso general a Montevideo Rock (2 días)", 1500, 2000);
        ce.altaTipoRegistro(mEv.obtenerEdicion("MONROCK25"), "VIP", "Incluye backstage + acceso preferencial", 4000, 200);

        ce.altaTipoRegistro(mEv.obtenerEdicion("MARATON25"), "Corredor 42K", "Inscripción a la maratón completa", 1200, 499);
        ce.altaTipoRegistro(mEv.obtenerEdicion("MARATON25"), "Corredor 21K", "Inscripción a la media maratón", 800, 700);
        ce.altaTipoRegistro(mEv.obtenerEdicion("MARATON25"), "Corredor 10K", "Inscripción a la carrera 10K", 500, 1000);

        ce.altaTipoRegistro(mEv.obtenerEdicion("MARATON24"), "Corredor 42K", "Inscripción a la maratón completa", 1000, 300);
        ce.altaTipoRegistro(mEv.obtenerEdicion("MARATON24"), "Corredor 21K", "Inscripción a la media maratón", 500, 500);

        ce.altaTipoRegistro(mEv.obtenerEdicion("MARATON22"), "Corredor 42K", "Inscripción a la maratón completa", 1100, 450);
        ce.altaTipoRegistro(mEv.obtenerEdicion("MARATON22"), "Corredor 21K", "Inscripción a la media maratón", 900, 750);
        ce.altaTipoRegistro(mEv.obtenerEdicion("MARATON22"), "Corredor 10K", "Inscripción a la carrera 10K", 650, 1400);

        ce.altaTipoRegistro(mEv.obtenerEdicion("COMICS24"), "General", "Entrada para los 4 días de Montevideo Comics", 600, 1500);
        ce.altaTipoRegistro(mEv.obtenerEdicion("COMICS24"), "Cosplayer", "Entrada especial con acreditación para concurso cosplay", 300, 50);

        ce.altaTipoRegistro(mEv.obtenerEdicion("COMICS25"), "General", "Entrada para los 4 días de Montevideo Comics", 800, 1000);
        ce.altaTipoRegistro(mEv.obtenerEdicion("COMICS25"), "Cosplayer", "Entrada especial con acreditación para concurso cosplay", 500, 100);

        ce.altaTipoRegistro(mEv.obtenerEdicion("EXPOAGRO25"), "General", "Acceso a la exposición agropecuaria", 300, 5000);
        ce.altaTipoRegistro(mEv.obtenerEdicion("EXPOAGRO25"), "Empresarial", "Acceso para empresas + networking", 2000, 5);

        ce.altaTipoRegistro(mEv.obtenerEdicion("CONFTECH26"), "Full", "Acceso ilimitado + Cena de gala", 1800, 300);
        ce.altaTipoRegistro(mEv.obtenerEdicion("CONFTECH26"), "General", "Acceso general", 1500, 500);
        ce.altaTipoRegistro(mEv.obtenerEdicion("CONFTECH26"), "Estudiante", "Acceso para estudiantes", 1000, 50);

        ce.altaTipoRegistro(mEv.obtenerEdicion("MWC"), "Full", "Acceso ilimitado + Cena de gala", 750, 550);
        ce.altaTipoRegistro(mEv.obtenerEdicion("MWC"), "General", "Acceso general", 500, 400);
        ce.altaTipoRegistro(mEv.obtenerEdicion("MWC"), "Estudiante", "Acceso para estudiantes", 250, 400);

        ce.altaTipoRegistro(mEv.obtenerEdicion("WS26"), "Full", "Acceso ilimitado + Cena de gala", 900, 30);
        ce.altaTipoRegistro(mEv.obtenerEdicion("WS26"), "General", "Acceso general", 650, 5);
        ce.altaTipoRegistro(mEv.obtenerEdicion("WS26"), "Estudiante", "Acceso para estudiantes", 300, 1);
    }

    // =========================
    // REGISTROS
    // =========================
    public static void cargarRegistrosEjemplo() {
        var ce = new logica.controladores.ControladorEvento();
        var mEv = ManejadorEvento.getInstancia();
        var mUs = logica.manejadores.manejadorUsuario.getInstancia();

        ce.altaRegistroEdicionEvento("atorres MONROCK25",
            mUs.getUsuarios().get("atorres"),
            mEv.obtenerEvento("Montevideo Rock"),
            mEv.obtenerEdicion("MONROCK25"),
            mEv.obtenerEdicion("MONROCK25").obtenerTipoRegistro("VIP"),
            LocalDate.of(2025, 5, 14), 4000, LocalDate.of(2025, 11, 20));

        ce.altaRegistroEdicionEvento("atorres MARATON24",
            mUs.getUsuarios().get("atorres"),
            mEv.obtenerEvento("Maratón de Montevideo"),
            mEv.obtenerEdicion("MARATON24"),
            mEv.obtenerEdicion("MARATON24").obtenerTipoRegistro("Corredor 21K"),
            LocalDate.of(2024, 7, 30), 500, LocalDate.of(2024, 9, 14));

        ce.altaRegistroEdicionEvento("sofirod WS26",
            mUs.getUsuarios().get("sofirod"),
            mEv.obtenerEvento("Conferencia de Tecnología"),
            mEv.obtenerEdicion("WS26"),
            mEv.obtenerEdicion("WS26").obtenerTipoRegistro("Estudiante"),
            LocalDate.of(2025, 8, 21), 300, LocalDate.of(2026, 1, 13));

        ce.altaRegistroEdicionEvento("atorres MARATON25",
            mUs.getUsuarios().get("atorres"),
            mEv.obtenerEvento("Maratón de Montevideo"),
            mEv.obtenerEdicion("MARATON25"),
            mEv.obtenerEdicion("MARATON25").obtenerTipoRegistro("Corredor 42K"),
            LocalDate.of(2025, 3, 3), 1200, LocalDate.of(2025, 9, 14));

        ce.altaRegistroEdicionEvento("msilva MWC",
            mUs.getUsuarios().get("msilva"),
            mEv.obtenerEvento("Conferencia de Tecnología"),
            mEv.obtenerEdicion("MWC"),
            mEv.obtenerEdicion("MWC").obtenerTipoRegistro("Full"),
            LocalDate.of(2025, 8, 22), 750, LocalDate.of(2025, 12, 12));

        ce.altaRegistroEdicionEvento("udelar MARATON25",
            mUs.getUsuarios().get("udelar"),
            mEv.obtenerEvento("Maratón de Montevideo"),
            mEv.obtenerEdicion("MARATON25"),
            mEv.obtenerEdicion("MARATON25").obtenerTipoRegistro("Corredor 10K"),
            LocalDate.of(2025, 4, 9), 500, LocalDate.of(2025, 9, 14));

        ce.altaRegistroEdicionEvento("mec MARATON25",
            mUs.getUsuarios().get("mec"),
            mEv.obtenerEvento("Maratón de Montevideo"),
            mEv.obtenerEdicion("MARATON25"),
            mEv.obtenerEdicion("MARATON25").obtenerTipoRegistro("Corredor 21K"),
            LocalDate.of(2025, 4, 10), 800, LocalDate.of(2025, 9, 14));

        ce.altaRegistroEdicionEvento("miseventos COMICS25",
            mUs.getUsuarios().get("miseventos"),
            mEv.obtenerEvento("Montevideo Comics"),
            mEv.obtenerEdicion("COMICS25"),
            mEv.obtenerEdicion("COMICS25").obtenerTipoRegistro("Cosplayer"),
            LocalDate.of(2025, 8, 3), 500, LocalDate.of(2025, 8, 4));

        ce.altaRegistroEdicionEvento("techcorp COMICS24",
            mUs.getUsuarios().get("techcorp"),
            mEv.obtenerEvento("Montevideo Comics"),
            mEv.obtenerEdicion("COMICS24"),
            mEv.obtenerEdicion("COMICS24").obtenerTipoRegistro("General"),
            LocalDate.of(2024, 7, 16), 600, LocalDate.of(2024, 7, 18));
    }

    // =========================
    // PATROCINIOS
    // =========================
    public static void cargarPatrociniosEjemplo() throws ValorPatrocinioExcedidoException {
        var ce = new logica.controladores.ControladorEvento();
        var mEv = ManejadorEvento.getInstancia();
        

        ce.altaPatrocinio(
            mEv.obtenerEdicion("CONFTECH26"),
            logica.manejadores.manejadorUsuario.getInstancia().findInstitucion("Facultad de Ingeniería"),
            DTNivel.ORO,
            mEv.obtenerEdicion("CONFTECH26").obtenerTipoRegistro("Estudiante"),
            20000,
            LocalDate.of(2025, 8, 21),
            4,
            "TECHFING"
        );

        ce.altaPatrocinio(
            mEv.obtenerEdicion("CONFTECH26"),
            logica.manejadores.manejadorUsuario.getInstancia().findInstitucion("Agencia Nacional de Investigación e Innovación (ANII)"),
            DTNivel.PLATA,
            mEv.obtenerEdicion("CONFTECH26").obtenerTipoRegistro("General"),
            10000,
            LocalDate.of(2025, 8, 20),
            1,
            "TECHANII"
        );

        ce.altaPatrocinio(
            mEv.obtenerEdicion("MARATON25"),
            logica.manejadores.manejadorUsuario.getInstancia().findInstitucion("Antel"),
            DTNivel.PLATINO,
            mEv.obtenerEdicion("MARATON25").obtenerTipoRegistro("Corredor 10K"),
            25000,
            LocalDate.of(2025, 3, 4),
            10,
            "CORREANTEL"
        );

        ce.altaPatrocinio(
            mEv.obtenerEdicion("EXPOAGRO25"),
            logica.manejadores.manejadorUsuario.getInstancia().findInstitucion("Universidad Católica del Uruguay"),
            DTNivel.BRONCE,
            mEv.obtenerEdicion("EXPOAGRO25").obtenerTipoRegistro("General"),
            15000,
            LocalDate.of(2025, 5, 5),
            10,
            "EXPOCAT"
        );
    }
}
