package edu.ub.bda.ubticket.windows.gestioncontenidos;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.TextBox;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import edu.ub.bda.UBTicket;
import edu.ub.bda.ubticket.beans.Espacio;
import edu.ub.bda.ubticket.utils.HibernateTransaction;
import edu.ub.bda.ubticket.windows.GestorGenericoWindow;

/**
 *
 * @author olopezsa13
 */
public class EspacioEditorWindow extends Window
{
    
    private final Espacio espacio;
    private final GestorGenericoWindow gestor;
    private final UBTicket ubticket;
    private TextBox nombreTextBox;
    private TextBox aforoTextBox;
    private TextBox telefonoTextBox;
    private TextBox direccionTextBox;
    private TextBox emailTextBox;
    private TextBox longitudTextBox;
    private TextBox latitudTextBox;

    public EspacioEditorWindow(UBTicket ubticket, GestorGenericoWindow gestor)
    {
        super("Crear [Espacio]");
        
        espacio = new Espacio();
        this.gestor = gestor;
        this.ubticket = ubticket;
        inicializarComponentes();
    }
    
    public EspacioEditorWindow(UBTicket ubticket, GestorGenericoWindow gestor, Espacio categoria)
    {
        super("Editar [Espacio][" + categoria.getId().toString() + "]");
        
        this.espacio = categoria;
        this.gestor = gestor;
        this.ubticket = ubticket;
        inicializarComponentes();
    }
    
    private void inicializarComponentes()
    {
        final Object self = this;
        
        Panel left = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        left.addComponent(new Label("   NOMBRE:"));
        left.addComponent(new Label("    AFORO:"));
        left.addComponent(new Label(" TELÉFONO:"));
        left.addComponent(new Label("DIRECCIÓN:"));
        left.addComponent(new Label("    EMAIL:"));
        left.addComponent(new Label(" LONGITUD:"));
        left.addComponent(new Label("  LATITUD:"));
        
        Panel right = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        nombreTextBox = new TextBox(espacio.getNombre(), 50);
        aforoTextBox = new TextBox(espacio.getAforo().toString(), 6);
        telefonoTextBox = new TextBox(espacio.getTelefono(), 16);
        direccionTextBox = new TextBox(espacio.getDireccion(), 50);
        emailTextBox = new TextBox(espacio.getEmail(), 32);
        longitudTextBox = new TextBox(espacio.getLongitud().toString(), 16);
        latitudTextBox = new TextBox(espacio.getLatitud().toString(), 16);
        right.addComponent(nombreTextBox);
        right.addComponent(aforoTextBox);
        right.addComponent(telefonoTextBox);
        right.addComponent(direccionTextBox);
        right.addComponent(emailTextBox);
        right.addComponent(longitudTextBox);
        right.addComponent(latitudTextBox);
        
        
        Panel editor = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
        editor.addComponent(left);
        editor.addComponent(right);
        
        addComponent(editor);
        
        Panel buttons = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
        buttons.addComponent(new Button("GUARDAR", new Action() {

            @Override
            public void doAction()
            {
                try
                {
                    espacio.setNombre(nombreTextBox.getText());
                    espacio.setAforo(Integer.parseInt(aforoTextBox.getText()));
                    espacio.setTelefono(telefonoTextBox.getText());
                    espacio.setDireccion(direccionTextBox.getText());
                    espacio.setEmail(emailTextBox.getText());
                    espacio.setLatitud(Float.parseFloat(latitudTextBox.getText()));
                    espacio.setLongitud(Float.parseFloat(longitudTextBox.getText()));

                    new HibernateTransaction<Object>() {

                        @Override
                        public Object run()
                        {
                            session.saveOrUpdate(espacio);
                            return null;
                        }

                    }.execute();

                    gestor.actualizarTabla();

                    ((Window) self).close();
                }
                catch ( Exception ex )
                {
                    MessageBox.showMessageBox(ubticket.getGUIScreen(), "Atención", "No se ha podido guardar correctamente.");
                }
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
