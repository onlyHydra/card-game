package rosu.persistence.repository;

import domain.Runda;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import rosu.persistence.RundaRepository;

import java.util.List;

public class RundaRepositoryHibernate implements RundaRepository {
    @Override
    public Runda findOne(Integer integer) throws IllegalArgumentException {
        initialize();
        try(Session session= sessionFactory.openSession()){
            session.beginTransaction();
            Query query=session.createQuery("from Runda where idRunda=:id");
            query.setParameter("id", integer);
            Runda runda= (Runda) query.uniqueResult();
            session.getTransaction().commit();
            close();
            return  runda;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            close();
        }
        return null;
    }

    @Override
        public Iterable<Runda> findAll() {
        initialize();
        try(Session session= sessionFactory.openSession()){
            session.beginTransaction();
            List<Runda> runde =
                    session.createQuery("from Runda", Runda.class)
                            .list();
            session.getTransaction().commit();
            close();
            return  runde;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            close();
        }
        return null;
    }

    @Override
    public void save(Runda entity) {
        initialize();
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Runda runda = new Runda(entity.getNrRunda(),entity.getIdJoc(),entity.getUsername(), entity.getCarteAleasa(), entity.getNrCartiCastigate());
                session.save(runda);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
    }

    static SessionFactory sessionFactory;
    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.err.println("Exception "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }


    @Override
    public Iterable<Runda> findByPlayerGame(String username, int idJoc) {
        initialize();
        try(Session session= sessionFactory.openSession()){
            session.beginTransaction();
            Query query=session.createQuery("from Runda where idJoc=:idJoc and username=:username");
            query.setParameter("idJoc", idJoc);
            query.setParameter("username",username);
            List<Runda> runde= query.list();
            session.getTransaction().commit();
            close();
            return  runde;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            close();
        }
        return null;
    }

    @Override
    public int nrCartiCastigate(int idJoc, String username) {
        initialize();
        int rez=0;
        try(Session session= sessionFactory.openSession()){
            session.beginTransaction();
            Query query=session.createQuery("from Runda where idJoc=:idJoc and username=:username");
            query.setParameter("idJoc", idJoc);
            query.setParameter("username",username);
            List<Runda> runde= query.list();
            session.getTransaction().commit();
            close();
            for (Runda r: runde) {
                System.out.println(r);
                rez += r.getNrCartiCastigate();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            close();
        }
        System.out.println(username+"Returnez "+rez);
        return rez;
    }
}
