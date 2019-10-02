/***********************************************************************
 * Autor: Cassio Meira Silva
 * Matricula: 201610373
 * Inicio: 06/12/18
 * Ultima alteracao: 14/12/18
 * Nome: Apdu
 * Funcao: Classe que define o protocolo da APDU
 ***********************************************************************/

package socket.pdu;

public class Apdu extends Thread {
  //Interface que contem metodos da APDU
  private IApdu iApdu;

  private String ip;
  private String pacote;

  private static final char SCAPE_CHAR = 126;
  private static final char END_CHAR = '\0';

  public static final String SEND = "s";
  public static final String CREATE = "c";
  public static final String JOIN = "j";
  public static final String LEAVE = "l";
  public static final String ACCEPT = "a";
  public static final String REJECT = "r";

  public static final String NEW_SERVER = "ns";
  public static final String RECONECT_SERVER = "rs";

  /*********************************************
   * Metodo: Apdu
   * Funcao: Construtor
   * Parametros: IApdu iApdu
   * Retorno: void
   *********************************************/
  public Apdu(IApdu iApdu) {
    this.iApdu = iApdu;
  }

  public void run() {
    try {
      String codigo = "";
      String sala = "";
      String ipMensagem = "";
      String mensagem = "";

      int i = 0;
      StringBuilder strBuilder;

      //Lendo o caractere que define o tipo da Mensagem
      strBuilder = new StringBuilder(codigo);
      for (; i < pacote.length() && pacote.charAt(i) != SCAPE_CHAR; ++i) {
        strBuilder.append(pacote.charAt(i));
      }
      codigo = strBuilder.toString();

      switch (codigo) {
        case SEND://    [s~sala~mensagem\0] [s~sala~ip~mensagem\0]
          i = 2;

          strBuilder = new StringBuilder();
          for (; i < pacote.length() && pacote.charAt(i) != SCAPE_CHAR && pacote.charAt(i) != END_CHAR; ++i) {
            strBuilder.append(pacote.charAt(i));
          }
          i++;
          sala = strBuilder.toString();

          strBuilder = new StringBuilder();
          for (; i < pacote.length() && pacote.charAt(i) != SCAPE_CHAR && pacote.charAt(i) != END_CHAR; ++i) {
            strBuilder.append(pacote.charAt(i));
          }
          mensagem = strBuilder.toString();

          if (pacote.charAt(i) == END_CHAR) {
            iApdu.mensagem(sala, ip, mensagem);
            return;
          } else {
            ipMensagem = mensagem;
            i++;
          }

          strBuilder = new StringBuilder();
          for (; i < pacote.length() && pacote.charAt(i) != SCAPE_CHAR && pacote.charAt(i) != END_CHAR; ++i) {
            strBuilder.append(pacote.charAt(i));
          }

          mensagem = strBuilder.toString();

          iApdu.mensagem(sala, ipMensagem, mensagem);

          break;
        case CREATE://  [c~sala\0]
          i = 2;

          strBuilder = new StringBuilder();
          for (; i < pacote.length() && pacote.charAt(i) != SCAPE_CHAR && pacote.charAt(i) != END_CHAR; i++) {
            strBuilder.append(pacote.charAt(i));
          }

          sala = strBuilder.toString();

          iApdu.criarSala(sala);
          //break;

        case JOIN://    [j~sala\0] [j~sala~ip\0]
          i = 2;

          //  [SALA]
          strBuilder = new StringBuilder();
          for (; i < pacote.length() && pacote.charAt(i) != SCAPE_CHAR && pacote.charAt(i) != END_CHAR; ++i) {
            strBuilder.append(pacote.charAt(i));
          }
          sala = strBuilder.toString();

          if (pacote.charAt(i) == END_CHAR) {
            iApdu.entrarSala(sala, ip);
            return;
          }

          i++;

          //  [IP]
          strBuilder = new StringBuilder();
          for (; i < pacote.length() && pacote.charAt(i) != SCAPE_CHAR && pacote.charAt(i) != END_CHAR; ++i) {
            strBuilder.append(pacote.charAt(i));
          }

          ipMensagem = strBuilder.toString();

          iApdu.entrarSala(sala, ipMensagem);

          break;
        case LEAVE://   [l~sala\0] [l~sala~ip\0]
          i = 2;

          //  [SALA]
          strBuilder = new StringBuilder();
          for (; i < pacote.length() && pacote.charAt(i) != SCAPE_CHAR && pacote.charAt(i) != END_CHAR; ++i) {
            strBuilder.append(pacote.charAt(i));
          }
          sala = strBuilder.toString();

          if (pacote.charAt(i) == END_CHAR) {
            iApdu.sairSala(sala, ip);
            return;
          }

          i++;

          //  [IP]
          strBuilder = new StringBuilder();
          for (; i < pacote.length() && pacote.charAt(i) != SCAPE_CHAR && pacote.charAt(i) != END_CHAR; ++i) {
            strBuilder.append(pacote.charAt(i));
          }

          ipMensagem = strBuilder.toString();

          iApdu.sairSala(sala, ipMensagem);

          break;

        case NEW_SERVER://  [ns~ip~porta\0] [ns~porta\0]
          i++;

          // [IP]
          strBuilder = new StringBuilder();
          for (; i < pacote.length() && pacote.charAt(i) != SCAPE_CHAR && pacote.charAt(i) != END_CHAR; ++i) {
            strBuilder.append(pacote.charAt(i));
          }

          String ip, porta;
          if (pacote.charAt(i) == END_CHAR) {
            porta = strBuilder.toString();
            iApdu.novoServidor(this.ip, porta);
            return;
          }
          i++;
          ip = strBuilder.toString();

          //  [PORTA]
          strBuilder = new StringBuilder();
          for (; i < pacote.length() && pacote.charAt(i) != SCAPE_CHAR && pacote.charAt(i) != END_CHAR; ++i) {
            strBuilder.append(pacote.charAt(i));
          }

          porta = strBuilder.toString();

          iApdu.novoServidor(ip, porta);

          break;

        case RECONECT_SERVER://  [rs~porta\0]
          i++;

          // [IP]
          strBuilder = new StringBuilder();
          for (; i < pacote.length() && pacote.charAt(i) != SCAPE_CHAR && pacote.charAt(i) != END_CHAR; ++i) {
            strBuilder.append(pacote.charAt(i));
          }

          String ips, portas;
          if (pacote.charAt(i) == END_CHAR) {
            portas = strBuilder.toString();
            iApdu.reconectarServidor(this.ip, portas);
            return;
          }

          break;

        default:
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /*********************************************
   * Metodo: CREATE
   * Funcao: Cria a APDU para uma mensagem do tipo CREATE
   * Parametros: String sala
   * Retorno: String
   *********************************************/
  public static String CREATE(String sala) {//  CREATE [c~sala]
    return new StringBuilder().
            append(CREATE).
            append(SCAPE_CHAR).
            append(sala).
            append(END_CHAR).
            toString();
  }

  /*********************************************
   * Metodo: JOIN
   * Funcao: Cria a APDU para uma mensagem do tipo JOIN
   * Parametros: String sala
   * Retorno: String
   *********************************************/
  public static String JOIN(String sala) {//  JOIN [j~sala\0]
    return new StringBuilder().
            append(JOIN).
            append(SCAPE_CHAR).
            append(sala).
            append(END_CHAR).
            toString();
  }

  /*********************************************
   * Metodo: JOIN
   * Funcao: Cria a APDU para uma mensagem do tipo JOIN
   * Parametros: String sala, String ip
   * Retorno: String
   *********************************************/
  public static String JOIN(String sala, String ip) {//  JOIN [j~sala~ip\0]
    return new StringBuilder().
            append(JOIN).
            append(SCAPE_CHAR).
            append(sala).
            append(SCAPE_CHAR).
            append(ip).
            append(END_CHAR).
            toString();
  }

  /*********************************************
   * Metodo: LEAVE
   * Funcao: Cria a APDU para uma mensagem do tipo LEAVE
   * Parametros: String sala
   * Retorno: String
   *********************************************/
  public static String LEAVE(String sala) {//  JOIN [l~sala\0]
    return new StringBuilder().
            append(LEAVE).
            append(SCAPE_CHAR).
            append(sala).
            append(END_CHAR).
            toString();
  }

  /*********************************************
   * Metodo: LEAVE
   * Funcao: Cria a APDU para uma mensagem do tipo LEAVE
   * Parametros: String sala
   * Retorno: String
   *********************************************/
  public static String LEAVE(String sala, String ip) {//  LEAVE [l~sala~ip\0]
    return new StringBuilder().
            append(LEAVE).
            append(SCAPE_CHAR).
            append(sala).
            append(SCAPE_CHAR).
            append(ip).
            append(END_CHAR).
            toString();
  }

  /*********************************************
   * Metodo: SEND
   * Funcao: Cria a APDU para uma mensagem do tipo SEND
   * Parametros: String sala, String mensagem
   * Retorno: String
   *********************************************/
  public static String SEND(String sala, String mensagem) {//  SEND [s~sala~mensagem\0]
    return new StringBuilder().
            append(SEND).
            append(SCAPE_CHAR).
            append(sala).
            append(SCAPE_CHAR).
            append(mensagem).
            append(END_CHAR).
            toString();
  }

  /*********************************************
   * Metodo: SEND
   * Funcao: Cria a APDU para uma mensagem do tipo SEND
   * Parametros: String sala, String ip, String mensagem
   * Retorno: String
   *********************************************/
  public static String SEND(String sala, String ip, String mensagem) {//  SEND [s~sala~ip~mensagem\0]
    return new StringBuilder().
            append(SEND).
            append(SCAPE_CHAR).
            append(sala).
            append(SCAPE_CHAR).
            append(ip).
            append(SCAPE_CHAR).
            append(mensagem).
            append(END_CHAR).
            toString();
  }

  public static String NEWSERVER(String ip, int porta) {
    return new StringBuilder().
            append(NEW_SERVER).
            append(SCAPE_CHAR).
            append(ip).
            append(SCAPE_CHAR).
            append(porta).
            append(END_CHAR).
            toString();
  }

  public static String NEWSERVER(int porta) {
    return new StringBuilder().
            append(NEW_SERVER).
            append(SCAPE_CHAR).
            append(porta).
            append(END_CHAR).
            toString();
  }

  public static String RECONECTSERVER(int porta) {
    return new StringBuilder().
            append(RECONECT_SERVER).
            append(SCAPE_CHAR).
            append(porta).
            append(END_CHAR).
            toString();
  }

  /*********************************************
   * Metodo: ACCEPT
   * Funcao: Cria a APDU para uma mensagem do tipo ACCEPT
   * Parametros: String mensagem
   * Retorno: String
   *********************************************/
  public static String ACCEPT(String mensagem) {//  ACCEPT [a~mensagem\0]
    return ACCEPT + SCAPE_CHAR + mensagem;
  }

  /*********************************************
   * Metodo: REJECT
   * Funcao: Cria a APDU para uma mensagem do tipo REJECT
   * Parametros: String sala
   * Retorno: String
   *********************************************/
  public static String REJECT(String mensagem) {//  REJECT [r~mensagem\0]
    return REJECT + SCAPE_CHAR + mensagem;
  }

  /*********************************************
   * Metodo: trartMensagem
   * Funcao: Inicia o tratamento de um novo pacote recebido
   * Parametros: String mensagem, String ip
   * Retorno: void
   *********************************************/
  public void tratarMensagem(String pacote, String ip) {
    this.pacote = pacote;
    this.ip = ip;
    this.start();
  }

  /*********************************************
   * Classe: Interface IApdu
   * Funcao: Interface que define as funcoes principais para cada tipo de APDU
   *********************************************/
  public interface IApdu {
    public void mensagem(String sala, String ip, String mensagem);
    public void criarSala(String sala);
    public void entrarSala(String sala, String ip);
    public void sairSala(String sala, String ip);
    public void novoServidor(String ip, String porta);
    public void reconectarServidor(String ip, String porta);
  }

}
