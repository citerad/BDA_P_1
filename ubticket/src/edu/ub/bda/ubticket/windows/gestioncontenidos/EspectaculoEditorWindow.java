package edu.ub.bda.ubticket.windows.gestioncontenidos;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.TextBox;
import com.googlecode.lanterna.gui.dialog.ListSelectDialog;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import edu.ub.bda.UBTicket;
import edu.ub.bda.ubticket.beans.Categoria;
import edu.ub.bda.ubticket.beans.Espectaculo;
import edu.ub.bda.ubticket.utils.HibernateTransaction;
import edu.ub.bda.ubticket.windows.GestorGenericoWindow;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author olopezsa13
 */
public class EspectaculoEditorWindow extends Window
{
    
    private final Espectaculo espectaculo;
    private final GestorGenericoWindow gestor;
    private final UBTicket ubticket;
    private TextBox tituloTextBox;
    private TextBox descripcionTextArea;
    private Button categoriaButton;

    public EspectaculoEditorWindow(final UBTicket ubticket, GestorGenericoWindow gestor)
    {
        super("Crear [Espectaculo]");
        
        espectaculo = new Espectaculo();
        this.gestor = gestor;
        this.ubticket = ubticket;
        inicializarComponentes();
    }
    
    public EspectaculoEditorWindow(final UBTicket ubticket, GestorGenericoWindow gestor, Espectaculo espectaculo)
    {
        super("Editar [Espectaculo][" + espectaculo.getId().toString() + "]");
        
        this.espectaculo = espectaculo;
        this.gestor = gestor;
        this.ubticket = ubticket;
        inicializarComponentes();
    }
    
    private void inicializarComponentes()
    {
        final Object self = this;
        String categoriaBotonTexto = "Seleccionar categoría";
        
        Panel left = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        left.addComponent(new Label("     TÍTULO:"));
        left.addComponent(new Label("DESCRIPCIÓN:"));
        left.addComponent(new Label("  CATEGORÍA:"));
        
        Panel right = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        tituloTextBox = new TextBox(espectaculo.getTitulo(), 50);
        descripcionTextArea = new TextBox(espectaculo.getDescripcion(), 50);
        
        if ( espectaculo.getCategoria() != null )
        {
            categoriaBotonTexto = espectaculo.getCategoria().getNombre();
        }
        categoriaButton = new Button(categoriaBotonTexto, new Action() {

            @Override
            public void doAction()
            {
                List<Categoria> categorias = new HibernateTransaction<List<Categoria>>() {

                    @Override
                    public List<Categoria> run()
                    {
                        Query query = session.createQuery("from Categoria");
                        return (List<Categoria>) query.list();
                    }

                }.execute();
                
                Categoria[] vector = new Categoria[categorias.size()];
                categorias.toArray(vector);
                Categoria categoria = ListSelectDialog.showDialog(ubticket.getGUIScreen(), "ATENCIÓN", "Seleccione la categoría:", vector);
                if ( categoria != null )
                {
                    espectaculo.setCategoria(categoria);
                    categoriaButton.setText(espectaculo.getCategoria().getNombre());
                }
            }
        
        });
        
        right.addComponent(tituloTextBox);
        right.addComponent(descripcionTextArea);
        right.addComponent(categoriaButton);
        
        Panel editor = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
        editor.addComponent(left);
        editor.addComponent(right);
        
        addComponent(editor);
        
        Panel buttons = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
        buttons.addComponent(new Button("GUARDAR", new Action() {

            @Override
            public void doAction()
            {
                espectaculo.setTitulo(tituloTextBox.getText());
                espectaculo.setDescripcion(descripcionTextArea.getText());
                
                if ( espectaculo.getCategoria() == null )
                {
                        MessageBox.showMessageBox(ubticket.getGUIScreen(), "Atención", "Se ha de asignar una categoría de espectáculo.");
                        return;
                }
                
                new HibernateTransaction<Object>() {

                    @Override
                    public Object run()
                    {
                        session.saveOrUpdate(espectaculo);
                        return null;
                    }
                
                }.execute();
                
                gestor.actualizarTabla();
                
                ((Window) self).close();
            }
        
        }));
        
        buttons.addComponent(new Button("CANCELAR", new Action() {

            @Override
            public void doAction()
            {
                ((Window) self).close();
            }
        
        }));
        
        addComponent(buttons);
    }

}
