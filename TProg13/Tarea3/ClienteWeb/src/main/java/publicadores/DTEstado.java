
package publicadores;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para DTEstado.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <pre>{@code
 * <simpleType name="DTEstado">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="Ingresada"/>
 *     <enumeration value="Aceptada"/>
 *     <enumeration value="Rechazada"/>
 *   </restriction>
 * </simpleType>
 * }</pre>
 * 
 */
@XmlType(name = "DTEstado")
@XmlEnum
public enum DTEstado {

    @XmlEnumValue("Ingresada")
    INGRESADA("Ingresada"),
    @XmlEnumValue("Aceptada")
    ACEPTADA("Aceptada"),
    @XmlEnumValue("Rechazada")
    RECHAZADA("Rechazada");
    private final String value;

    DTEstado(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DTEstado fromValue(String v) {
        for (DTEstado c: DTEstado.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
