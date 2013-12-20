package edu.ub.bda.ubticket.windows;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Table;
import edu.ub.bda.ubticket.beans.Sesion;
import edu.ub.bda.ubticket.utils.HibernateTransaction;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author domenicocitera
 */
public class VendidasViewWindow extends Window {

    private Panel todoPanel;
    private Panel tablaPanel;
    private Table tabla;
    private Label paginaEtiqueta;
    private Integer maxFilas = 15;
    private Integer pagina = 0;
    private Integer maxPaginas = 0;

    public VendidasViewWindow() {
        super("Ver las ventas");

        todoPanel = new Panel(new Border.Invisible(), Panel.Orientation.VERTICAL);
        todoPanel.setVisible(false);

        tablaPanel = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);

        Panel paginadorPanel = new Panel(new Border.Invisible(), Panel.Orientation.HORISONTAL);

        paginadorPanel.addComponent(new Button("<--", new Action() {
            @Override
            public void doAction() {
                if (pagina > 0) {
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
            public void doAction() {
                if ((pagina + 1) < maxPaginas) {
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
            public void doAction() {
                todoPanel.setVisible(false);

                if (tabla != null) {
                    pagina = 0;
                    tabla.removeAllRows();
                }

                close();
            }
        }));
    }

    @Override
    public void onVisible() {
        actualizarTabla();
    }

    private void actualizarTabla() {
        actualizarPagina();

        List<Sesion> list = new HibernateTransaction<List<Sesion>>() {
            @Override
            public List<Sesion> run() {
                Criteria cQuery = session.createCriteria(Sesion.class);
                cQuery.add(Restrictions.gt("entradas_vendidas", 0));
                cQuery.setFirstResult(pagina * maxFilas);
                cQuery.setMaxResults(maxFilas);
                return (List<Sesion>) cQuery.list();
            }
        }.execute();

        if (list != null && list.size() > 0) {
            boolean addComponent = false;
            if (tabla == null) {
                tabla = new Table(5, "Ventas");
                addComponent = true;
            } else {
                tabla.removeAllRows();
            }

            for (Sesion s : list) {
                tabla.addRow(new Label(s.getId().toString()),
                        new Label(s.getEspectaculo().toString()),
                        new Label(s.getEspacio().toString()),
                        new Label(s.getFecha_inicio().toString()),
                        new Label(s.getEntradas_vendidas().toString()));
            }

            if (addComponent) {
                tablaPanel.addComponent(tabla);
            }
            todoPanel.setVisible(true);
        }
    }

    private String getTextoPagina() {
        Integer pag = pagina + 1;
        return pag.toString() + " / " + maxPaginas.toString();
    }

    private void actualizarPagina() {
        Integer numFilas = new HibernateTransaction<Integer>() {
            @Override
            public Integer run() {
                Query query = session.createSQLQuery("SELECT COUNT(*) FROM SESION WHERE `ENTRADAS_VENDIDAS` > 0");
                return (Integer) query.list().get(0);
            }
        }.execute();
        Double maxPaginasFP = numFilas.doubleValue() / maxFilas.doubleValue();
        maxPaginas = (int) Math.ceil(maxPaginasFP.doubleValue());
        paginaEtiqueta.setText(getTextoPagina());
    }
}
