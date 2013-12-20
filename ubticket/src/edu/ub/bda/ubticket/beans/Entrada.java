package edu.ub.bda.ubticket.beans;

/**
 * Esta clase define la entrada de una sesion y de un usuario
 *
 * @author domenicocitera
 */
public class Entrada {

    private Integer id;
    private Integer fila;
    private Integer asiento;
    private Sesion sesion;
    private Usuario usuario;

    public Entrada() {
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the fila
     */
    public Integer getFila() {
        return fila;
    }

    /**
     * @return the asiento
     */
    public Integer getAsiento() {
        return asiento;
    }

    /**
     * @return the sesion
     */
    public Sesion getSesion() {
        return sesion;
    }

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param fila the fila to set
     */
    public void setFila(Integer fila) {
        this.fila = fila;
    }

    /**
     * @param asiento the asiento to set
     */
    public void setAsiento(Integer asiento) {
        this.asiento = asiento;
    }

    /**
     * @param sesion the sesion to set
     */
    public void setSesion(Sesion sesion) {
        this.sesion = sesion;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
