
package publicadores;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para DTNivel.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <pre>{@code
 * <simpleType name="DTNivel">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="ORO"/>
 *     <enumeration value="PLATA"/>
 *     <enumeration value="BRONCE"/>
 *     <enumeration value="PLATINO"/>
 *   </restriction>
 * </simpleType>
 * }</pre>
 * 
 */
@XmlType(name = "DTNivel")
@XmlEnum
public enum DTNivel {

    ORO,
    PLATA,
    BRONCE,
    PLATINO;

    public String value() {
        return name();
    }

    public static DTNivel fromValue(String v) {
        return valueOf(v);
    }

}
