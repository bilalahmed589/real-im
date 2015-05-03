# real-im
Real TIme Chat like whats app using PARSE as backened

1. Parse is used as backend
2. For real time chat PARSE PUSH is used.
3. When a user sends a message
      a. REST call sent to PARSE Push server 
      b. that in turn send broadcast to all the connected users to the channel.
      c. receiver receives the message and display
4. For image 
        a. is saved on cloud and 
        b. message is sent to PARSE push. 
        c. Parse send meesage to all the users . 
        d. So receiver receives the message get the image object id 
            and download the image.
