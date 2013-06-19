import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class Stress extends Thread {
  private static final int NUM_THREADS = 100;
  private static final int MAX_TESTS = 100;
  private static final int JOIN_TIMEOUT_MS = 1000;
  private static TextAnalyzer textAnalyser;
  private static Set<Stress> instances;
  private static int num_errors;
  private int n;
  private String s;
  private long id;

  private void checkIntegrity() throws RemoteException {
    int numWords = textAnalyser.countWords(id);
    if (numWords != n) handleError("numWords != "+n+": "+numWords);
    int uniqueWords = textAnalyser.countUniqueWords(id);
    if (uniqueWords != n) handleError("uniqueWords != "+n+": "+uniqueWords);
  }

  public void run() {
    System.out.println("Stress "+n+" iniciado");
    try {
      id = textAnalyser.storeText(s);
      checkIntegrity();
    }
    catch (RemoteException e) {
      handleError(e);
    }
    try {
      for (int i=(n%2==0 ? MAX_TESTS : MAX_TESTS/2); i>0; i--) {
        checkIntegrity();
        if (interrupted()) break;
      }
      textAnalyser.removeText(id);
    }
    catch (Throwable t) {
      handleError(t);
    }
    System.out.println("Stress "+n+" terminando");
  }

  private Stress(int n) {
    this.n = n;
    s = "";
    for (int i=0; i<n; i++) s += i+" ";
  }

  private static synchronized void handleError(Throwable t) {
    t.printStackTrace();
    handleError(t.toString());
  }

  private static synchronized void handleError(String s) {
    System.err.println("Erro: "+(s!=null ? s : "<indefinido>"));
    num_errors++;
  }

  public static void main(String[] args) {
    try {
      if (args.length == 0) {
        System.out.println("Forneça a URL para o servidor.");
        System.exit(-1);
      }
      textAnalyser = (TextAnalyzer)Naming.lookup(args[0]);
      instances = new HashSet<Stress>();
      num_errors = 0;
      for (int i=NUM_THREADS; i>0; i--) {
        Stress stress = new Stress(i);
        instances.add(stress);
      }
      for (Stress stress : instances) {
        stress.start();
      }
      for (Stress stress : instances) {
        try {
          stress.join();
        }
        catch (InterruptedException e) {
          // do nothing
        }
      }
      if (num_errors == 0) {
        System.out.println("Nenhum erro encontrado.");
      }
      else {
        System.out.println("Total de erros encontrados: "+num_errors+".");
      }
    }
    catch (Exception e) {
      handleError(e);
    }
  }
}
