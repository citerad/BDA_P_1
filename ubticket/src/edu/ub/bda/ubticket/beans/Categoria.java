package edu.ub.bda.ubticket.beans;

/**
 * Esta clase define la categoría de un espectáculo
 *
 * @author olopezsa13
 */
public class Categoria
{
    
    private Integer id;
    private String nombre;

    /**
     * @return El identificador de la categoría
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @return El nombre de la categoria
     */
    public String getNombre()
    {
        return nombre;
    }

    /**
     * @param id El identificador de la categoría
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @param nombre El nuevo nombre de la categoría a editar
     */
    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }
    
    @Override
    public String toString()
    {
        return "" + id.toString() + ". " + nombre;
    }
    
}
