package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Person;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade implements IPersonFacade{

    private static PersonFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private PersonFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    //TODO Remove/Change this before use
    public long getPersonCount(){
        EntityManager em = emf.createEntityManager();
        try{
            long pCount = (long)em.createQuery("SELECT COUNT(r) FROM Person r").getSingleResult();
            return pCount;
        }finally{  
            em.close();
        }
        
    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone) {
        EntityManager em = emf.createEntityManager(); 
        Person p = new Person (fName, lName, phone); 
        
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
            return new PersonDTO(p); 
        }
        finally {
            em.close();
        }
    }

    @Override
    public PersonDTO deletePerson(int id) {
        EntityManager em = getEntityManager(); 
        Person person = em.find(Person.class, id); 
        PersonDTO p = new PersonDTO(person); 
        try {
            em.getTransaction().begin();
            em.remove(person);
            em.getTransaction().commit();
            return p; 
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO getPerson(int id) {
        EntityManager em = getEntityManager(); 
        try {
            Person person = em.find(Person.class, id); 
            PersonDTO personDTO = new PersonDTO(person); 
            return personDTO; 
        }
        finally {
            em.close(); 
        }
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager(); 
        TypedQuery<Person> q = em.createQuery("select p from Person p", Person.class) ;
        List<Person> all = q.getResultList(); 
        return new PersonsDTO(all); 
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
