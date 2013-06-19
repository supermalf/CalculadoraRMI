import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server abstraction layer.
 * 
 * @author Malf
 */
public class PadServer {
  /**
   * Initializes the server and make it ready to receive a connection though the
   * RMI protocol.
   * 
   * @throws Exception
   */
  private static void init() throws Exception {
    System.setProperty("java.security.policy", "policy.all");
    System.setSecurityManager(new RMISecurityManager());

    Registry registry = LocateRegistry.createRegistry(1099);
    registry.bind("pad", new PadText());
  }

  /**
   * Server main method.
   * 
   * @param args
   * @throws Exception
   */
  public static void main(final String args[]) throws Exception {
    init();
    System.out.println("Server ready!");
  }
}
