
package asyncprogramming;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.json.JSONException;

public class Main {
    
    public static void main (String[] args)throws Exception{
        Thread AsyncServerThread = new Thread(){
            public void run(){
                try{
                    new AsyncServer().runServer();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            
        };
        
        Thread AsyncClientThread = new Thread(){
            
             public void run(){
                 try{
                     new AsyncClient().runClient();
                 }
                 catch (Exception e){
                     e.printStackTrace();
                 }
             }
            
        };
    
        AsyncServerThread.start();
        AsyncClientThread.start();
    }
    
    
}