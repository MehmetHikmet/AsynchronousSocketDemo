
package asyncprogramming;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
public class AsyncServer {
    
        public static String sendResponse(AsynchronousSocketChannel channel, String input) throws JSONException, ExecutionException, InterruptedException {

        JSONObject obj = new JSONObject(input); //parse et
        int idServer = obj.getInt("id");
        int numS1 = obj.getInt("num1");
        int numS2 = obj.getInt("num2");
        String arr = obj.getString("operation");
        int sonuc = 0;

        switch (arr) {
            case "topla":
                sonuc = numS1 + numS2;
                break;
            case "cikar":
                sonuc = numS1 - numS2;
                break;
            case "carp":
                sonuc = numS1 * numS2;
                break;
            case "bol":
                sonuc = numS1 / numS2;
                break;
        }

        JSONObject jsonServer = new JSONObject();
        try {
            jsonServer.put("id", idServer);
            jsonServer.put("equals", sonuc);
        } catch (JSONException ex) {
            Logger.getLogger(AsyncServer.class.getName()).log(Level.SEVERE, null, ex);

        }


        byte [] serverMessage = new String(jsonServer.toString().trim()).getBytes();
        ByteBuffer serverBuffer = ByteBuffer.wrap(serverMessage);
        Future<Integer> resultS = channel.write(serverBuffer);

        resultS.get();

        serverBuffer.clear();

        return jsonServer.toString();
    }
    
    public void runServer() throws IOException, InterruptedException, ExecutionException, JSONException{
         AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 56428);
        serverChannel.bind(hostAddress);
        System.out.println("Server channel bound to port: " + hostAddress.getPort());


        for(int i=0; i<1000; i++) {
           // System.out.println("Waiting for a client to connect... ");

            Future acceptResult = serverChannel.accept();
            AsynchronousSocketChannel clientChannel = (AsynchronousSocketChannel) acceptResult.get();

            if ((clientChannel != null) && (clientChannel.isOpen())) {

                ByteBuffer buffer = ByteBuffer.allocate(1000);
                Future result = clientChannel.read(buffer);

                result.get();

                buffer.flip(); //nesneyi okumaya hazır hale getir
                String message = new String(buffer.array()).trim(); //tekrar stringe çevir

      
                
                String response  = sendResponse(clientChannel, message);
                
                 System.out.println("Server : ");
                System.out.println(message + "\n" + response);


                clientChannel.close();
            }

        }

        serverChannel.close();

    }
}
