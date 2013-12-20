package edu.ub.bda.ubticket.windows;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Table;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.gui.dialog.TextInputDialog;
import edu.ub.bda.UBTicket;
import edu.ub.bda.ubticket.beans.Categoria;
import edu.ub.bda.ubticket.beans.Espacio;
import edu.ub.bda.ubticket.beans.Espectaculo;
import edu.ub.bda.ubticket.beans.Usuario;
import edu.ub.bda.ubticket.utils.HibernateTransaction;
import edu.ub.bda.ubticket.windows.gestioncontenidos.CategoriaEditorWindow;
import edu.ub.bda.ubticket.windows.gestioncontenidos.EspacioEditorWindow;
import edu.ub.bda.ubticket.windows.gestioncontenidos.EspectaculoEditorWindow;
import edu.ub.bda.ubticket.windows.gestioncontenidos.UsuarioEditorWindow;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author olopezsa13
 */
public class GestorGenericoWindow extends Window
{
    
    private Class<?> claseEntidad;
    private Table tabla;
    private Panel tablaPanel;
    private Label paginaEtiqueta;
    
    private Integer maxFilas = 15;
    private Integer pagina = 0;
    private Integer maxPaginas = 0;

    public GestorGenericoWindow(final UBTicket ubticket, Class c)
    {
        super("Gestionar [" + c.getSimpleName() + "]");
        final Object self = this;
        
        claseEntidad = c;
        
        tablaPanel = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
        addComponent(tablaPanel);
        
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
        
        addComponent(paginadorPanel);
        
        Panel botonesPanel = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
        
        botonesPanel.addComponent(new Button("CREAR", new Action() {

            @Override
            public void doAction()
            {
                if ( claseEntidad == Categoria.class )
                {
                    ubticket.getGUIScreen().showWindow(new CategoriaEditorWindow((GestorGenericoWindow) self));
                }
                else if ( claseEntidad == Espectaculo.class )
                {
                    ubticket.getGUIScreen().showWindow(new EspectaculoEditorWindow(ubticket, (GestorGenericoWindow) self));
                }
                else if ( claseEntidad == Espacio.class )
                {
                    ubticket.getGUIScreen().showWindow(new EspacioEditorWindow(ubticket, (GestorGenericoWindow) self));
                }
                else if ( claseEntidad == Usuario.class )
               {
                    ubticket.getGUIScreen().showWindow(new UsuarioEditorWindow(ubticket, (GestorGenericoWindow) self));
                }
            }
        }));
        
        
        botonesPanel.addComponent(new Button("EDITAR", new Action() {
            
            final String nombreClaseEntidad = claseEntidad.getSimpleName();

            @Override
            public void doAction()
            {
                String input = TextInputDialog.showTextInputBox(ubticket.getGUIScreen(), "Atención", "¿Qué tabla desea editar?", "");
                
                if ( input != null && input.length() != 0 )
                {
                    try
                    {
                        final Integer i = Integer.parseInt(input);
                        
                        Object entidad = new HibernateTransaction<Object>() {

                            @Override
                            public Object run()
                            {
                                return session.byId(claseEntidad).load(i);
                            }

                        }.execute();
                        
                        if ( entidad != null )
                        {
                            if ( entidad instanceof Categoria )
                            {
                                ubticket.getGUIScreen().showWindow(new CategoriaEditorWindow((GestorGenericoWindow) self, (Categoria) entidad));
                            }
                            else if ( entidad instanceof Espectaculo )
                            {
                                ubticket.getGUIScreen().showWindow(new EspectaculoEditorWindow(ubticket, (GestorGenericoWindow) self, (Espectaculo) entidad));
                            }
                            else if ( entidad instanceof Espacio )
                            {
                                ubticket.getGUIScreen().showWindow(new EspacioEditorWindow(ubticket, (GestorGenericoWindow) self, (Espacio) entidad));
                            }
                            else if ( entidad instanceof Usuario )
                            {
                                ubticket.getGUIScreen().showWindow(new UsuarioEditorWindow(ubticket, (GestorGenericoWindow) self, (Usuario) entidad));
                            }
                        }
                        else
                        {
                            MessageBox.showMessageBox(ubticket.getGUIScreen(), "Atención", "No se ha encontrado ninguna entidad con tal identificador.");
                        }
                    }
                    catch ( NumberFormatException ex )
                    {
                        MessageBox.showMessageBox(ubticket.getGUIScreen(), "Atención", "Identificador inválido.");
                    }
                }
            }
        
        }));
        
        botonesPanel.addComponent(new Button("BORRAR", new Action() {
            
            final String nombreClaseEntidad = claseEntidad.getSimpleName();

            @Override
            public void doAction()
            {
                String input = TextInputDialog.showTextInputBox(ubticket.getGUIScreen(), "Atención", "¿Qué tabla desea eliminar?", "");
                
                if ( input != null && input.length() != 0 )
                {
                    try
                    {
                        final Integer i = Integer.parseInt(input);
                        
                        Integer entidadesBorradas = new HibernateTransaction<Integer>() {

                            @Override
                            public Integer run()
                            {
                                int entidadesBorradas = session.createQuery("DELETE " + nombreClaseEntidad + " e WHERE e.id = :idEntidad")
                                        .setString("idEntidad", i.toString())
                                        .executeUpdate();
                                return new Integer(entidadesBorradas);
                            }

                        }.execute();
                        
                        if ( entidadesBorradas.intValue() > 0 )
                        {
                            MessageBox.showMessageBox(ubticket.getGUIScreen(), "Información", "Se ha(n) borrado " + entidadesBorradas + " entrada(s).");
                            actualizarTabla();
                        }
                        else
                        {
                            MessageBox.showMessageBox(ubticket.getGUIScreen(), "Atención", "No se ha encontrado ninguna entidad con tal identificador.");
                        }
                    }
                    catch ( NumberFormatException ex )
                    {
                        MessageBox.showMessageBox(ubticket.getGUIScreen(), "Atención", "Identificador inválido.");
                    }
                }
            }
        
        }));
        
        botonesPanel.addComponent(new Button("CANCELAR", new Action() {

            @Override
            public void doAction()
            {
                ((Window) self).close();
            }
        
        }));
        
        addComponent(botonesPanel);
    }
    
    @Override
    public void onVisible()
    {
        actualizarTabla();
    }
    
    public void actualizarTabla()
    {
        final String nombreClaseEntidad = claseEntidad.getSimpleName();
        
        actualizarPagina();
        
        List<Object> list = new HibernateTransaction<List<Object>>() {

            @Override
            public List<Object> run()
            {
                Query query = session.createQuery("from " + nombreClaseEntidad);
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
                tabla = new Table(8, "Contenido");
                addComponent = true;
            }
            else
            {
                tabla.removeAllRows();
            }
            
            for ( Object objeto : list )
            {                
                if ( objeto instanceof Categoria )
                {
                    Categoria o = (Categoria) objeto;
                    tabla.addRow(new Label(o.getId().toString()), new Label(o.getNombre()));
                }
                else if ( objeto instanceof Espectaculo )
                {
                    Espectaculo o = (Espectaculo) objeto;
                    tabla.addRow(new Label(o.getId().toString()),
                            new Label(o.getTitulo()),
                            new Label(o.getCategoria().getNombre()));
                }
                else if ( objeto instanceof Espacio )
                {
                    Espacio o = (Espacio) objeto;
                    tabla.addRow(new Label(o.getId().toString()),
                            new Label(o.getNombre()),
                            new Label(o.getDireccion()));
                }
                else if ( objeto instanceof Usuario )
                {
                    Usuario o = (Usuario) objeto;
                    
                    if(o.getFecha_ultima_compra()!=null){
                         tabla.addRow(new Label(o.getId().toString()),
                            new Label(o.getLogin()),
                            new Label(o.getNombre()),
                            new Label(o.getFecha_alta().toString()),
                            new Label(o.getTipo_usuario()),
                            new Label(o.getFecha_ultima_compra().toString()));
                    }
                    else {
                         tabla.addRow(new Label(o.getId().toString()),
                            new Label(o.getLogin()),
                            new Label(o.getNombre()),
                            new Label(o.getFecha_alta().toString()),
                            new Label(o.getTipo_usuario()));
                    }

                }
            }
        
            if ( addComponent )
            {
                tablaPanel.addComponent(tabla);
            }
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
        final String nombreClaseEntidad = claseEntidad.getSimpleName();
        
        Long numFilas = new HibernateTransaction<Long>() {

            @Override
            public Long run()
            {
                return (Long) session.createQuery("select count(*) from " + nombreClaseEntidad).iterate().next();
            }
        
        }.execute();
        Double maxPaginasFP = numFilas.doubleValue() / maxFilas.doubleValue();
        maxPaginas = (int) Math.ceil(maxPaginasFP.doubleValue());
        paginaEtiqueta.setText(getTextoPagina());
    }
    
}

