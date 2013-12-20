package edu.ub.bda.ubticket.windows;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import edu.ub.bda.UBTicket;
import edu.ub.bda.ubticket.beans.Usuario;
import edu.ub.bda.ubticket.utils.AutenticacionServicio;

/**
 *
 * @author olopezsa13
 */
public class MenuPrincipalWindow extends Window {

    private Button gestionarContenidosButton;
    private Button vendidasViewWindowButton;

    public MenuPrincipalWindow(final UBTicket ubticket) {
        super("Menú principal");

        this.setSoloWindow(true);

        addComponent(new Button("Comprar entradas", new Action() {
            @Override
            public void doAction() {
                ubticket.comprarEntradas();
            }
        }));

        addComponent(new Button("Ver entradas compradas", new Action() {
            @Override
            public void doAction() {
                ubticket.gestionarEntradas();
            }
        }));

        gestionarContenidosButton = new Button("Gestionar contenidos", new Action() {
            @Override
            public void doAction() {
                ubticket.gestionarContenidos();
            }
        });

        addComponent(gestionarContenidosButton);

        vendidasViewWindowButton = new Button("Control de Ventas", new Action() {
            @Override
            public void doAction() {
                ubticket.gestionarVendidas();
            }
        });

        addComponent(vendidasViewWindowButton);

        addComponent(new Button("Cerrar sesión", new Action() {
            @Override
            public void doAction() {
                ubticket.cerrarSesion();
            }
        }));

        addComponent(new Button("Salir", new Action() {
            @Override
            public void doAction() {
                ubticket.cerrar();
            }
        }));
    }

    @Override
    public void onVisible() {
        Usuario usuario = AutenticacionServicio.GetUsuario();
        gestionarContenidosButton.setVisible(usuario.getTipo_usuario().equals(Usuario.Tipos.ADMINISTRADOR.toString()));
        vendidasViewWindowButton.setVisible(usuario.getTipo_usuario().equals(Usuario.Tipos.ADMINISTRADOR.toString()));
    }
}