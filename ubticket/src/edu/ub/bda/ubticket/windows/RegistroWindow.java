package edu.ub.bda.ubticket.windows;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.DefaultBackgroundRenderer;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.PasswordBox;
import com.googlecode.lanterna.gui.component.TextBox;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import edu.ub.bda.UBTicket;
import edu.ub.bda.ubticket.beans.Usuario;
import edu.ub.bda.ubticket.utils.AutenticacionServicio;
import edu.ub.bda.ubticket.windows.gestioncontenidos.UsuarioEditorWindow;

/**
 *
 * @author olopezsa13
 */
public class RegistroWindow extends Window
{
    
    private TextBox login;
    private PasswordBox password;
    
    public RegistroWindow(final UBTicket ubticket)
    {
        super("Autenticación");
        
        this.setSoloWindow(true);
        
        Panel p = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);
        
        Panel left = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        left.addComponent(new Label("   LOGIN:"));
        left.addComponent(new Label("PASSWORD:"));
        
        Panel right = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        login = new TextBox("", 16);
        password = new PasswordBox("", 16);
        right.addComponent(login);
        right.addComponent(password);
        
        right.addComponent(new Button("OK", new Action() {

            @Override
            public void doAction()
            {
                try
                {
                    // Validate data
                    if ( AutenticacionServicio.Registrar(login.getText(), password.getText()) )
                    {
                        Usuario usuario = AutenticacionServicio.GetUsuario();
                        ((DefaultBackgroundRenderer) ubticket.getGUIScreen().getBackgroundRenderer()).setTitle("ubticket [" + usuario.getNombre() + "]");
                        ubticket.iniciarSesion();
                        return;
                    }
                }
                catch ( Exception ex )
                {
                    ex.printStackTrace();
                }
                
                MessageBox.showMessageBox(ubticket.getGUIScreen(), "Atención", "No existe el usuario o la contraseña es incorrecta.");
            }
        
        }));
        
        left.addComponent(new Button("SALIR", new Action() {

            @Override
            public void doAction()
            {
                ubticket.cerrar();
            }
        
        }));
        
        left.addComponent(new Button("AGREGAR", new Action() {

            @Override
            public void doAction()
            {
                ubticket.getGUIScreen().showWindow(new UsuarioEditorWindow(ubticket, null));
            }
         }));
        
        p.addComponent(left);
        p.addComponent(right);
        addComponent(p);
    }
    
    @Override
    public void onVisible()
    {
        login.setText("");
        password.setText("");
        
        this.setFocus(login);
    }
    
}
