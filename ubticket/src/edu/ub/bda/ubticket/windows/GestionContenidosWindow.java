package edu.ub.bda.ubticket.windows;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import edu.ub.bda.UBTicket;
import edu.ub.bda.ubticket.beans.Categoria;
import edu.ub.bda.ubticket.beans.Espacio;
import edu.ub.bda.ubticket.beans.Espectaculo;
import edu.ub.bda.ubticket.beans.Usuario;

/**
 *
 * @author olopezsa13
 */
public class GestionContenidosWindow extends Window
{
    
    public GestionContenidosWindow(final UBTicket ubticket)
    {
        super("Gestionar contenidos");
        final Object self = this;
        
        addComponent(new Button("Categorías", new Action() {

            @Override
            public void doAction()
            {
                ubticket.getGUIScreen().showWindow(new GestorGenericoWindow(ubticket, Categoria.class));
            }
        
        }));
        
        addComponent(new Button("Espectáculos", new Action() {

            @Override
            public void doAction()
            {
                ubticket.getGUIScreen().showWindow(new GestorGenericoWindow(ubticket, Espectaculo.class));
            }
        
        }));
        
        addComponent(new Button("Espacios", new Action() {

            @Override
            public void doAction()
            {
                ubticket.getGUIScreen().showWindow(new GestorGenericoWindow(ubticket, Espacio.class));
            }
        
        }));
        
        addComponent(new Button("Usuarios", new Action() {

            @Override
            public void doAction()
            {
                ubticket.getGUIScreen().showWindow(new GestorGenericoWindow(ubticket, Usuario.class));
            }
        
        }));
              
        addComponent(new Button("Salir", new Action() {

            @Override
            public void doAction()
            {
                ((Window) self).close();
            }
        }));
    }
    
}

