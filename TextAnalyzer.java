/**
 * Interface de definicao do servico de analise de textos. Permite que textos
 * sejam armazenados para posteriores consultas. Oferece varias analises sobre
 * os textos armazenados. Em todos as analises valem as regras: 1) caracteres de
 * pontuacaoo (. , / ; ( ) _ + etc) e quebras de linha apenas separam palavras.
 * 2) palavras sao compostas de letras, numeros ou hifens. 3) para efeito de
 * relatorios, todas as palavras sao convertidas para letras minusculas.
 */
public interface TextAnalyzer extends java.rmi.Remote {

  /**
   * Armazena um texto para posterior analise. O texto deve ser mantido no
   * servico apenas enquando ha interesse em fazer analises sobre ele. Deve ser
   * removido quando nao for mais ser utilizado. O cliente e responsavel por
   * este controle.
   * 
   * @param text o texto a ser armazenado.
   * @return o identificador do texto para posterior referencia.
   * @throws java.rmi.RemoteException
   */
  public long storeText(String text) throws java.rmi.RemoteException;

  /**
   * Remove um texto anteriormente armazenado.
   * 
   * @param id o identificador do texto armazenado.
   * @throws java.rmi.RemoteException
   */
  public void removeText(long id) throws java.rmi.RemoteException;

  /**
   * Relatorio de numero de palavras. Conta o numero de palavras no texto.
   * 
   * @param id o identificador do texto armazenado.
   * @return o numero total de palavras do texto.
   * @throws java.rmi.RemoteException
   */
  public int countWords(long id) throws java.rmi.RemoteException;

  /**
   * Relatorio de numero de palavras diferentes. Conta o numero de palavras
   * diferentes no texto.
   * 
   * @param id o identificador do texto armazenado.
   * @return o numero de palavras diferentes do texto.
   * @throws java.rmi.RemoteException
   */
  public int countUniqueWords(long id) throws java.rmi.RemoteException;

  /**
   * Relatorio de uso das palavras. Conta o numero de vezes que cada
   * diferente palavra é utilizada no texto. Por exemplo, para o texto:
   *              "Isso é um teste: teste, é, um, é."
   * o resultado deve ser:                                                     
   * é - 3
   * teste - 2
   * um - 2
   * isso - 1
   * A ordem na qual as palavras aparecem no resultado nao  especificada.
   * @param id o identificador do texto armazenado.
   * @return o numero de palavras diferentes do texto.
   */
  public java.util.List<WordCount> wordUsage(long id)
    throws java.rmi.RemoteException;
}