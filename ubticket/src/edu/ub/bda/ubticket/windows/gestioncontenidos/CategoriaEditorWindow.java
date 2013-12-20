package edu.ub.bda.ubticket.windows.gestioncontenidos;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.TextBox;
import edu.ub.bda.ubticket.beans.Categoria;
import edu.ub.bda.ubticket.utils.HibernateTransaction;
import edu.ub.bda.ubticket.windows.GestorGenericoWindow;

/**
 *
 * @author olopezsa13
 */
public class CategoriaEditorWindow extends Window
{
    
    private final Categoria categoria;
    private final GestorGenericoWindow gestor;
    private TextBox nombreTextBox;

    public CategoriaEditorWindow(GestorGenericoWindow gestor)
    {
        super("Crear [Categoria]");
        
        categoria = new Categoria();
        this.gestor = gestor;
        inicializarComponentes();
    }
    
    public CategoriaEditorWindow(GestorGenericoWindow gestor, Categoria categoria)
    {
        super("Editar [Categoria][" + categoria.getId().toString() + "]");
        
        this.categoria = categoria;
        this.gestor = gestor;
        inicializarComponentes();
    }
    
    private void inicializarComponentes()
    {
        final Object self = this;
        
        Panel left = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        left.addComponent(new Label("NOMBRE:"));        
        
        Panel right = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        nombreTextBox = new TextBox(categoria.getNombre());
        right.addComponent(nombreTextBox);
        
        Panel editor = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
        editor.addComponent(left);
        editor.addComponent(right);
        
        addComponent(editor);
        
        Panel buttons = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
        buttons.addComponent(new Button("GUARDAR", new Action() {

            @Override
            public void doAction()
            {
                categoria.setNombre(nombreTextBox.getText());
                
                new HibernateTransaction<Object>() {

                    @Override
                    public Object run()
                    {
                        session.saveOrUpdate(categoria);
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
