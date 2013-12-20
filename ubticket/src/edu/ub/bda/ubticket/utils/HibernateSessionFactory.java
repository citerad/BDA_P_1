package edu.ub.bda.ubticket.utils;

import java.sql.Connection;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author olopezsa13
 */
public class HibernateSessionFactory
{
    
    private static SessionFactory sf = null;
    private static Session s = null;
  
    static
    {
        //Inicializa el SF buscando los ficheros de configuracion
        try
        {
            sf = new Configuration().configure().buildSessionFactory();
            //System.out.println("Instanciando SF");
        }
        catch (HibernateException e)
        {
            System.out.println("Error SF: "+e.getMessage());
        }
    }

     /**
     * @return the sesion
     */
    public static Session getSession()
    {
        if ( s == null )
        {
            s = sf.openSession();
        }
        else
        {
            /*
             * Cuando veas esto y pienses que alguien se ha hecho la picha un lio,
             * y te preguntes: ¿Pero qué leches?
             * 
             * La respuesta a esta pregunta es simple:
             * 
             * Debido a que en la base de datos se usan varios TRIGGERs para poder
             * actualizar campos de la base de datos al insertar o borrar ENTRADAs,
             * cualquier consulta de selección sobre dicha tabla no reflejará
             * correctamente los cambios a no ser que se use una nueva sesión.
             * 
             * Por lo tanto, para garantizar la correcta funcionalidad, hay que cerrar
             * la sesión actual, cerrar la conexión si se nos devuelve una, y
             * abrir una nueva sesión.
             */
            
            Connection c = s.close();
            
            try
            {
                if ( c != null )
                    c.close();
            }
            catch ( SQLException ex )
            {
                ex.printStackTrace();
            }
            
            s = sf.openSession();
        }
        
        return s;
    }
}
