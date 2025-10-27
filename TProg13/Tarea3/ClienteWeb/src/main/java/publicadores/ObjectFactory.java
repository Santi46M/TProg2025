
package publicadores;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the publicadores package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CategoriaYaExisteException_QNAME = new QName("http://publicadores/", "CategoriaYaExisteException");
    private final static QName _InstitucionYaExisteException_QNAME = new QName("http://publicadores/", "InstitucionYaExisteException");
    private final static QName _UsuarioNoExisteException_QNAME = new QName("http://publicadores/", "UsuarioNoExisteException");
    private final static QName _UsuarioTipoIncorrectoException_QNAME = new QName("http://publicadores/", "UsuarioTipoIncorrectoException");
    private final static QName _UsuarioYaExisteException_QNAME = new QName("http://publicadores/", "UsuarioYaExisteException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: publicadores
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DtEvento }
     * 
     * @return
     *     the new instance of {@link DtEvento }
     */
    public DtEvento createDtEvento() {
        return new DtEvento();
    }

    /**
     * Create an instance of {@link DtEdicion }
     * 
     * @return
     *     the new instance of {@link DtEdicion }
     */
    public DtEdicion createDtEdicion() {
        return new DtEdicion();
    }

    /**
     * Create an instance of {@link DtDatosUsuario }
     * 
     * @return
     *     the new instance of {@link DtDatosUsuario }
     */
    public DtDatosUsuario createDtDatosUsuario() {
        return new DtDatosUsuario();
    }

    /**
     * Create an instance of {@link CategoriaYaExisteException }
     * 
     * @return
     *     the new instance of {@link CategoriaYaExisteException }
     */
    public CategoriaYaExisteException createCategoriaYaExisteException() {
        return new CategoriaYaExisteException();
    }

    /**
     * Create an instance of {@link InstitucionYaExisteException }
     * 
     * @return
     *     the new instance of {@link InstitucionYaExisteException }
     */
    public InstitucionYaExisteException createInstitucionYaExisteException() {
        return new InstitucionYaExisteException();
    }

    /**
     * Create an instance of {@link UsuarioNoExisteException }
     * 
     * @return
     *     the new instance of {@link UsuarioNoExisteException }
     */
    public UsuarioNoExisteException createUsuarioNoExisteException() {
        return new UsuarioNoExisteException();
    }

    /**
     * Create an instance of {@link UsuarioTipoIncorrectoException }
     * 
     * @return
     *     the new instance of {@link UsuarioTipoIncorrectoException }
     */
    public UsuarioTipoIncorrectoException createUsuarioTipoIncorrectoException() {
        return new UsuarioTipoIncorrectoException();
    }

    /**
     * Create an instance of {@link UsuarioYaExisteException }
     * 
     * @return
     *     the new instance of {@link UsuarioYaExisteException }
     */
    public UsuarioYaExisteException createUsuarioYaExisteException() {
        return new UsuarioYaExisteException();
    }

    /**
     * Create an instance of {@link LocalDate }
     * 
     * @return
     *     the new instance of {@link LocalDate }
     */
    public LocalDate createLocalDate() {
        return new LocalDate();
    }

    /**
     * Create an instance of {@link DtRegistro }
     * 
     * @return
     *     the new instance of {@link DtRegistro }
     */
    public DtRegistro createDtRegistro() {
        return new DtRegistro();
    }

    /**
     * Create an instance of {@link DtTipoRegistro }
     * 
     * @return
     *     the new instance of {@link DtTipoRegistro }
     */
    public DtTipoRegistro createDtTipoRegistro() {
        return new DtTipoRegistro();
    }

    /**
     * Create an instance of {@link DtPatrocinio }
     * 
     * @return
     *     the new instance of {@link DtPatrocinio }
     */
    public DtPatrocinio createDtPatrocinio() {
        return new DtPatrocinio();
    }

    /**
     * Create an instance of {@link DtDatosUsuarioArray }
     * 
     * @return
     *     the new instance of {@link DtDatosUsuarioArray }
     */
    public DtDatosUsuarioArray createDtDatosUsuarioArray() {
        return new DtDatosUsuarioArray();
    }

    /**
     * Create an instance of {@link StringArray }
     * 
     * @return
     *     the new instance of {@link StringArray }
     */
    public StringArray createStringArray() {
        return new StringArray();
    }

    /**
     * Create an instance of {@link DtEvento.Categorias }
     * 
     * @return
     *     the new instance of {@link DtEvento.Categorias }
     */
    public DtEvento.Categorias createDtEventoCategorias() {
        return new DtEvento.Categorias();
    }

    /**
     * Create an instance of {@link DtEvento.Ediciones }
     * 
     * @return
     *     the new instance of {@link DtEvento.Ediciones }
     */
    public DtEvento.Ediciones createDtEventoEdiciones() {
        return new DtEvento.Ediciones();
    }

    /**
     * Create an instance of {@link DtEdicion.TiposRegistro }
     * 
     * @return
     *     the new instance of {@link DtEdicion.TiposRegistro }
     */
    public DtEdicion.TiposRegistro createDtEdicionTiposRegistro() {
        return new DtEdicion.TiposRegistro();
    }

    /**
     * Create an instance of {@link DtEdicion.Patrocinios }
     * 
     * @return
     *     the new instance of {@link DtEdicion.Patrocinios }
     */
    public DtEdicion.Patrocinios createDtEdicionPatrocinios() {
        return new DtEdicion.Patrocinios();
    }

    /**
     * Create an instance of {@link DtEdicion.Registros }
     * 
     * @return
     *     the new instance of {@link DtEdicion.Registros }
     */
    public DtEdicion.Registros createDtEdicionRegistros() {
        return new DtEdicion.Registros();
    }

    /**
     * Create an instance of {@link DtDatosUsuario.Registros }
     * 
     * @return
     *     the new instance of {@link DtDatosUsuario.Registros }
     */
    public DtDatosUsuario.Registros createDtDatosUsuarioRegistros() {
        return new DtDatosUsuario.Registros();
    }

    /**
     * Create an instance of {@link DtDatosUsuario.Ediciones }
     * 
     * @return
     *     the new instance of {@link DtDatosUsuario.Ediciones }
     */
    public DtDatosUsuario.Ediciones createDtDatosUsuarioEdiciones() {
        return new DtDatosUsuario.Ediciones();
    }

    /**
     * Create an instance of {@link DtDatosUsuario.Seguidores }
     * 
     * @return
     *     the new instance of {@link DtDatosUsuario.Seguidores }
     */
    public DtDatosUsuario.Seguidores createDtDatosUsuarioSeguidores() {
        return new DtDatosUsuario.Seguidores();
    }

    /**
     * Create an instance of {@link DtDatosUsuario.Seguidos }
     * 
     * @return
     *     the new instance of {@link DtDatosUsuario.Seguidos }
     */
    public DtDatosUsuario.Seguidos createDtDatosUsuarioSeguidos() {
        return new DtDatosUsuario.Seguidos();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CategoriaYaExisteException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link CategoriaYaExisteException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "CategoriaYaExisteException")
    public JAXBElement<CategoriaYaExisteException> createCategoriaYaExisteException(CategoriaYaExisteException value) {
        return new JAXBElement<>(_CategoriaYaExisteException_QNAME, CategoriaYaExisteException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InstitucionYaExisteException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InstitucionYaExisteException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "InstitucionYaExisteException")
    public JAXBElement<InstitucionYaExisteException> createInstitucionYaExisteException(InstitucionYaExisteException value) {
        return new JAXBElement<>(_InstitucionYaExisteException_QNAME, InstitucionYaExisteException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioNoExisteException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UsuarioNoExisteException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "UsuarioNoExisteException")
    public JAXBElement<UsuarioNoExisteException> createUsuarioNoExisteException(UsuarioNoExisteException value) {
        return new JAXBElement<>(_UsuarioNoExisteException_QNAME, UsuarioNoExisteException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioTipoIncorrectoException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UsuarioTipoIncorrectoException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "UsuarioTipoIncorrectoException")
    public JAXBElement<UsuarioTipoIncorrectoException> createUsuarioTipoIncorrectoException(UsuarioTipoIncorrectoException value) {
        return new JAXBElement<>(_UsuarioTipoIncorrectoException_QNAME, UsuarioTipoIncorrectoException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioYaExisteException }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UsuarioYaExisteException }{@code >}
     */
    @XmlElementDecl(namespace = "http://publicadores/", name = "UsuarioYaExisteException")
    public JAXBElement<UsuarioYaExisteException> createUsuarioYaExisteException(UsuarioYaExisteException value) {
        return new JAXBElement<>(_UsuarioYaExisteException_QNAME, UsuarioYaExisteException.class, null, value);
    }

}
