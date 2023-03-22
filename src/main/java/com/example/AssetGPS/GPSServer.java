package com.example.AssetGPS;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.example.AssetGPS.configuration.CustomFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class GPSServer
{
    public void startListening()
    {
        try
        {
            // Create an AsynchronousServerSocketChannel that will listen on port 7575
            final AsynchronousServerSocketChannel listener =
                    AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(7575));
            // Listen for a new request
            listener.accept( null, new CompletionHandler<AsynchronousSocketChannel,Void>() {

                @Override
                public void completed(AsynchronousSocketChannel ch, Void att)
                {
                    // Accept the next connection
                    listener.accept( null, this );

                    // Greet the client
//                    ch.write( ByteBuffer.wrap( "Hello, testing!\n".getBytes() ) );

                    // Allocate a byte buffer (4K) to read from the client
                    ByteBuffer byteBuffer = ByteBuffer.allocate( 4096 );
                    try
                    {
                        // Read the first line
                        int bytesRead = ch.read( byteBuffer ).get( 60, TimeUnit.SECONDS );

                        boolean running = true;
                        while( bytesRead != -1 && running )
                        {
                            log.debug( "bytes read: " + bytesRead );

                            // Make sure that we have data to read
                            if( byteBuffer.position() > 2 )
                            {
                                // Make the buffer ready to read
                                byteBuffer.flip();

                                // Convert the buffer into a line
                                byte[] lineBytes = new byte[ bytesRead ];
                                byteBuffer.get( lineBytes, 0, bytesRead );
                                String line = new String( lineBytes );

                                // Debug
                                log.debug( "Message: " + line );
//                                line = "Server: "+line;

                                // Echo back to the caller
                                ch.write( ByteBuffer.wrap( line.getBytes() ) );

                                // Send received data to readable file or database
                                update(line);

                                // Make the buffer ready to write
                                byteBuffer.clear();

                                // Read the next line
                                bytesRead = ch.read( byteBuffer ).get( 60, TimeUnit.SECONDS );
                            }
                            else
                            {
                                ch.write( ByteBuffer.wrap( "Empty line !!!".getBytes() ) );
                                // An empty line signifies the end of the conversation in our protocol
                                running = false;
                            }
                        }
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    catch (ExecutionException e)
                    {
                        e.printStackTrace();
                    }
                    catch (TimeoutException e)
                    {
                        // The user exceeded the 20 seconds timeout, so close the connection
//                        ch.write( ByteBuffer.wrap( "Good Bye\n".getBytes() ) );
                        log.debug( "Connection timed out, closing connection" );
                    }

                    log.debug( "End of conversation" );
                    try
                    {
                        // Close the connection if we need to
                        if( ch.isOpen() )
                        {
                            ch.close();
                        }
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }

                @Override
                public void failed(Throwable exc, Void att) {
                    ///...
                }

                void update(String message){
                    new CustomFile().write("tracker.txt", message);
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Bean
    public static void startSocket() {
        new Thread(()->{
            GPSServer server = new GPSServer();
            server.startListening();
        }).start();
//        GPSServer server = new GPSServer();
//        server.startListening();
////        try
//        {
//            // 60000
//            Thread.sleep( 30000 );
//        }
//        catch( Exception e )
//        {
//            e.printStackTrace();
//        }
    }
}
