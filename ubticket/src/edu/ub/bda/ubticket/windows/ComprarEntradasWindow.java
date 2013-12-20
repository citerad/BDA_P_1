package edu.ub.bda.ubticket.windows;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Table;
import com.googlecode.lanterna.gui.dialog.ListSelectDialog;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import edu.ub.bda.UBTicket;
import edu.ub.bda.ubticket.beans.Entrada;
import edu.ub.bda.ubticket.beans.Espacio;
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
public class ComprarEntradasWindow extends Window
{
    
    private UBTicket ubticket;
    private Button seleccionarEspacioButton;
    private Espacio espacio = new Espacio();
    private Panel tablaPanel;
    private Table tabla;
    private Label paginaEtiqueta;
    private Panel paginadorPanel;
    private Usuario usuario;
    private static final int IVA=20;
    
    private int maxEntradasComprables = 6;
    
    
    private Integer maxFilas = 10;
    private Integer pagina = 0;
    private Integer maxPaginas = 0;
    
    public ComprarEntradasWindow(final UBTicket ubticket)
    {
        super("Comprar entradas");
        this.ubticket = ubticket;
        
        Panel espacioPanel = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
        espacioPanel.addComponent(new Label(" Espacio seleccionado:"));
        seleccionarEspacioButton = new Button("Ninguno", new Action() {

            @Override
            public void doAction()
            {
                List<Espacio> espacios = new HibernateTransaction<List<Espacio>>() {

                    @Override
                    public List<Espacio> run()
                    {
                        /**
                         * @TODO Hay que soportar listas paginadas de espacios
                         */
                        Query query = session.createQuery("from Espacio");
                        return (List<Espacio>) query.list();
                    }

                }.execute();
                
                Espacio[] vector = new Espacio[espacios.size()];
                espacios.toArray(vector);
                Espacio esp = ListSelectDialog.showDialog(ubticket.getGUIScreen(), "ATENCIÓN", "Seleccione el espacio:", vector);
                if ( esp != null )
                {
                    pagina = 0;
                    espacio.copyFrom(esp);
                    seleccionarEspacioButton.setText(esp.getNombre());
                    paginadorPanel.setVisible(true);
                    actualizarTabla();
                }
            }
        
        });
        espacioPanel.addComponent(seleccionarEspacioButton);
        addComponent(espacioPanel);
        
        
        tablaPanel = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
        addComponent(tablaPanel);
        
        paginadorPanel = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);   
        paginadorPanel.setVisible(false);
        
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
        
        addComponent(paginadorPanel);
        
        addComponent(new Button("Cancelar", new Action() {

            @Override
            public void doAction()
            {
                close();
                
                espacio = new Espacio();
                seleccionarEspacioButton.setText("Ninguno");
                paginadorPanel.setVisible(false);
                tablaPanel.removeComponent(tabla);
                tabla = null;
            }
        
        }));
    }
    
    @Override
    public void onVisible()
    {
        usuario = AutenticacionServicio.GetUsuario();
        this.setFocus(seleccionarEspacioButton);
    }
    
    private void actualizarTabla()
    {
        actualizarPagina();
                
        List<Sesion> list = new HibernateTransaction<List<Sesion>>() {

            @Override
            public List<Sesion> run()
            {
                Criteria query = session.createCriteria(Sesion.class)
                        .add(Restrictions.eq("espacio", espacio));
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
                tabla = new Table(4, "Espectáculos");
                addComponent = true;
            }
            else
            {
                tabla.removeAllRows();
            }
            
            for ( Sesion sesion : list )
            {
                Espectaculo o = sesion.getEspectaculo();
                tabla.addRow(new Label(o.getTitulo()),
                        new Label(o.getCategoria().getNombre()),
                        new Label(sesion.getFecha_inicio().toString()),
                        new Button("Comprar por " + sesion.getPrecio().toString() + "€/"+sesion.getPrecio()*(IVA+100)/100+" IVA inc.", new ComprarEntradaAction(ubticket, sesion, usuario)));
            }
        
            if ( addComponent )
            {
                tablaPanel.addComponent(tabla);
            }
        }
    }
    
    private String getTextoPagina()
    {
        Integer pag = pagina + 1;
        return pag.toString() + " / " + maxPaginas.toString();
    }
    
    private void actualizarPagina()
    {
        Integer numFilas = new HibernateTransaction<Integer>() {

            @Override
            public Integer run()
            {
                Query query = session.createSQLQuery("SELECT COUNT(*) FROM SESION WHERE ESPACIO_ID = :espacio_id");
                query.setInteger("espacio_id", espacio.getId());
                return (Integer) query.list().get(0);
            }
        
        }.execute();
        Double maxPaginasFP = numFilas.doubleValue() / maxFilas.doubleValue();
        maxPaginas = (int) Math.ceil(maxPaginasFP.doubleValue());
        paginaEtiqueta.setText(getTextoPagina());
    }
    
    private class ComprarEntradaAction implements Action
    {
        
        private Sesion sesion;
        private UBTicket ubticket;
        private Usuario usuario;
        
        public ComprarEntradaAction(UBTicket ubticket, Sesion sesion, Usuario usuario)
        {
            this.sesion = sesion;
            this.ubticket = ubticket;
            this.usuario = usuario;
        }

        @Override
        public void doAction()
        {
            Integer numEntradas = (Integer) usuario.Sesion("ENTRADAS_SESION_" + sesion.getId());
            if ( numEntradas == null )
            {
                numEntradas = 0;
            }
            
            if ( numEntradas < maxEntradasComprables ) {
                new HibernateTransaction() {

                    @Override
                    public Object run()
                    {
                        Entrada entrada = new Entrada();
                        entrada.setSesion(sesion);
                        entrada.setUsuario(usuario);

                        session.saveOrUpdate(entrada);

                        return null;
                    }
                }.execute();
                
                numEntradas++;
                usuario.Sesion("ENTRADAS_SESION_" + sesion.getId(), numEntradas);

                MessageBox.showMessageBox(ubticket.getGUIScreen(), "ATENCIÓN", "Ha comprado una entrada de '" + sesion.getEspectaculo().getTitulo() + "'.");
            } else {
                MessageBox.showMessageBox(ubticket.getGUIScreen(), "ATENCIÓN", "Se puede comprar sólo seis entradas para el espectáculo en la misma sesión!");
            }
        }
    
    }
    
}
