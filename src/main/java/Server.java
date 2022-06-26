import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress("127.0.0.1", 8080));
        while (true) {
            try(SocketChannel socketChannel = serverChannel.accept();) {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                while (socketChannel.isConnected()){
                    int bytesCount = socketChannel.read(inputBuffer);

                    if (bytesCount == -1){
                        break;
                    }
                    final String msg = new String(inputBuffer.array(),0,  bytesCount, StandardCharsets.UTF_8);
                    inputBuffer.clear();
                    System.out.println("Получено сообщение от клиента :" + msg);
                    String msgWithoutSpaces = TextRedactor.deleteSpaces(msg);
                    socketChannel.write(ByteBuffer.wrap(("Server: " + msgWithoutSpaces).getBytes(StandardCharsets.UTF_8)));
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            }

    }
}
