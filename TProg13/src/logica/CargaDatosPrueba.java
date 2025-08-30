package logica;

import logica.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import excepciones.EventoYaExisteException;
import excepciones.InstitucionYaExisteException;
import excepciones.TipoRegistroYaExisteException;
import excepciones.UsuarioYaExisteException;

public class CargaDatosPrueba {
    public static void cargar() throws Exception {
        cargarCategorias();
        cargarInstitucionesEjemplo();
        cargarEventosEjemplo();
        cargarUsuariosEjemplo();
        cargarEdicionesEjemplo();
        cargarTipoRegistroEjemplo();
        cargarRegistrosEjemplo();
        cargarPatrociniosEjemplo();
    }

    // Variables globales para instituciones y categorías
    public static Institucion INS01, INS02, INS03, INS04, INS05;

    // Variables globales para eventos
    public static Eventos EV01, EV02, EV03, EV04, EV05, EV06, EV07;

    // Variables globales para ediciones de evento
    public static Ediciones EDEV01, EDEV02, EDEV03, EDEV04, EDEV05, EDEV06, EDEV07, EDEV08, EDEV09, EDEV10;

    // Variables globales para tipos de registro (simuladas para ejemplo)
    public static TipoRegistro TR02, TR07, TR25, TR03, TR20, TR05, TR04, TR14, TR11;
    public static Usuario US03, US07, US09, US12, US13, US14, US15;

    // Cargar instituciones y guardar en variables globales
    private static void cargarInstitucionesEjemplo() throws InstitucionYaExisteException {
        logica.ControladorUsuario controlador = new logica.ControladorUsuario();
        controlador.AltaInstitucion("Facultad de Ingeniería", "Facultad de Ingeniería de la Universidad de la República", "https://www.fing.edu.uy");
        controlador.AltaInstitucion("ORT Uruguay", "Universidad privada enfocada en tecnología y gestión", "https://ort.edu.uy");
        controlador.AltaInstitucion("Universidad Católica del Uruguay", "Institución de educación superior privada", "https://ucu.edu.uy");
        controlador.AltaInstitucion("Antel", "Empresa estatal de telecomunicaciones", "https://antel.com.uy");
        controlador.AltaInstitucion("Agencia Nacional de Investigación e Innovación (ANII)", "Fomenta la investigación y la innovación en Uruguay", "https://anii.org.uy");
    }

    private static void cargarCategorias() {
        logica.ControladorEvento controlador = new logica.ControladorEvento();
        //CA01
        controlador.AltaCategoria("Tecnología");
        //CA02
        controlador.AltaCategoria("Innovación");
        //CA03
        controlador.AltaCategoria("Literatura");
        //CA04
        controlador.AltaCategoria("Cultura");
        //CA05
        controlador.AltaCategoria("Música");
        //CA06
        controlador.AltaCategoria("Deporte");
        //CA07
        controlador.AltaCategoria("Salud");
        //CA08
        controlador.AltaCategoria("Entretenimiento");
        //CA09
        controlador.AltaCategoria("Agro");
        //CA10
        controlador.AltaCategoria("Negocios");
        //CA11
        controlador.AltaCategoria("Moda");
        //CA12
        controlador.AltaCategoria("Investigación");
        System.out.println("Categorías creadas:");
        for (String cat : logica.manejadorAuxiliar.getInstancia().listarCategorias()) {
            System.out.println(cat);
        }
    }
    
    private static void cargarEventosEjemplo() throws EventoYaExisteException {
        logica.ControladorEvento controlador = new logica.ControladorEvento();
        java.util.List<String> catEv01 = java.util.Arrays.asList("Tecnología", "Innovación");
        java.util.List<String> catEv02 = java.util.Arrays.asList("Literatura", "Cultura");
        java.util.List<String> catEv03 = java.util.Arrays.asList("Música");
        java.util.List<String> catEv04 = java.util.Arrays.asList("Deporte", "Salud");
        java.util.List<String> catEv05 = java.util.Arrays.asList("Entretenimiento");
        java.util.List<String> catEv06 = java.util.Arrays.asList("Agro", "Negocios");
        java.util.List<String> catEv07 = java.util.Arrays.asList("Moda", "Investigación");
        // EV01
        controlador.AltaEvento("Conferencia de Tecnología", "Evento sobre innovación tecnológica", java.time.LocalDate.of(2025, 1, 10), "CONFTEC", new DTCategorias(catEv01));
        // EV02
        controlador.AltaEvento("Feria del Libro", "Encuentro anual de literatura", java.time.LocalDate.of(2025, 2, 1), "FERLIB", new DTCategorias(catEv02));
        // EV03
        controlador.AltaEvento("Montevideo Rock", "Festival de rock con artistas nacionales e internacionales", java.time.LocalDate.of(2023, 3, 15), "MONROCK", new DTCategorias(catEv03));
        // EV04
        controlador.AltaEvento("Maratón de Montevideo", "Competencia deportiva anual en la capital", java.time.LocalDate.of(2022, 1, 1), "MARATON", new DTCategorias(catEv04));
        // EV05
        controlador.AltaEvento("Montevideo Comics", "Convención de historietas, cine y cultura geek", java.time.LocalDate.of(2024, 4, 10), "COMICS", new DTCategorias(catEv05));
        // EV06
        controlador.AltaEvento("Expointer Uruguay", "Exposición internacional agropecuaria y ganadera", java.time.LocalDate.of(2024, 12, 12), "EXPOAGRO", new DTCategorias(catEv06));
        // EV07
        controlador.AltaEvento("Montevideo Fashion Week", "Pasarela de moda uruguaya e internacional", java.time.LocalDate.of(2025, 7, 20), "MFASHION", new DTCategorias(catEv07));
        System.out.println("Eventos de ejemplo dados de alta.");
        // Listar todos los eventos cargados
        System.out.println("Eventos cargados:");
        for (Eventos ev : logica.ManejadorEvento.getInstancia().obtenerEventos().values()) {
            System.out.println("Nombre: " + ev.getNombre());
            System.out.println("Sigla: " + ev.getSigla());
            System.out.println("Descripción: " + ev.getDescripcion());
            System.out.println("Fecha: " + ev.getFecha());
            System.out.print("Categorías: ");
            if (ev.getCategorias() != null) {
                for (Categoria cat : ev.getCategorias().values()) {
                    System.out.print(cat.getNombre() + " ");
                }
            }
            System.out.println("\n----------------------");
        }
    }
    

    private static java.time.LocalDate parseFecha(String fecha) {
        String[] partes = fecha.split("/");
        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int anio = Integer.parseInt(partes[2]);
        return java.time.LocalDate.of(anio, mes, dia);
    }
    
    private static void cargarUsuariosEjemplo() throws UsuarioYaExisteException {
        logica.ControladorUsuario controlador = new logica.ControladorUsuario();
        manejadorUsuario manejador = manejadorUsuario.getInstancia();
        // Asistentes
        controlador.AltaUsuario("atorres", "Ana", "atorres@gmail.com", null, null, "Torres", java.time.LocalDate.of(1990, 5, 12), "Facultad de Ingeniería", false);
        controlador.AltaUsuario("msilva", "Martin", "martin.silva@fing.edu.uy", null, null, "Silva", java.time.LocalDate.of(1987, 8, 21), "Facultad de Ingeniería", false);
        controlador.AltaUsuario("sofirod", "Sofia", "srodriguez@outlook.com", null, null, "Rodriguez", java.time.LocalDate.of(1995, 2, 3), "Universidad Católica del Uruguay", false);
        controlador.AltaUsuario("vale23", "Valentina", "valentina.costa@mail.com", null, null, "Costa", java.time.LocalDate.of(1992, 12, 1), null, false);
        controlador.AltaUsuario("luciag", "Lucía", "lucia.garcia@mail.com", null, null, "García", java.time.LocalDate.of(1993, 11, 9), null, false);
        controlador.AltaUsuario("andrearod", "Andrea", "andrea.rod@mail.com", null, null, "Rodríguez", java.time.LocalDate.of(2000, 6, 10), "Agencia Nacional de Investigación e Innovación (ANII)", false);
        controlador.AltaUsuario("AnaG", "Ana", "ana.gomez@hotmail.com", null, null, "Gómez", java.time.LocalDate.of(1998, 3, 15), null, false);
        controlador.AltaUsuario("JaviL", "Javier", "javier.lopez@outlook.com", null, null, "López", java.time.LocalDate.of(1995, 7, 22), null, false);
        controlador.AltaUsuario("MariR", "María", "maria.rodriguez@gmail.com", null, null, "Rodríguez", java.time.LocalDate.of(2000, 11, 10), null, false);
        controlador.AltaUsuario("SofiM", "Sofía", "sofia.martinez@yahoo.com", null, null, "Martínez", java.time.LocalDate.of(1997, 2, 5), null, false);
        // Organizadores
        //US04
        controlador.AltaUsuario("miseventos", "MisEventos", "contacto@miseventos.com", "Empresa de organización de eventos.", "https://miseventos.com", null, null, null, true);
        //US05
        controlador.AltaUsuario("techcorp", "Corporación Tecnológica", "info@techcorp.com", "Empresa líder en tecnologías de la información.", null, null, null, null, true);
        //US06
        controlador.AltaUsuario("imm", "Intendencia de Montevideo", "contacto@imm.gub.uy", "Gobierno departamental de Montevideo.", "https://montevideo.gub.uy", null, null, null, true);
        //US10
        controlador.AltaUsuario("udelar", "Universidad de la República", "contacto@udelar.edu.uy", "Universidad pública de Uruguay.", "https://udelar.edu.uy", null, null, null, true);
        //US11
        controlador.AltaUsuario("mec", "Ministerio de Educación y Cultura", "mec@mec.gub.uy", "Institución pública promotora de cultura.", "https://mec.gub.uy", null, null, null, true);
    }
    
    private static void cargarEdicionesEjemplo() throws Exception {
        logica.ControladorEvento controlador = new logica.ControladorEvento();
        ManejadorEvento manejadorEvento = ManejadorEvento.getInstancia();
        logica.manejadorUsuario manejadorUsuario = logica.manejadorUsuario.getInstancia();
        // Helper para obtener evento y validar
        Eventos ev;
        // EDEV01
        ev = manejadorEvento.obtenerEvento("Conferencia de Tecnología");
        if (ev == null) { System.err.println("Evento Conferencia de Tecnología no encontrado"); } else {
            controlador.AltaEdicionEvento(
                ev,
                manejadorUsuario.getUsuarios().get("imm"),
                "Montevideo Rock 2025", "MONROCK25", "", parseFecha("20/11/2025"), parseFecha("22/11/2025"), parseFecha("12/03/2025"), "Montevideo", "Uruguay"
            );
        }
        // EDEV02
        ev = manejadorEvento.obtenerEvento("Maratón de Montevideo");
        if (ev == null) { System.err.println("Evento Maratón de Montevideo no encontrado"); } else {
            controlador.AltaEdicionEvento(
                ev,
                manejadorUsuario.getUsuarios().get("imm"),
                "Maratón de Montevideo 2025", "MARATON25", "", parseFecha("14/09/2025"), parseFecha("14/09/2025"), parseFecha("05/02/2025"), "Montevideo", "Uruguay"
            );
            controlador.AltaEdicionEvento(
                ev,
                manejadorUsuario.getUsuarios().get("imm"),
                "Maratón de Montevideo 2024", "MARATON24", "", parseFecha("14/09/2024"), parseFecha("14/09/2024"), parseFecha("21/04/2024"), "Montevideo", "Uruguay"
            );
            controlador.AltaEdicionEvento(
                ev,
                manejadorUsuario.getUsuarios().get("imm"),
                "Maratón de Montevideo 2022", "MARATON22", "", parseFecha("14/09/2022"), parseFecha("14/09/2022"), parseFecha("21/05/2022"), "Montevideo", "Uruguay"
            );
        }
        // EDEV05
        ev = manejadorEvento.obtenerEvento("Montevideo Comics");
        if (ev == null) { System.err.println("Evento Montevideo Comics no encontrado"); } else {
            controlador.AltaEdicionEvento(
                ev,
                manejadorUsuario.getUsuarios().get("miseventos"),
                "Montevideo Comics 2024", "COMICS24", "", parseFecha("18/07/2024"), parseFecha("21/07/2024"), parseFecha("20/06/2024"), "Montevideo", "Uruguay"
            );
            controlador.AltaEdicionEvento(
                ev,
                manejadorUsuario.getUsuarios().get("miseventos"),
                "Montevideo Comics 2025", "COMICS25", "", parseFecha("04/08/2025"), parseFecha("06/08/2025"), parseFecha("04/07/2025"), "Montevideo", "Uruguay"
            );
        }
        // EDEV07
        ev = manejadorEvento.obtenerEvento("Expointer Uruguay");
        if (ev == null) { System.err.println("Evento Expointer Uruguay no encontrado"); } else {
            controlador.AltaEdicionEvento(
                ev,
                manejadorUsuario.getUsuarios().get("miseventos"),
                "Expointer Uruguay 2025", "EXPOAGRO25", "", parseFecha("11/09/2025"), parseFecha("17/09/2025"), parseFecha("01/02/2025"), "Durazno", "Uruguay"
            );
        }
        // EDEV08
        ev = manejadorEvento.obtenerEvento("Conferencia de Tecnología");
        if (ev == null) { System.err.println("Evento Conferencia de Tecnología no encontrado"); } else {
            controlador.AltaEdicionEvento(
                ev,
                manejadorUsuario.getUsuarios().get("udelar"),
                "Tecnología Punta del Este 2026", "CONFTECH26", "", parseFecha("06/04/2026"), parseFecha("10/04/2026"), parseFecha("01/08/2025"), "Punta del Este", "Uruguay"
            );
            controlador.AltaEdicionEvento(
                ev,
                manejadorUsuario.getUsuarios().get("techcorp"),
                "Mobile World Congress 2025", "MWC", "", parseFecha("12/12/2025"), parseFecha("15/12/2025"), parseFecha("21/08/2025"), "Barcelona", "España"
            );
            controlador.AltaEdicionEvento(
                ev,
                manejadorUsuario.getUsuarios().get("techcorp"),
                "Web Summit 2026", "WS26", "", parseFecha("13/01/2026"), parseFecha("01/02/2026"), parseFecha("04/06/2025"), "Lisboa", "Portugal"
            );
        }
        System.out.println("Ediciones de evento de ejemplo dadas de alta.");
    }
    
    private static void cargarTipoRegistroEjemplo() throws TipoRegistroYaExisteException {
        logica.ControladorEvento controlador = new logica.ControladorEvento();
        ManejadorEvento manejadorEvento = ManejadorEvento.getInstancia();
        //TR01 - General para MONROCK25
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("MONROCK25"), "General", "Acceso general a Montevideo Rock (2 días)", 1500, 2000);
        //TR02 - VIP para MONROCK25
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("MONROCK25"), "VIP", "Incluye backstage + acceso preferencial", 4000, 200);
        //TR03 - Corredor 42K para MARATON25
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("MARATON25"), "Corredor 42K", "Inscripción a la maratón completa", 1200, 499);
        //TR04 - Corredor 21K para MARATON25
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("MARATON25"), "Corredor 21K", "Inscripción a la media maratón", 800, 700);
        //TR05 - Corredor 10K para MARATON25
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("MARATON25"), "Corredor 10K", "Inscripción a la carrera 10K", 500, 1000);
        //TR06 - Corredor 42K para MARATON24
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("MARATON24"), "Corredor 42K", "Inscripción a la maratón completa", 1000, 300);
        //TR07 - Corredor 21K para MARATON24
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("MARATON24"), "Corredor 21K", "Inscripción a la media maratón", 500, 500);
        //TR08 - Corredor 42K para MARATON22
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("MARATON22"), "Corredor 42K", "Inscripción a la maratón completa", 1100, 450);
        //TR09 - Corredor 21K para MARATON22
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("MARATON22"), "Corredor 21K", "Inscripción a la media maratón", 900, 750);
        //TR10 - Corredor 10K para MARATON22
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("MARATON22"), "Corredor 10K", "Inscripción a la carrera 10K", 650, 1400);
        //TR11 - General para COMICS24
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("COMICS24"), "General", "Entrada para los 4 días de Montevideo Comics", 600, 1500);
        //TR12 - Cosplayer para COMICS24
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("COMICS24"), "Cosplayer", "Entrada especial con acreditación para concurso cosplay", 300, 50);
        //TR13 - General para COMICS25
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("COMICS25"), "General", "Entrada para los 4 días de Montevideo Comics", 800, 1000);
        //TR14 - Cosplayer para COMICS25
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("COMICS25"), "Cosplayer", "Entrada especial con acreditación para concurso cosplay", 500, 100);
        //TR15 - General para EXPOAGRO25
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("EXPOAGRO25"), "General", "Acceso a la exposición agropecuaria", 300, 5000);
        //TR16 - Empresarial para EXPOAGRO25
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("EXPOAGRO25"), "Empresarial", "Acceso para empresas + networking", 2000, 5);
        //TR17 - Full para CONFTECH26
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("CONFTECH26"), "Full", "Acceso ilimitado + Cena de gala", 1800, 300);
        //TR18 - General para CONFTECH26
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("CONFTECH26"), "General", "Acceso general", 1500, 500);
        //TR19 - Estudiante para CONFTECH26
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("CONFTECH26"), "Estudiante", "Acceso para estudiantes", 1000, 50);
        //TR20 - Full para MWC
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("MWC"), "Full", "Acceso ilimitado + Cena de gala", 750, 550);
        //TR21 - General para MWC
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("MWC"), "General", "Acceso general", 500, 400);
        //TR22 - Estudiante para MWC
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("MWC"), "Estudiante", "Acceso para estudiantes", 250, 400);
        //TR23 - Full para WS26
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("WS26"), "Full", "Acceso ilimitado + Cena de gala", 900, 30);
        //TR24 - General para WS26
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("WS26"), "General", "Acceso general", 650, 5);
        //TR25 - Estudiante para WS26
        controlador.AltaTipoRegistro(manejadorEvento.obtenerEdicion("WS26"), "Estudiante", "Acceso para estudiantes", 300, 1);
        System.out.println("Tipos de registro de ejemplo dados de alta.");
    }
    
    private static void cargarRegistrosEjemplo() {
        logica.ControladorEvento controlador = new logica.ControladorEvento();
        ManejadorEvento manejadorEvento = ManejadorEvento.getInstancia();
        logica.manejadorUsuario manejadorUsuario = logica.manejadorUsuario.getInstancia();        // Formato: idRegistro, usuario, evento, edicion, tipoRegistro, fechaRegistro, costo, fechaInicio
        controlador.altaRegistroEdicionEvento("RE01", manejadorUsuario.getUsuarios().get("atorres"), manejadorEvento.obtenerEvento("Montevideo Rock"), manejadorEvento.obtenerEdicion("MONROCK25"), manejadorEvento.obtenerEdicion("MONROCK25").obtenerTipoRegistro("VIP"), java.time.LocalDate.of(2025, 5, 14), 4000, java.time.LocalDate.of(2025, 11, 20));
        controlador.altaRegistroEdicionEvento("RE02", manejadorUsuario.getUsuarios().get("atorres"), manejadorEvento.obtenerEvento("Maratón de Montevideo"), manejadorEvento.obtenerEdicion("MARATON24"), manejadorEvento.obtenerEdicion("MARATON24").obtenerTipoRegistro("Corredor 21K"), java.time.LocalDate.of(2024, 7, 30), 500, java.time.LocalDate.of(2024, 9, 14));
        controlador.altaRegistroEdicionEvento("RE03", manejadorUsuario.getUsuarios().get("sofirod"), manejadorEvento.obtenerEvento("Conferencia de Tecnología"), manejadorEvento.obtenerEdicion("WS26"), manejadorEvento.obtenerEdicion("WS26").obtenerTipoRegistro("Estudiante"), java.time.LocalDate.of(2025, 8, 21), 300, java.time.LocalDate.of(2026, 1, 13));
        controlador.altaRegistroEdicionEvento("RE04", manejadorUsuario.getUsuarios().get("atorres"), manejadorEvento.obtenerEvento("Maratón de Montevideo"), manejadorEvento.obtenerEdicion("MARATON25"), manejadorEvento.obtenerEdicion("MARATON25").obtenerTipoRegistro("Corredor 42K"), java.time.LocalDate.of(2025, 3, 3), 1200, java.time.LocalDate.of(2025, 9, 14));
        controlador.altaRegistroEdicionEvento("RE05", manejadorUsuario.getUsuarios().get("msilva"), manejadorEvento.obtenerEvento("Conferencia de Tecnología"), manejadorEvento.obtenerEdicion("MWC"), manejadorEvento.obtenerEdicion("MWC").obtenerTipoRegistro("Full"), java.time.LocalDate.of(2025, 8, 22), 750, java.time.LocalDate.of(2025, 12, 12));
//        controlador.altaRegistroEdicionEvento("RE06", manejadorUsuario.getUsuarios().get("udelar"), manejadorEvento.obtenerEvento("Maratón de Montevideo"), manejadorEvento.obtenerEdicion("MARATON25"), manejadorEvento.obtenerEdicion("MARATON25").obtenerTipoRegistro("Corredor 10K"), java.time.LocalDate.of(2025, 4, 9), 500, java.time.LocalDate.of(2025, 9, 14));
//        controlador.altaRegistroEdicionEvento("RE07", manejadorUsuario.getUsuarios().get("mec"), manejadorEvento.obtenerEvento("Maratón de Montevideo"), manejadorEvento.obtenerEdicion("MARATON25"), manejadorEvento.obtenerEdicion("MARATON25").obtenerTipoRegistro("Corredor 21K"), java.time.LocalDate.of(2025, 4, 10), 800, java.time.LocalDate.of(2025, 9, 14));
//        controlador.altaRegistroEdicionEvento("RE08", manejadorUsuario.getUsuarios().get("miseventos"), manejadorEvento.obtenerEvento("Montevideo Comics"), manejadorEvento.obtenerEdicion("COMICS25"), manejadorEvento.obtenerEdicion("COMICS25").obtenerTipoRegistro("Cosplayer"), java.time.LocalDate.of(2025, 8, 3), 500, java.time.LocalDate.of(2025, 8, 4));
//        controlador.altaRegistroEdicionEvento("RE09", manejadorUsuario.getUsuarios().get("techcorp"), manejadorEvento.obtenerEvento("Montevideo Comics"), manejadorEvento.obtenerEdicion("COMICS24"), manejadorEvento.obtenerEdicion("COMICS24").obtenerTipoRegistro("General"), java.time.LocalDate.of(2024, 7, 16), 600, java.time.LocalDate.of(2024, 7, 18));
        System.out.println("Registros de ejemplo dados de alta.");
    }
    
    private static void cargarPatrociniosEjemplo() {
        logica.ControladorEvento controlador = new logica.ControladorEvento();
        ManejadorEvento manejadorEvento = ManejadorEvento.getInstancia();
        logica.ControladorUsuario controladorUsuario = new logica.ControladorUsuario();
        // Obtener instituciones usando el método por nombre
        Institucion ins01 = controladorUsuario.getInstitucionPorNombre("Facultad de Ingeniería");
        Institucion ins03 = controladorUsuario.getInstitucionPorNombre("Universidad Católica del Uruguay");
        Institucion ins04 = controladorUsuario.getInstitucionPorNombre("Antel");
        Institucion ins05 = controladorUsuario.getInstitucionPorNombre("Agencia Nacional de Investigación e Innovación (ANII)");
        // Obtener ediciones y tipos de registro
        Ediciones edev08 = manejadorEvento.obtenerEdicion("CONFTECH26");
        Ediciones edev02 = manejadorEvento.obtenerEdicion("MARATON25");
        Ediciones edev07 = manejadorEvento.obtenerEdicion("EXPOAGRO25");
        // Tipos de registro
        TipoRegistro tr19 = edev08 != null ? edev08.obtenerTipoRegistro("Estudiante") : null;
        TipoRegistro tr18 = edev08 != null ? edev08.obtenerTipoRegistro("General") : null;
        TipoRegistro tr05 = edev02 != null ? edev02.obtenerTipoRegistro("Corredor 10K") : null;
        TipoRegistro tr15 = edev07 != null ? edev07.obtenerTipoRegistro("General") : null;
        // Alta de patrocinios con los parámetros correctos
        controlador.AltaPatrocinio(edev08, ins01, DTNivel.ORO, tr19, 20000, java.time.LocalDate.of(2025, 8, 21), 4, "TECHFING");
        controlador.AltaPatrocinio(edev08, ins05, DTNivel.PLATA, tr18, 10000, java.time.LocalDate.of(2025, 8, 20), 1, "TECHANII");
        controlador.AltaPatrocinio(edev02, ins04, DTNivel.PLATINO, tr05, 25000, java.time.LocalDate.of(2025, 3, 4), 10, "CORREANTEL");
        controlador.AltaPatrocinio(edev07, ins03, DTNivel.BRONCE, tr15, 15000, java.time.LocalDate.of(2025, 5, 5), 10, "EXPOCAT");
        System.out.println("Patrocinios de ejemplo dados de alta.");
    }
    
}