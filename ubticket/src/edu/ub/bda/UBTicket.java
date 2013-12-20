package edu.ub.bda;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.DefaultBackgroundRenderer;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import edu.ub.bda.ubticket.windows.ComprarEntradasWindow;
import edu.ub.bda.ubticket.windows.GestionContenidosWindow;
import edu.ub.bda.ubticket.windows.GestorEntradasWindow;
import edu.ub.bda.ubticket.windows.MenuPrincipalWindow;
import edu.ub.bda.ubticket.windows.RegistroWindow;
import edu.ub.bda.ubticket.windows.VendidasViewWindow;
import java.nio.charset.Charset;

/**
 * 
 * @author olopezsa13
 */
public class UBTicket
{
    
    private boolean autenticacion = false;
    
    private Terminal terminal;
    private GUIScreen gui;
    private Screen screen;
    
    private RegistroWindow registroWindow;
    private MenuPrincipalWindow menuPrincipalWindow;
    private GestionContenidosWindow gestionContenidosWindow;
    private ComprarEntradasWindow comprarEntradasWindow;
    private GestorEntradasWindow gestorEntradasWindow;
    private VendidasViewWindow vendidasViewWindow;
    
    public UBTicket(boolean activarAutenticacion)
    {
        this.autenticacion = activarAutenticacion;
        
        terminal = TerminalFacade.createTerminal(System.in, System.out, Charset.forName("UTF8"));
        gui = TerminalFacade.createGUIScreen(terminal);
        screen = gui.getScreen();
        
        ((DefaultBackgroundRenderer) gui.getBackgroundRenderer()).setTitle("ubticket");
        
        inicializarComponentes();
        
        if ( gui == null )
        {
            System.err.println("OHNOES");
            return;
        }
        
        screen.startScreen();

        if ( autenticacion )
        {
            gui.showWindow(registroWindow);
        }
        else
        {
            gui.showWindow(menuPrincipalWindow);
        }

        screen.stopScreen();
    }
    
    private void inicializarComponentes()
    {
         registroWindow = new RegistroWindow(this);
         menuPrincipalWindow = new MenuPrincipalWindow(this);
         gestionContenidosWindow = new GestionContenidosWindow(this);
         comprarEntradasWindow = new ComprarEntradasWindow(this);
         gestorEntradasWindow = new GestorEntradasWindow();
         vendidasViewWindow= new VendidasViewWindow();
    }
    
    public void iniciarSesion()
    {
        registroWindow.close();
        gui.invalidate();
        gui.showWindow(menuPrincipalWindow);
    }
    
    public void gestionarContenidos()
    {
        gui.showWindow(gestionContenidosWindow);
    }
    
    public void comprarEntradas()
    {
        gui.showWindow(comprarEntradasWindow, GUIScreen.Position.CENTER);
    }
    
    public void gestionarEntradas()
    {
        gui.showWindow(gestorEntradasWindow, GUIScreen.Position.CENTER);
    }
    
    public void gestionarVendidas()
    {
        gui.showWindow(vendidasViewWindow, GUIScreen.Position.CENTER);
    }
    
    public void cerrarSesion()
    {
        ((DefaultBackgroundRenderer) gui.getBackgroundRenderer()).setTitle("ubticket");
        menuPrincipalWindow.close();
        gui.invalidate();
        gui.showWindow(registroWindow);
    }
    
    public void cerrar()
    {
        gui.getActiveWindow().close();
    }
    
    public GUIScreen getGUIScreen()
    {
        return gui;
    }
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args)
    {
        new UBTicket(true);
    }

}
