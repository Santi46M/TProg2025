package publicadores;

public class PublicadorUsuarioProxy implements publicadores.PublicadorUsuario {
  private String _endpoint = null;
  private publicadores.PublicadorUsuario publicadorUsuario = null;
  
  public PublicadorUsuarioProxy() {
    _initPublicadorUsuarioProxy();
  }
  
  public PublicadorUsuarioProxy(String endpoint) {
    _endpoint = endpoint;
    _initPublicadorUsuarioProxy();
  }
  
  private void _initPublicadorUsuarioProxy() {
    try {
      publicadorUsuario = (new publicadores.PublicadorUsuarioServiceLocator()).getPublicadorUsuarioPort();
      if (publicadorUsuario != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)publicadorUsuario)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)publicadorUsuario)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (publicadorUsuario != null)
      ((javax.xml.rpc.Stub)publicadorUsuario)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public publicadores.PublicadorUsuario getPublicadorUsuario() {
    if (publicadorUsuario == null)
      _initPublicadorUsuarioProxy();
    return publicadorUsuario;
  }
  
  public java.lang.String altaUsuario(java.lang.String arg0, java.lang.String arg1, java.lang.String arg2, java.lang.String arg3, java.lang.String arg4, java.lang.String arg5, publicadores.LocalDate arg6, java.lang.String arg7, boolean arg8, java.lang.String arg9, java.lang.String arg10) throws java.rmi.RemoteException{
    if (publicadorUsuario == null)
      _initPublicadorUsuarioProxy();
    return publicadorUsuario.altaUsuario(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
  }
  
  
}