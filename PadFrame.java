import java.awt.BorderLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

/**
 * This class represents the swing's main frame, that controls only the
 * components. The logic callbacks should be implemented in the class that
 * instantiates PadFrame.
 * 
 * @author Malf
 */
public class PadFrame extends JFrame {
	// Class variables
  private long m_txtVer;
  private JTextArea m_textArea, m_reportArea;
  private MenuBar m_menuBar;
  private Menu m_file_mnu, m_report_mnu;
  private MenuItem m_newFile_itm, m_openFile_itm, m_close_itm;
  private MenuItem m_saveAsFile_itm, m_showRep_itm, m_clearRep_itm;
  private MenuItem m_wordsRep_itm, m_uwordsRep_itm, m_wordUsageRep_itm;
  private JSplitPane m_sp;
  private boolean m_repVisible = false;
  private static final long serialVersionUID = 1L;

  /**
   * Constructor
   */
  public PadFrame() {
    createDialog();
    createMenu();
  }

  // Interface methods

  /**
   * Creates MenuBar swing components.
   */
  private void createMenu() {
    m_menuBar = new MenuBar();
    m_file_mnu = new Menu();
    m_report_mnu = new Menu();
    this.setMenuBar(this.m_menuBar);

    // Create File menu
    this.m_menuBar.add(this.m_file_mnu);
    this.m_file_mnu.setLabel("File");

    m_newFile_itm = new MenuItem();
    this.m_newFile_itm.setLabel("New");
    this.m_newFile_itm.setShortcut(new MenuShortcut(KeyEvent.VK_N, false));
    this.m_file_mnu.add(this.m_newFile_itm);

    m_openFile_itm = new MenuItem();
    this.m_openFile_itm.setLabel("Open File...");
    this.m_openFile_itm.setShortcut(new MenuShortcut(KeyEvent.VK_O, false));
    this.m_file_mnu.add(this.m_openFile_itm);
    this.m_file_mnu.addSeparator();

    m_saveAsFile_itm = new MenuItem();
    this.m_saveAsFile_itm.setLabel("Save As...");
    this.m_saveAsFile_itm.setShortcut(new MenuShortcut(KeyEvent.VK_S, false));
    this.m_file_mnu.add(this.m_saveAsFile_itm);
    this.m_file_mnu.addSeparator();

    m_close_itm = new MenuItem();
    this.m_close_itm.setLabel("Close");
    this.m_close_itm.setShortcut(new MenuShortcut(KeyEvent.VK_F4, false));
    this.m_file_mnu.add(this.m_close_itm);

    // Create Report menu
    this.m_menuBar.add(this.m_report_mnu);
    this.m_report_mnu.setLabel("Report");

    m_showRep_itm = new MenuItem();
    this.m_showRep_itm.setLabel("Show / Hide");
    this.m_showRep_itm.setShortcut(new MenuShortcut(KeyEvent.VK_R, false));
    this.m_report_mnu.add(this.m_showRep_itm);

    m_clearRep_itm = new MenuItem();
    this.m_clearRep_itm.setLabel("Clear");
    this.m_report_mnu.add(this.m_clearRep_itm);
    this.m_report_mnu.addSeparator();

    m_wordsRep_itm = new MenuItem();
    this.m_wordsRep_itm.setLabel("# of words");
    this.m_wordsRep_itm.setShortcut(new MenuShortcut(KeyEvent.VK_1, false));
    this.m_report_mnu.add(this.m_wordsRep_itm);

    m_uwordsRep_itm = new MenuItem();
    this.m_uwordsRep_itm.setLabel("# of unique words");
    this.m_uwordsRep_itm.setShortcut(new MenuShortcut(KeyEvent.VK_2, false));
    this.m_report_mnu.add(this.m_uwordsRep_itm);

    m_wordUsageRep_itm = new MenuItem();
    this.m_wordUsageRep_itm.setLabel("Word usage");
    this.m_wordUsageRep_itm.setShortcut(new MenuShortcut(KeyEvent.VK_3, false));
    this.m_report_mnu.add(this.m_wordUsageRep_itm);
  }

  /**
   * Creates swing main dialog
   */
  private void createDialog() {
    this.setSize(600, 600);
    this.setTitle("EngDist Pad");
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    m_textArea = new JTextArea("");
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(m_textArea);

    m_reportArea = new JTextArea("");
    m_reportArea.setEditable(false);
    getContentPane().add(m_reportArea);

    m_sp =
      new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, m_reportArea, m_textArea);
    getContentPane().add(m_sp, BorderLayout.CENTER);

    hideReport();
    m_sp.setContinuousLayout(true);
  }

  // Interface methods

  /**
   * Opens the file through the system's dialog.
   */
  protected void openFile() {
    JFileChooser open = new JFileChooser();
    int option = open.showOpenDialog(this);

    if (option == JFileChooser.APPROVE_OPTION) {
      this.m_textArea.setText("");
      try {
        Scanner scan =
          new Scanner(new FileReader(open.getSelectedFile().getPath()));
        while (scan.hasNext()) {
          this.m_textArea.append(scan.nextLine() + "\n");
        }
      }
      catch (Exception ex) {
        System.out.println(ex.getMessage());
      }
    }
  }

  /**
   * Saves the file through the system's dialog.
   */
  protected void saveFile() {
    JFileChooser save = new JFileChooser();
    int option = save.showSaveDialog(this);
    if (option == JFileChooser.APPROVE_OPTION) {
      try {
        BufferedWriter out =
          new BufferedWriter(new FileWriter(save.getSelectedFile().getPath()));
        out.write(this.m_textArea.getText());
        out.close();
      }
      catch (Exception ex) {
        System.out.println(ex.getMessage());
      }
    }
  }

  /**
   * Clears the user text.
   */
  protected void clearText() {
    //TODO: Ask if the user wants to save the current work.
    m_textArea.setText("");
  }

  /**
   * Clears the report text.
   */
  protected void clearReport() {
    m_reportArea.setText("");
  }

  /**
   * Prints the word counting report.
   * 
   * @param nWords the total of words counted.
   */
  protected void printWordsReport(int nWords) {
    clearReport();
    m_reportArea.append("Report:\n");
    m_reportArea.append("# Words: " + nWords + "\n");
  }

  /**
   * Prints the unique word counting report.
   * 
   * @param nWords the total of words counted.
   */
  protected void printUWordsReport(int nWords) {
    clearReport();
    m_reportArea.append("Report:\n");
    m_reportArea.append("# Unique words: " + nWords + "\n");
  }

  /**
   * Prints the word usage report.
   * 
   * @param words
   */
  protected void printWordsUsageReport(List<WordCount> words) {
    clearReport();
    m_reportArea.append("Report:\n");
    m_reportArea.append("# Word list: \n");

    for (Iterator<WordCount> i = words.iterator(); i.hasNext();) {
      WordCount w = i.next();
      m_reportArea.append(" - " + w.word + ": " + w.count + "\n");
    }
  }

  /**
   * Show report
   */
  protected void showReport() {
    m_repVisible = true;
    m_sp.setDividerLocation(200);
  }

  /**
   * Hide report
   */
  protected void hideReport() {
    m_repVisible = false;
    m_sp.setDividerLocation(0);
  }

  /**
   * Toggles the report visibility
   */
  protected void toggleReport() {
    m_repVisible = !m_repVisible;

    if (m_repVisible) {
      showReport();
    }
    else {
      hideReport();
    }
  }

  /**
   * Gets the text from the notepad.
   * 
   * @return text
   */
  protected String getText() {
    return m_textArea.getText();
  }

  /**
   * Gets the text version for the remote control.
   * 
   * @return text version.
   */
  public long getTextVersion() {
    return m_txtVer;
  }

  /**
   * Sets the text version for the remote control.
   * 
   * @param m_txtVer text version.
   */
  public void setTextVersion(long m_txtVer) {
    this.m_txtVer = m_txtVer;
  }

  // Callbacks

  /**
   * Sets the New File callback
   * 
   * @param a action
   */
  public void setNewfileAction(final ActionListener a) {
    m_newFile_itm.addActionListener(a);
  }

  /**
   * Sets the Open File callback
   * 
   * @param a action
   */
  public void setOpenfileAction(final ActionListener a) {
    m_openFile_itm.addActionListener(a);
  }

  /**
   * Sets the Open File callback
   * 
   * @param a action
   */
  public void setSavefileAction(final ActionListener a) {
    m_saveAsFile_itm.addActionListener(a);
  }

  /**
   * Sets the Show/Hide Report callback
   * 
   * @param a action
   */
  public void setShowReportAction(final ActionListener a) {
    m_showRep_itm.addActionListener(a);
  }

  /**
   * Sets the Clear Report callback
   * 
   * @param a action
   */
  public void setClearReportAction(final ActionListener a) {
    m_clearRep_itm.addActionListener(a);
  }

  /**
   * Sets the number of words Report callback
   * 
   * @param a action
   */
  public void setWordsReportAction(final ActionListener a) {
    m_wordsRep_itm.addActionListener(a);
  }

  /**
   * Sets the number of unique words Report callback
   * 
   * @param a action
   */
  public void setUWordsReportAction(final ActionListener a) {
    m_uwordsRep_itm.addActionListener(a);
  }

  /**
   * Sets the word usage Report callback
   * 
   * @param a action
   */
  public void setWordsUsageReportAction(final ActionListener a) {
    m_wordUsageRep_itm.addActionListener(a);
  }
}
