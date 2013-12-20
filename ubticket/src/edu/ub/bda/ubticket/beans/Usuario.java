package edu.ub.bda.ubticket.beans;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase define el usuario.
 *
 * @author domenicocitera
 */
public class Usuario {
    
    public static enum Tipos { ADMINISTRADOR, CLIENTE };

    private Integer id;
    private String login;
    private String password;
    private String nombre;
    private Timestamp fecha_alta;
    private Timestamp fecha_ultima_compra;
    private String tipo_usuario;
    
    private Map<String, Object> metadatos = new HashMap<>();

    public Usuario() {
        login = "";
        password = "";
        nombre = "";
       
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the fecha_alta
     */
    public Timestamp getFecha_alta() {
        return fecha_alta;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @param fecha_alta the fecha_alta to set
     */
    public void setFecha_alta(Timestamp fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    /**
     * @return the tipo_usuario
     */
    public String getTipo_usuario() {
        return tipo_usuario;
    }

    /**
     * @param aTipo_usuario the tipo_usuario to set
     */
    public void setTipo_usuario(String aTipo_usuario) {
        tipo_usuario = aTipo_usuario;
    }

    /**
     * @return the fecha_ultima_compra
     */
    public Timestamp getFecha_ultima_compra() {
        return fecha_ultima_compra;
    }

    /**
     * @param fecha_ultima_compra the fecha_ultima_compra to set
     */
    public void setFecha_ultima_compra(Timestamp fecha_ultima_compra) {
        this.fecha_ultima_compra = fecha_ultima_compra;
    }
    
    /**
     * 
     * @param clave
     * @return 
     */
    public Object Sesion(String clave)
    {
        //System.out.print(id.toString() + "." +clave + " -> ");
        //System.out.println(metadatos.containsKey(clave));
        return metadatos.get(clave);
    }
    
    /**
     * 
     * @param clave
     * @param valor 
     */
    public void Sesion(String clave, Object valor)
    {
        metadatos.put(clave, valor);
    }
    
    public void vaciarDatosSesion()
    {
        metadatos.clear();
    }
    
    public void printSesion()
    {
        for ( Object o : metadatos.keySet() )
        {
            String clave = (String) o;
            System.out.print(id.toString() + "." +clave + " -> ");
            System.out.println(metadatos.get(clave));
        }
    }

}
