
package asyncprogramming;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
 
public class AsyncClient {
      public static String sendRequest(AsynchronousSocketChannel client, String message) throws ExecutionException, InterruptedException {
        byte[] byteMsg = new String(message).getBytes();
        ByteBuffer writeBuffer = ByteBuffer.wrap(byteMsg);
        Future<Integer> writeResult = client.write(writeBuffer);

        writeResult.get();
        writeBuffer.clear();

        ByteBuffer readBuffer = ByteBuffer.allocate(10000);
        Future<Integer> readResult = client.read(readBuffer);
        readResult.get();
        String result = new String(readBuffer.array()).trim();
        readBuffer.clear();

        return result;
    }

   public void runClient()
            throws IOException, InterruptedException, ExecutionException{
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 56428);
        String[] options = {"topla", "carp", "bol", "cikar"};
        for (int i = 0; i < 1000; i++) {
            try {
                        AsynchronousSocketChannel client = AsynchronousSocketChannel.open();
                        Future<Void> future = client.connect(hostAddress);
                        future.get();

                        JSONObject jsonClient = new JSONObject();

                        try {
                            jsonClient.put("id", new Random().nextInt());
                            jsonClient.put("operation", options[new Random().nextInt(4)]);
                            jsonClient.put("num1", new Random().nextInt());
                            jsonClient.put("num2", new Random().nextInt());
                        } catch (JSONException ex) {
                            Logger.getLogger(AsyncClient.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        String read = sendRequest(client, jsonClient.toString());
                         System.out.println("Client : ");
                        System.out.println(jsonClient + "\n" + read);

                    } catch (IOException | InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                 
        }
    }
    
}
}
