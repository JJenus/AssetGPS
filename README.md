# Asset Tracker TCP connectivity

- TCP connection class:
    <br>src/main/com.example.AssetGPS/GPSServer
```
// Start connection and listen to port
TcpNetServerConnectionFactory connectionFactory()

// Initialize incoming connection
TcpReceivingChannelAdapter inbound(AbstractServerConnectionFactory connectionFactory)

// Setup message queue
PollableChannel receiver()

// Recieve message as a string
ObjectToStringTransformer transformer()

// Message channel
MessageChannel messageChannel()

// Recieve and utilize incoming messages
void service(String inData)
```

### Ignore configuration 
meant for realtime websocket testing with frontend