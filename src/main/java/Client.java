import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8080);
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(inetSocketAddress);

        try(BufferedReader terminalReader = new BufferedReader(new InputStreamReader(System.in))){
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

            String msg;
            while (true){
                System.out.println("Enter message for server:");
                System.out.println("Write 'end' to stop");
                msg = terminalReader.readLine();
                if (msg.equals("end")) {
                    break;
                }
                socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));

                Thread.sleep(2000);

                int bytesCount = socketChannel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());

                inputBuffer.clear();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
