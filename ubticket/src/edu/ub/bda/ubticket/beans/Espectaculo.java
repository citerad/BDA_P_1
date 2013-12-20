package edu.ub.bda.ubticket.beans;

/**
 *
 * @author olopezsa13
 */
public class Espectaculo
{

    private Integer id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;

    /**
     * @return El identificador del espectáculo
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @return El título del espectáculo
     */
    public String getTitulo()
    {
        return titulo;
    }

    /**
     * @return La descripción del espectáculo
     */
    public String getDescripcion()
    {
        return descripcion;
    }

    /**
     * @return La categoría del espectáculo
     */
    public Categoria getCategoria()
    {
        return categoria;
    }

    /**
     * @param id El identificador del espectáculo
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @param titulo El nuevo título que reemplazará el anterior
     */
    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    /**
     * @param descripcion La nueva descripción que reemplazará la anterior
     */
    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    /**
     * @param categoria La nueva categoría del espectáculo
     */
    public void setCategoria(Categoria categoria)
    {
        this.categoria = categoria;
    }
    
        @Override
    public String toString()
    {
        return "" + titulo;
    }
    
    
}
