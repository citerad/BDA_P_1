package edu.ub.bda.ubticket.beans;

import java.sql.Timestamp;

/**
 * Esta clase define la sesion de un espectaculo en un espacio
 *
 * @author domenicocitera
 */
public class Sesion {

    private Integer id;
    private Timestamp fecha_inicio;
    private Timestamp fecha_finalizacion;
    private Integer precio;
    private Integer entradas_vendidas;
    private Espectaculo espectaculo;
    private Espacio espacio;

    public Sesion() {
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the fecha_inicio
     */
    public Timestamp getFecha_inicio() {
        return fecha_inicio;
    }

    /**
     * @return the fecha_finalizacion
     */
    public Timestamp getFecha_finalizacion() {
        return fecha_finalizacion;
    }

    /**
     * @return the espectaculo
     */
    public Espectaculo getEspectaculo() {
        return espectaculo;
    }

    /**
     * @return the espacio
     */
    public Espacio getEspacio() {
        return espacio;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param fecha_inicio the fecha_inicio to set
     */
    public void setFecha_inicio(Timestamp fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    /**
     * @param fecha_finalizacion the fecha_finalizacion to set
     */
    public void setFecha_finalizacion(Timestamp fecha_finalizacion) {
        this.fecha_finalizacion = fecha_finalizacion;
    }

    /**
     * @param espectaculo the espectaculo to set
     */
    public void setEspectaculo(Espectaculo espectaculo) {
        this.espectaculo = espectaculo;
    }

    /**
     * @param espacio the espacio to set
     */
    public void setEspacio(Espacio espacio) {
        this.espacio = espacio;
    }

    /**
     * @return the precio
     */
    public Integer getPrecio() {
        return precio;
    }

    /**
     * @param precio the precio to set
     */
    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    /**
     * @return the endtradas_vendidas
     */
    public Integer getEntradas_vendidas() {
        return entradas_vendidas;
    }

    /**
     * @param endtradas_vendidas the endtradas_vendidas to set
     */
    public void setEntradas_vendidas(Integer entradas_vendidas) {
        this.entradas_vendidas = entradas_vendidas;
    }

}
