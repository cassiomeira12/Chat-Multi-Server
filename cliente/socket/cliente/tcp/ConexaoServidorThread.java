package socket.cliente.tcp;

import java.io.PrintStream;
import java.net.Socket;

public class ConexaoServidorThread extends Thread {

  private Socket socket;
  private String ip;
  private int porta;
  private boolean reconectar;

  private INovoServidor iNovoServidor;

  public ConexaoServidorThread(Socket socket, INovoServidor iNovoServidor) {
    this.socket = socket;
    this.ip = socket.getLocalAddress().getHostAddress();
    this.porta = socket.getPort();
    this.iNovoServidor = iNovoServidor;
    this.reconectar = true;
  }

  public void run() {
    while (!socket.isClosed()) {
      try {
        PrintStream saida = new PrintStream(socket.getOutputStream());
        saida.println("Servidor Online?");//Enviando msg
        Thread.sleep(2000);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    if (reconectar) {
      iNovoServidor.buscarServidor();
    }
  }

  public interface INovoServidor {
    public void buscarServidor();
  }

  public void setReconectar(boolean reconectar) {
    this.reconectar = reconectar;
  }

}
