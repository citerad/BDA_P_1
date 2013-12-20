package edu.ub.bda.ubticket.beans;

/**
 * Esta clase define un espacio
 *
 * @author domenicocitera
 */
public class Espacio {

    private Integer id;
    private String nombre;
    private Integer aforo;
    private String telefono;
    private String email;
    private String direccion;
    private Float longitud;
    private Float latitud;

    public Espacio() {
        aforo = new Integer(0);
        longitud = new Float(0.0);
        latitud = new Float(0.0);
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }
    
    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * @return the direccion
     */
    public String getDireccion() {
        return direccion;
    }
    
    /**
     * @param direccion the direccion to set
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * @return the telefono
     */
    public String getTelefono() {
        return telefono;
    }
    
    /**
     * @param telefono the telefono to set
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * @return the aforo
     */
    public Integer getAforo() {
        return aforo;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param aforo the aforo to set
     */
    public void setAforo(Integer aforo) {
        this.aforo = aforo;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the longitud
     */
    public Float getLongitud() {
        return longitud;
    }

    /**
     * @return the latitud
     */
    public Float getLatitud() {
        return latitud;
    }

    /**
     * @param longitud the longitud to set
     */
    public void setLongitud(Float longitud) {
        this.longitud = longitud;
    }

    /**
     * @param latitud the latitud to set
     */
    public void setLatitud(Float latitud) {
        this.latitud = latitud;
    }
    
    @Override
    public String toString()
    {
        return "" + id.toString() + ". " + nombre;
    }
    
    
    /**
     * @copiar un objeto Espacio
     */
    public void copyFrom(Espacio espacio)
    {
        id = espacio.getId();
        nombre = espacio.getNombre();
        aforo = espacio.getAforo();
        telefono = espacio.getTelefono();
        direccion = espacio.getDireccion();
        email = espacio.getEmail();
        longitud = espacio.getLongitud();
        latitud = espacio.getLatitud();
    }

}
