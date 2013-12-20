package edu.ub.bda.ubticket.utils;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Esta clase nos sirve para definir un punto común funcional 
 * para cualquier transacción a través de Hibernate.
 *
 * @author olopezsa13
 */
public abstract class HibernateTransaction<T>
{
    
    protected Session session;
    private Transaction transaction;
    
    private boolean createTransaction = true;
    
    protected HibernateTransaction()
    {
    }
    
    public synchronized T execute()
    {
        try
        {
            session = HibernateSessionFactory.getSession();
            
            T returnValue = null;
            
            if ( session != null )
            {
                session.setFlushMode(FlushMode.MANUAL);
                
                createTransaction = !session.getTransaction().isActive();
                
                if ( createTransaction )
                    transaction = session.beginTransaction();
                
                returnValue = run();
                
                if ( createTransaction )
                {
                    if ( transaction != null )
                        transaction.commit();
                }
                
                session.flush();
            }
            
            return returnValue;
        }
        catch ( RuntimeException ex )
        {
            if ( createTransaction )
            {
                try
                {
                    if ( transaction != null ) 
                        transaction.rollback();
                }
                catch ( Exception exx )
                {
                    // WOPS!
                }
            }
            
            throw ex;
        }
        finally
        {
            session = null;
            transaction = null;
        }
    }
    
    public abstract T run();
    
}
