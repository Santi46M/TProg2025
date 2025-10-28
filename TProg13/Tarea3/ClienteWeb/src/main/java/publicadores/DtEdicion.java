
package publicadores;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para dtEdicion complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>{@code
 * <complexType name="dtEdicion">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="sigla" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="fechaInicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="fechaFin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="fechaAlta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="organizador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="ciudad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="pais" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="imagen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="video" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="estado" type="{http://publicadores/}DTEstado" minOccurs="0"/>
 *         <element name="tiposRegistro" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="tipoRegistro" type="{http://publicadores/}dtTipoRegistro" maxOccurs="unbounded" minOccurs="0"/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <element name="patrocinios" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="patrocinio" type="{http://publicadores/}dtPatrocinio" maxOccurs="unbounded" minOccurs="0"/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <element name="registros" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="registro" type="{http://publicadores/}dtRegistro" maxOccurs="unbounded" minOccurs="0"/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <element name="evento" type="{http://publicadores/}dtEvento" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dtEdicion", propOrder = {
    "nombre",
    "sigla",
    "fechaInicio",
    "fechaFin",
    "fechaAlta",
    "organizador",
    "ciudad",
    "pais",
    "imagen",
    "video",
    "estado",
    "tiposRegistro",
    "patrocinios",
    "registros",
    "evento"
})
public class DtEdicion {

    protected String nombre;
    protected String sigla;
    protected String fechaInicio;
    protected String fechaFin;
    protected String fechaAlta;
    protected String organizador;
    protected String ciudad;
    protected String pais;
    protected String imagen;
    protected String video;
    @XmlSchemaType(name = "string")
    protected DTEstado estado;
    protected DtEdicion.TiposRegistro tiposRegistro;
    protected DtEdicion.Patrocinios patrocinios;
    protected DtEdicion.Registros registros;
    protected DtEvento evento;

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad sigla.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * Define el valor de la propiedad sigla.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSigla(String value) {
        this.sigla = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaInicio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Define el valor de la propiedad fechaInicio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaInicio(String value) {
        this.fechaInicio = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaFin.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaFin() {
        return fechaFin;
    }

    /**
     * Define el valor de la propiedad fechaFin.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaFin(String value) {
        this.fechaFin = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaAlta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Define el valor de la propiedad fechaAlta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaAlta(String value) {
        this.fechaAlta = value;
    }

    /**
     * Obtiene el valor de la propiedad organizador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganizador() {
        return organizador;
    }

    /**
     * Define el valor de la propiedad organizador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganizador(String value) {
        this.organizador = value;
    }

    /**
     * Obtiene el valor de la propiedad ciudad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Define el valor de la propiedad ciudad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCiudad(String value) {
        this.ciudad = value;
    }

    /**
     * Obtiene el valor de la propiedad pais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPais() {
        return pais;
    }

    /**
     * Define el valor de la propiedad pais.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPais(String value) {
        this.pais = value;
    }

    /**
     * Obtiene el valor de la propiedad imagen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImagen() {
        return imagen;
    }

    /**
     * Define el valor de la propiedad imagen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImagen(String value) {
        this.imagen = value;
    }

    /**
     * Obtiene el valor de la propiedad video.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVideo() {
        return video;
    }

    /**
     * Define el valor de la propiedad video.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVideo(String value) {
        this.video = value;
    }

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link DTEstado }
     *     
     */
    public DTEstado getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link DTEstado }
     *     
     */
    public void setEstado(DTEstado value) {
        this.estado = value;
    }

    /**
     * Obtiene el valor de la propiedad tiposRegistro.
     * 
     * @return
     *     possible object is
     *     {@link DtEdicion.TiposRegistro }
     *     
     */
    public DtEdicion.TiposRegistro getTiposRegistro() {
        return tiposRegistro;
    }

    /**
     * Define el valor de la propiedad tiposRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link DtEdicion.TiposRegistro }
     *     
     */
    public void setTiposRegistro(DtEdicion.TiposRegistro value) {
        this.tiposRegistro = value;
    }

    /**
     * Obtiene el valor de la propiedad patrocinios.
     * 
     * @return
     *     possible object is
     *     {@link DtEdicion.Patrocinios }
     *     
     */
    public DtEdicion.Patrocinios getPatrocinios() {
        return patrocinios;
    }

    /**
     * Define el valor de la propiedad patrocinios.
     * 
     * @param value
     *     allowed object is
     *     {@link DtEdicion.Patrocinios }
     *     
     */
    public void setPatrocinios(DtEdicion.Patrocinios value) {
        this.patrocinios = value;
    }

    /**
     * Obtiene el valor de la propiedad registros.
     * 
     * @return
     *     possible object is
     *     {@link DtEdicion.Registros }
     *     
     */
    public DtEdicion.Registros getRegistros() {
        return registros;
    }

    /**
     * Define el valor de la propiedad registros.
     * 
     * @param value
     *     allowed object is
     *     {@link DtEdicion.Registros }
     *     
     */
    public void setRegistros(DtEdicion.Registros value) {
        this.registros = value;
    }

    /**
     * Obtiene el valor de la propiedad evento.
     * 
     * @return
     *     possible object is
     *     {@link DtEvento }
     *     
     */
    public DtEvento getEvento() {
        return evento;
    }

    /**
     * Define el valor de la propiedad evento.
     * 
     * @param value
     *     allowed object is
     *     {@link DtEvento }
     *     
     */
    public void setEvento(DtEvento value) {
        this.evento = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="patrocinio" type="{http://publicadores/}dtPatrocinio" maxOccurs="unbounded" minOccurs="0"/>
     *       </sequence>
     *     </restriction>
     *   </complexContent>
     * </complexType>
     * }</pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "patrocinio"
    })
    public static class Patrocinios {

        protected List<DtPatrocinio> patrocinio;

        /**
         * Gets the value of the patrocinio property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a {@code set} method for the patrocinio property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPatrocinio().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DtPatrocinio }
         * 
         * 
         * @return
         *     The value of the patrocinio property.
         */
        public List<DtPatrocinio> getPatrocinio() {
            if (patrocinio == null) {
                patrocinio = new ArrayList<>();
            }
            return this.patrocinio;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="registro" type="{http://publicadores/}dtRegistro" maxOccurs="unbounded" minOccurs="0"/>
     *       </sequence>
     *     </restriction>
     *   </complexContent>
     * </complexType>
     * }</pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "registro"
    })
    public static class Registros {

        protected List<DtRegistro> registro;

        /**
         * Gets the value of the registro property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a {@code set} method for the registro property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getRegistro().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DtRegistro }
         * 
         * 
         * @return
         *     The value of the registro property.
         */
        public List<DtRegistro> getRegistro() {
            if (registro == null) {
                registro = new ArrayList<>();
            }
            return this.registro;
        }

    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="tipoRegistro" type="{http://publicadores/}dtTipoRegistro" maxOccurs="unbounded" minOccurs="0"/>
     *       </sequence>
     *     </restriction>
     *   </complexContent>
     * </complexType>
     * }</pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "tipoRegistro"
    })
    public static class TiposRegistro {

        protected List<DtTipoRegistro> tipoRegistro;

        /**
         * Gets the value of the tipoRegistro property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a {@code set} method for the tipoRegistro property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTipoRegistro().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link DtTipoRegistro }
         * 
         * 
         * @return
         *     The value of the tipoRegistro property.
         */
        public List<DtTipoRegistro> getTipoRegistro() {
            if (tipoRegistro == null) {
                tipoRegistro = new ArrayList<>();
            }
            return this.tipoRegistro;
        }

    }

}
