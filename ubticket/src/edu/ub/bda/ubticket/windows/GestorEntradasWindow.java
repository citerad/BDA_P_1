package edu.ub.bda.ubticket.windows;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Table;
import edu.ub.bda.ubticket.beans.Entrada;
import edu.ub.bda.ubticket.beans.Espectaculo;
import edu.ub.bda.ubticket.beans.Sesion;
import edu.ub.bda.ubticket.beans.Usuario;
import edu.ub.bda.ubticket.utils.AutenticacionServicio;
import edu.ub.bda.ubticket.utils.HibernateTransaction;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author olopezsa13
 */
public class GestorEntradasWindow extends Window
{
    
    private Usuario usuario;
    
    private Panel todoPanel;
    private Panel tablaPanel;
    private Table tabla;
    private Label paginaEtiqueta;
    
    private Integer maxFilas = 15;
    private Integer pagina = 0;
    private Integer maxPaginas = 0;
    
    public GestorEntradasWindow()
    {
        super("Gestionar entradas");
        
        todoPanel = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        todoPanel.setVisible(false);
        
        tablaPanel = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
        
        Panel paginadorPanel = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
        
        paginadorPanel.addComponent(new Button("<--", new Action() {

            @Override
            public void doAction()
            {
                if ( pagina > 0 )
                {
                    pagina--;
                    actualizarTabla();
                }
            }
        
        }));
        
        paginaEtiqueta = new Label();
        paginaEtiqueta.setText(getTextoPagina());
        paginadorPanel.addComponent(paginaEtiqueta);
        
        paginadorPanel.addComponent(new Button("-->", new Action() {

            @Override
            public void doAction()
            {
                if ( ( pagina + 1 ) < maxPaginas )
                {
                    pagina++;
                    actualizarTabla();
                }
            }
        
        }));
        
        todoPanel.addComponent(tablaPanel);
        todoPanel.addComponent(paginadorPanel);
        addComponent(todoPanel);
        
        addComponent(new Button("Salir", new Action() {

            @Override
            public void doAction()
            {
                todoPanel.setVisible(false);
                
                if ( tabla != null )
                {
                    tabla.removeAllRows();
                }
                
                close();
            }
        
        }));
    }
    
    @Override
    public void onVisible()
    {
        usuario = AutenticacionServicio.GetUsuario();
        actualizarTabla();
    }
    
    private void actualizarTabla()
    {
        actualizarPagina();
                
        List<Entrada> list = new HibernateTransaction<List<Entrada>>() {

            @Override
            public List<Entrada> run()
            {
                Criteria query = session.createCriteria(Entrada.class)
                        .add(Restrictions.eq("usuario", usuario));
                query.setFirstResult(pagina * maxFilas);
                query.setMaxResults(maxFilas);
                return query.list();
            }
        
        }.execute();
        
        if ( list != null && list.size() > 0 )
        {
            boolean addComponent = false;
            if ( tabla == null )
            {
                tabla = new Table(4, "Entradas compradas");
                addComponent = true;
            }
            else
            {
                tabla.removeAllRows();
            }
            
            for ( Entrada entrada : list )
            {
                Sesion sesion = entrada.getSesion();
                Espectaculo o = sesion.getEspectaculo();
                tabla.addRow(new Label(o.getTitulo()),
                        new Label(o.getCategoria().getNombre()),
                        new Label(sesion.getFecha_inicio().toString()),
                        new Button("Cancelar", new BorrarEntradaAction(this, entrada)));
            }
        
            if ( addComponent )
            {
                tablaPanel.addComponent(tabla);
            }
            todoPanel.setVisible(true);
        }
        else
        {
            if ( tabla != null )
                tabla.removeAllRows();
            
            pagina = 0;
            actualizarPagina();
        }
    }
    
    private String getTextoPagina()
    {
        Integer pag = pagina + 1;
        
        if ( maxPaginas == 0 )
            pag = 0;
        
        return pag.toString() + " / " + maxPaginas.toString();
    }
    
    private void actualizarPagina()
    {
        Integer numFilas = new HibernateTransaction<Integer>() {

            @Override
            public Integer run()
            {
                Query query = session.createSQLQuery("SELECT COUNT(*) FROM ENTRADA WHERE USUARIO_ID = :usuario_id");
                query.setInteger("usuario_id", usuario.getId());
                return (Integer) query.list().get(0);
            }
        
        }.execute();
        Double maxPaginasFP = numFilas.doubleValue() / maxFilas.doubleValue();
        maxPaginas = (int) Math.ceil(maxPaginasFP.doubleValue());
        paginaEtiqueta.setText(getTextoPagina());
    }
    
    private class BorrarEntradaAction implements Action
    {
        
        private GestorEntradasWindow gestor;
        private Entrada entrada;
        private Usuario usuario;
        
        public BorrarEntradaAction(GestorEntradasWindow gestor, Entrada entrada)
        {
            this.gestor = gestor;
            this.entrada = entrada;
            usuario = AutenticacionServicio.GetUsuario();
        }

        @Override
        public void doAction()
        {
            Integer numEntradas = (Integer) usuario.Sesion("ENTRADAS_SESION_" + entrada.getSesion().getId());
            numEntradas--;
            usuario.Sesion("ENTRADAS_SESION_" + entrada.getSesion().getId(), numEntradas);
            
            new HibernateTransaction() {

                @Override
                public Object run()
                {
                    session.delete(entrada);
                    return null;
                }
            
            }.execute();
            
            gestor.actualizarTabla();
        }
        
    }
    
}
