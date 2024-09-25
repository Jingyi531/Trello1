package com0.trellobackend;

import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Te{
    public static void main(String[] args) throws IOException{
        ServerSocket serverSock = null;
        try{
            // Create a ServerSocket object and bind it to port 50000
            serverSock = new ServerSocket(50000);
        }

        // In case the connection was unsuccessful
        catch (IOException ie){
            System.out.println("Can't listen on 50000");
            System.exit(1);
        }

        //Create a client socket object and wait for the connection from the client
        Socket link = null;
        System.out.println("Listening for connection ...");

        try {
            // Accept the connection if the client sends a request
            link = serverSock.accept();
        }
        // if connection fail, exit
        catch (IOException ie){
            System.out.println("Accept failed");
            System.exit(1);
        }

        System.out.println("Connection successful");
        System.out.println("Listening for input ...");

        // Create a PrintWritter object to send its output to the client
        PrintWriter output = new PrintWriter(link.getOutputStream(), true);

        // Create a BufferedReader to get input from the client
        BufferedReader input = new BufferedReader(new InputStreamReader(link.getInputStream()));

        // Receive data from the server
        String inputLine;
        int numberToGuess = -1;

        while ((inputLine = input.readLine())!=null){


            System.out.println("Server: " + inputLine);

            if(inputLine.equals("Bye")){
                output.println(inputLine);
                break;
            }

            // Start game
            if(inputLine.equalsIgnoreCase("Play?")){
                output.println("I have a number between 0 and 100. Can you guess it?");
                Random random = new Random();
                numberToGuess = random.nextInt(101);
            }
            else if(numberToGuess != -1){
                int inputNumber = Integer.parseInt(inputLine);
                if(inputNumber == numberToGuess){
                    output.println("You got it!");
                }
                else if(inputNumber < numberToGuess){
                    output.println("Guess Higher");
                }
                else{
                    output.println("Guess Lower");
                }
            }





        }

        // Close all the connections
        output.close();
        input.close();
        link.close();
        serverSock.close();
    }
}
