import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Implements the methods from the TextAnalyzer. Is responsible to the Report
 * functionality and remote objected methods.
 * 
 * @author Malf
 */
public class PadText extends UnicastRemoteObject implements TextAnalyzer {

  private int m_words, m_uwords;
  private long m_currKey;
  private static final long serialVersionUID = 1L;
  private HashMap<Long, String> m_txtMap;
  private List<WordCount> m_wordList;

  /**
   * Constructor.
   * 
   * @throws RemoteException
   */
  protected PadText() throws RemoteException {
    super();
    m_words = 0;
    m_uwords = 0;
    m_currKey = 0;
    m_txtMap = new HashMap<Long, String>();
    m_wordList = new LinkedList<WordCount>();
  }

  /**
   * Generate and save the full report: number of words, number of unique words
   * and the word usage.
   * 
   * @param id identifier for the stored text.
   */
  private void makeReport(long id) {
    //TODO: Make a more efficient solution to the word map!
    HashMap<String, Integer> wordMap = new HashMap<String, Integer>();

    String txt = m_txtMap.get(id);
    String[] words = txt.split(" ");
    m_words = 0;

    int count = 0;
    for (String word : words) {
      if (word != "") {
        if (!wordMap.containsKey(word)) {
          count = 1;
        }
        else {
          count = wordMap.get(word) + 1;
          wordMap.remove(word);
        }
        wordMap.put(word, count);
        m_words++;
      }
    }
    m_uwords = wordMap.size();

    // Save word list
    m_wordList.clear();
    for (String key : wordMap.keySet()) {
      if (key != null) {
        WordCount w = new WordCount();
        w.word = key;
        w.count = wordMap.get(key);
        m_wordList.add(w);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long storeText(String text) throws RemoteException {
    //TODO: A better idea to the hashKey should be something like clientId/txtId
    //TODO: Concurrency, a list of jobs?
    m_currKey++;

    //TODO: Better mask?
    String cleanTxt = text.toLowerCase().replaceAll("[^a-zA-Z0-9ç-ó\\s\\-]+", "");
    cleanTxt = cleanTxt.replaceAll("\\n", " ");
    m_txtMap.put(m_currKey, cleanTxt);

    return m_currKey;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeText(long id) throws RemoteException {
    //TODO: Concurrency, a list of jobs?
    m_txtMap.remove(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int countWords(long id) throws RemoteException {
    //TODO: Concurrency, a list of jobs?
    if (!m_txtMap.containsKey(id)) {
      return 0;
    }
    makeReport(id);
    return m_words;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int countUniqueWords(long id) throws RemoteException {
    //TODO: Concurrency, a list of jobs?
    if (!m_txtMap.containsKey(id)) {
      return 0;
    }
    makeReport(id);
    return m_uwords;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<WordCount> wordUsage(long id) throws RemoteException {
    //TODO: Concurrency, a list of jobs?
    if (!m_txtMap.containsKey(id)) {
      return null;
    }
    makeReport(id);
    return m_wordList;
  }
}
