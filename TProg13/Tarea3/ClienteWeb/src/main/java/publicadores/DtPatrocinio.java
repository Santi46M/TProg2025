
package publicadores;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para dtPatrocinio complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>{@code
 * <complexType name="dtPatrocinio">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="monto" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="fecha" type="{http://publicadores/}localDate" minOccurs="0"/>
 *         <element name="nivel" type="{http://publicadores/}DTNivel" minOccurs="0"/>
 *         <element name="cantRegistrosGratuitos" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="institucion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="siglaEdicion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="tipoRegistro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dtPatrocinio", propOrder = {
    "codigo",
    "monto",
    "fecha",
    "nivel",
    "cantRegistrosGratuitos",
    "institucion",
    "siglaEdicion",
    "tipoRegistro"
})
public class DtPatrocinio {

    protected String codigo;
    protected int monto;
    protected LocalDate fecha;
    @XmlSchemaType(name = "string")
    protected DTNivel nivel;
    protected int cantRegistrosGratuitos;
    protected String institucion;
    protected String siglaEdicion;
    protected String tipoRegistro;

    /**
     * Obtiene el valor de la propiedad codigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Define el valor de la propiedad codigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Obtiene el valor de la propiedad monto.
     * 
     */
    public int getMonto() {
        return monto;
    }

    /**
     * Define el valor de la propiedad monto.
     * 
     */
    public void setMonto(int value) {
        this.monto = value;
    }

    /**
     * Obtiene el valor de la propiedad fecha.
     * 
     * @return
     *     possible object is
     *     {@link LocalDate }
     *     
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Define el valor de la propiedad fecha.
     * 
     * @param value
     *     allowed object is
     *     {@link LocalDate }
     *     
     */
    public void setFecha(LocalDate value) {
        this.fecha = value;
    }

    /**
     * Obtiene el valor de la propiedad nivel.
     * 
     * @return
     *     possible object is
     *     {@link DTNivel }
     *     
     */
    public DTNivel getNivel() {
        return nivel;
    }

    /**
     * Define el valor de la propiedad nivel.
     * 
     * @param value
     *     allowed object is
     *     {@link DTNivel }
     *     
     */
    public void setNivel(DTNivel value) {
        this.nivel = value;
    }

    /**
     * Obtiene el valor de la propiedad cantRegistrosGratuitos.
     * 
     */
    public int getCantRegistrosGratuitos() {
        return cantRegistrosGratuitos;
    }

    /**
     * Define el valor de la propiedad cantRegistrosGratuitos.
     * 
     */
    public void setCantRegistrosGratuitos(int value) {
        this.cantRegistrosGratuitos = value;
    }

    /**
     * Obtiene el valor de la propiedad institucion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstitucion() {
        return institucion;
    }

    /**
     * Define el valor de la propiedad institucion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstitucion(String value) {
        this.institucion = value;
    }

    /**
     * Obtiene el valor de la propiedad siglaEdicion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiglaEdicion() {
        return siglaEdicion;
    }

    /**
     * Define el valor de la propiedad siglaEdicion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiglaEdicion(String value) {
        this.siglaEdicion = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRegistro() {
        return tipoRegistro;
    }

    /**
     * Define el valor de la propiedad tipoRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRegistro(String value) {
        this.tipoRegistro = value;
    }

}
