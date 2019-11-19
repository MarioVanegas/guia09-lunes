/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ues.occ.edu.sv.ingenieria.prn335.guia09.client.boundary;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.primefaces.event.SelectEvent;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Sala;
import ues.occ.edu.sv.ingenieria.prn335.cineData.entity.Sucursal;

/**
 *
 * @author mario
 */
@Named(value = "sucursalBean")
@ViewScoped
public class SucursalBean implements Serializable {

    private List<Sucursal> listaSucursal;
    Client cliente;
    WebTarget wt;
    Integer first, range;
    private final static String URL = "http://localhost:8080/server/webresource/";
    private Integer id;
    private String nombre, direccion, contacto;
    private boolean activo;
    Sucursal selectSucursal = new Sucursal();
    private List<Sala> salasSelect;

    public SucursalBean() {
        this.cliente = ClientBuilder.newClient();
        this.wt = cliente.target(URL);
    }

    /**
     * METODO INIT para cargar los datos de la tabla sucursal.
     */
    @PostConstruct
    public void init() {
        if (range == null) {
            range = 100;
        }
        if (first == null) {
            first = 0;
        }
        Response response = this.wt.path("sucursal").queryParam("first", first).queryParam("pagesize", range).request().get();
        listaSucursal = response.readEntity(new GenericType<List<Sucursal>>() {
        });
        if (response.getStatus() == 404) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay datos o no se establecio la conexion"));
        }
    }

    /**
     * Se  busca el rango que se ingresa
     * @param ae
     */
    public void btnRangeHandler(ActionEvent ae) {
        init();
    }

    /**
     * en este metodo se persiste gracias a que se manda a llamar la entidad REST
     *
     * @param ae Accion de hacer click
     */
    public void btnAgregarHandler(ActionEvent ae) {
        FacesContext context = FacesContext.getCurrentInstance();
        Response respuesta = wt.path("/sucursal").request().post(Entity.json(new Sucursal(id, nombre, direccion, contacto, activo)));
        if (respuesta.getStatus() == 201) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK", "El registro ha sido creado con exito"));
        } else if (respuesta.getStatus() == 500) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El registro no ha sido creado"));
        }
        borrarCampos();
        init();
    }

    /**
     * Se cancelan los procesos.
     *
     * @param ae
     */
    public void btnCancelarHandler(ActionEvent ae) {
        borrarCampos();
        init();
    }

    public void borrarCampos() {
        this.id = null;
        this.nombre = null;
        this.direccion = null;
        this.contacto = null;
        this.activo = false;
    }

    /**
     * Select event para cuando se seleccione una sucursal muestre la sala
     *
     * @param se
     */
    public void salasSucursal(SelectEvent se) {
        FacesContext context = FacesContext.getCurrentInstance();;
        try {
            Response response = wt.path("sucursal/" + selectSucursal.getIdSucursal() + "/salas").request().get();
            salasSelect = response.readEntity(new GenericType<List<Sala>>() {
            });
            if (response.getStatus() == 404) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay datos o no se establecio la conexion"));
            }
        } catch (Exception e) {
            salasSelect = Collections.EMPTY_LIST;
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay datos o no se establecio la conexion"));
        }

    }

    public List<Sala> getSalasSelect() {
        return salasSelect;
    }

    public Sucursal getSelect() {
        return selectSucursal;
    }

    public void setSelect(Sucursal select) {
        this.selectSucursal = select;
    }

    public List<Sucursal> getLista() {
        return listaSucursal;
    }

    public void setLista(List<Sucursal> lista) {
        this.listaSucursal = lista;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFirst() {
        return first;
    }

    public void setFirst(Integer first) {
        this.first = first;
    }

    public Integer getRange() {
        return range;
    }

    public void setRange(Integer range) {
        this.range = range;
    }

}
