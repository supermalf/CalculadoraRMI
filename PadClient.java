import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Client abstraction class. It's responsible for the Main dialog logic and the
 * RMI connection to the server.
 * 
 * @author Malf
 */
public class PadClient {
  private static PadFrame m_app;
  private static TextAnalyzer m_txtAnalyser;

  /**
   * Initialize the client, connect to the server and constructs the main frame.
   * 
   * @throws Exception
   */
  private static void init() throws Exception {
    System.setProperty("java.security.policy", "policy.all");
    System.setSecurityManager(new RMISecurityManager());

    m_txtAnalyser = (TextAnalyzer) Naming.lookup("rmi://localhost/pad");
    m_app = new PadFrame();
  }

  /**
   * Registers PadFrame callbacks.
   */
  private static void registerCallbacks() {
    // New file callback
    m_app.setNewfileAction(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        m_app.clearText();
      }
    });

    // Open file callback
    m_app.setOpenfileAction(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        m_app.openFile();
      }
    });

    // Save file callback
    m_app.setSavefileAction(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        m_app.saveFile();
      }
    });

    // Show/Hide report callback
    m_app.setShowReportAction(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        m_app.toggleReport();
      }
    });

    // Clear report callback
    m_app.setClearReportAction(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        m_app.clearReport();
      }
    });

    // Words report callback
    // TODO: Make a cleaner code bellow
    m_app.setWordsReportAction(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
      	Thread t = new Thread(new Runnable() {
      		public void run() {  
      		  try {
      		  	String txt = m_app.getText();
	            long ver = m_txtAnalyser.storeText(txt);
		          int nWords = m_txtAnalyser.countWords(ver);
		       
		          m_app.setTextVersion(ver);
		          m_app.printWordsReport(nWords);
		          m_app.showReport();
		
		          m_txtAnalyser.removeText(ver);
            } 
            catch (RemoteException e) {
	            e.printStackTrace();
            }
        	}
        });
        t.start();
      }
    });


    // Unique words report callback
    // TODO: Make a cleaner code bellow
    m_app.setUWordsReportAction(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
      	Thread t = new Thread(new Runnable() {
      		public void run() {  
      		  try {
      		  	String txt = m_app.getText();
	            long ver = m_txtAnalyser.storeText(txt);
		          int nWords = m_txtAnalyser.countUniqueWords(ver);
		       
		          m_app.setTextVersion(ver);
		          m_app.printUWordsReport(nWords);
		          m_app.showReport();
		
		          m_txtAnalyser.removeText(ver);
            } 
            catch (RemoteException e) {
	            e.printStackTrace();
            }
        	}
        });
        t.start();
      }
    });

    // Word usage report callback
    // TODO: Make a cleaner code bellow
    m_app.setWordsUsageReportAction(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
      	Thread t = new Thread(new Runnable() {
      		public void run() {  
      		  try {
              String txt = m_app.getText();
              long ver = m_txtAnalyser.storeText(txt);
              List<WordCount> words = m_txtAnalyser.wordUsage(ver);

              m_app.setTextVersion(ver);
              m_app.printWordsUsageReport(words);
              m_app.showReport();

              m_txtAnalyser.removeText(ver);
            } 
            catch (RemoteException e) {
	            e.printStackTrace();
            }
        	}
        });
        t.start();
      }
    });
  }

  /**
   * Show the main dialog.
   */
  private static void showDialog() {
    m_app.setVisible(true);
  }

  /**
   * Client's main method.
   * 
   * @param args
   * @throws Exception
   */
  public static void main(final String args[]) throws Exception {
    init();
    registerCallbacks();
    showDialog();
  }
}
