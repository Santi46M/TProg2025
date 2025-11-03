package testpersistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.spi.PersistenceProviderResolverHolder;

import logica.modelo.UsuarioOO;

import java.net.URL;
import java.util.List;

public class TestPersistence {
    public static void main(String[] args) {
        System.out.println("Starting persistence test...");

        // Environment/debug information
        System.out.println("java.version=" + System.getProperty("java.version"));
        System.out.println("java.class.version=" + System.getProperty("java.class.version"));

        URL res = Thread.currentThread().getContextClassLoader().getResource("logica.resources/persistence.xml");
        System.out.println("META-INF/persistence.xml resource: " + res);

        try {
            List providers = PersistenceProviderResolverHolder.getPersistenceProviderResolver().getPersistenceProviders();
            System.out.println("Discovered persistence providers: " + providers.size());
            for (Object p : providers) System.out.println(" - " + p.getClass().getName());
        } catch (Throwable t) {
            System.out.println("Error listing providers: " + t);
        }

        // Try loading EclipseLink provider class directly to see if it's on classpath
        try {
            Class<?> c = Class.forName("org.eclipse.persistence.jpa.PersistenceProvider");
            System.out.println("EclipseLink provider class found: " + c.getName() + " (loader: " + c.getClassLoader() + ")");
        } catch (Throwable t) {
            System.out.println("EclipseLink provider class NOT found: " + t);
        }



        System.out.println("java.class.path=" + System.getProperty("java.class.path"));

        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("ServidorCentralPU");
            em = emf.createEntityManager();
            EntityTransaction tx = em.getTransaction();

            tx.begin();
            UsuarioOO u = new UsuarioOO("testnick", "Test User", "test@example.com", "secret", "img.png", "Asistente");
            em.persist(u);
            tx.commit();

            UsuarioOO found = em.find(UsuarioOO.class, "testnick");
            System.out.println("Found user: " + found);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
            System.out.println("Persistence test finished.");
        }
    }
}