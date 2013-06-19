Calculadora RMI
===============

Implemente um programa que emita relatórios sobre textos do usuário. Posto
em tópicos, o programa seria:

1) Uma aplicação cliente, usando Swing para construir uma interface
   gráfica com o usuário. Utilizando a aplicação cliente, o usuário
   deve ser capaz de:

   1.1) Digitar um texto.
   1.2) Carregar um arquivo texto.
   1.2) Gravar o texto corrente em um arquivo.
   1.3) Solicitar um relatório, entre as alternativas:
        1.3.1) número total de palavras que compõem o texto.
        1.3.2) número de palavras diferentes que aparecem no texto.
        1.3.3) número de vezes que cada palavra diferente aparece no texto.
   1.4) Visualizar o texto e o relatório simultaneamente.
   1.5) Apagar o texto.
   1.6) Apagar o relatório.
   1.7) Sair do programa.

2) Um servidor que analise os textos e forneça os resultados desejados
   à aplicação cliente. O servidor só precisa armazenar os textos em
   memória, não é preciso nenhum tipo de armazenamento persistente.

3) Cliente e servidor devem se comunicar via RMI, segundo a interface:

```java
/**
 * Interface de definição do serviço de análise de textos. Permite que
 * textos sejam armazenados para posteriores consultas. Oferece várias
 * análises sobre os textos armazenados. Em todos as análises valem as
 * regras:
 * 1) caracteres de pontuação (. , / ; ( ) _ + etc) e quebras de linha
 *    apenas separam palavras.
 * 2) palavras são compostas de letras, números ou hífens.
 * 3) para efeito de relatórios, todas as palavras são convertidas para
 *    letras minúsculas.
 */
public interface TextAnalyzer extends java.rmi.Remote {

  /**
   * Armazena um texto para posterior análise. O texto deve ser mantido
   * no serviço apenas enquando há interesse em fazer análises sobre ele.
   * Deve ser removido quando não for mais ser utilizado. O cliente é
   * responsável por este controle.
   * @param text o texto a ser armazenado.
   * @return o identificador do texto para posterior referência.
   */
  public long storeText(String text)
  throws java.rmi.RemoteException;

  /**
   * Remove um texto anteriormente armazenado.
   * @param id o identificador do texto armazenado.
   */
  public void removeText(long id)
  throws java.rmi.RemoteException;

  /**
   * Relátorio de número de palavras. Conta o número de palavras no texto.
   * @param id o identificador do texto armazenado.
   * @return o número total de palavras do texto.
   */
  public int countWords(long id)
  throws java.rmi.RemoteException;

  /**
   * Relátorio de número de palavras diferentes. Conta o número de palavras
   * diferentes no texto.
   * @param id o identificador do texto armazenado.
   * @return o número de palavras diferentes do texto.
   */
  public int countUniqueWords(long id)
  throws java.rmi.RemoteException;

  /**
   * Relátorio de uso das palavras. Conta o número de vezes que cada
   * diferente palavra é utilizada no texto. Por exemplo, para o texto:
   *              "Isso é um teste: teste, é, um, é."
   * o resultado deve ser:
   * é - 3
   * teste - 2
   * um - 2
   * isso - 1
   * A ordem na qual as palavras aparecem no resultado não é especificada.
   * @param id o identificador do texto armazenado.
   * @return o número de palavras diferentes do texto.
   */
  public java.util.List<WordCount> wordUsage(long id)
  throws java.rmi.RemoteException;
}
```
Sendo:

```java
public class WordCount implements java.io.Serializable {
  String word;
  int count;
}
```

O único objetivo deste exercício é podermos avaliar os seus conhecimentos.
Dessa forma, não faz sentido você pedir ajuda a alguém. É importante que o
código escrito reflita a *sua* melhor forma de implementar o que foi pedido, 
portanto, evite usar ferramentas de geração automática de código como o NetBeans. 
Claro que você pode consultar todo o material que quiser, como seria normal no 
dia-a-dia do trabalho.

Serão considerados na avaliação: clareza, simplicidade, corretude e completude.

