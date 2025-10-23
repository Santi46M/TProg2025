/**
 * PublicadorUsuarioServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4.1-SNAPSHOT Nov 07, 2023 (07:57:58 UTC) WSDL2Java emitter.
 */

package publicadores;

public class PublicadorUsuarioServiceLocator extends org.apache.axis.client.Service implements publicadores.PublicadorUsuarioService {

    public PublicadorUsuarioServiceLocator() {
    }


    public PublicadorUsuarioServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PublicadorUsuarioServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PublicadorUsuarioPort
    private java.lang.String PublicadorUsuarioPort_address = "http://localhost:8090/publicadorUsuario";

    public java.lang.String getPublicadorUsuarioPortAddress() {
        return PublicadorUsuarioPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PublicadorUsuarioPortWSDDServiceName = "PublicadorUsuarioPort";

    public java.lang.String getPublicadorUsuarioPortWSDDServiceName() {
        return PublicadorUsuarioPortWSDDServiceName;
    }

    public void setPublicadorUsuarioPortWSDDServiceName(java.lang.String name) {
        PublicadorUsuarioPortWSDDServiceName = name;
    }

    public publicadores.PublicadorUsuario getPublicadorUsuarioPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PublicadorUsuarioPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPublicadorUsuarioPort(endpoint);
    }

    public publicadores.PublicadorUsuario getPublicadorUsuarioPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            publicadores.PublicadorUsuarioPortBindingStub _stub = new publicadores.PublicadorUsuarioPortBindingStub(portAddress, this);
            _stub.setPortName(getPublicadorUsuarioPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPublicadorUsuarioPortEndpointAddress(java.lang.String address) {
        PublicadorUsuarioPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (publicadores.PublicadorUsuario.class.isAssignableFrom(serviceEndpointInterface)) {
                publicadores.PublicadorUsuarioPortBindingStub _stub = new publicadores.PublicadorUsuarioPortBindingStub(new java.net.URL(PublicadorUsuarioPort_address), this);
                _stub.setPortName(getPublicadorUsuarioPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("PublicadorUsuarioPort".equals(inputPortName)) {
            return getPublicadorUsuarioPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://publicadores/", "PublicadorUsuarioService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://publicadores/", "PublicadorUsuarioPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PublicadorUsuarioPort".equals(portName)) {
            setPublicadorUsuarioPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
