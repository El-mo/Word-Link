/**
 * Name: Jacob Wolfe
 * E-mail address: jmwolfe@go.olemiss.edu
 * Course number and section: CSCI 211 Section 1
 * Program number and title: Program 2 "WordChain"
 * Due date: 10/15/14
 * Sources consulted: None
 * An honor code statement. Something like "In keeping with the Honor Code policies of the University of Mississippi, the School of Engineering, and the Department of Computer and Information Science, I affirm that I have neither given nor received inappropriate assistance on this programming assignment."
 * Version of Java used: Version 7 Update 67 (build 1.7.0_67-b01)
 * Description of the program: Takes two words from user and checks if there is a link by using words that differ from adjacent words by one letter
 * 
 * ::NOTE TO GRADER::
 *   Dr. Wilkins wanted to make sure that I told you that I went beyond the scope of the original project. Instead of returning the first word link the search
 * finds, I have made it return all of the shortest word chains (i.e. if the shortest possible word chain is 4 words long then it returns all the possible chains
 * that are 4 words long.) In order to make this efficient I had to modify the books version of LinearNode so I could keep up with the length of the current chain.
 * I also included a time taken for the search in the printed results to prove that my way was efficient. I tried to document all my changes the best I could so as
 * not to be confusing for you. 
 */

package wordchain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;


public class Driver {
	
	public static void main(String[] args)  {
		String start, end; //Starting and ending four letter word
		Scanner scan = new Scanner(System.in);
		boolean flag = true;//boolean for User Response Menu
		
		//Import four.txt into a HashSet using newDictionary method below
		HashSet<String> dictionary = null;
		try {
			dictionary = newDictionary();
		} catch (IOException e) {
			System.out.println("Warning: There is an error importing the dictionary file");
		}
		
		//User Response Menu
		while (flag){
			String userResponse;
			
			System.out.println("What four letter word would you like to start with?");
			start = stringCheck(scan.nextLine().toLowerCase().trim(),dictionary); //Method for data integrity 
			while(start == "ERROR")
			{
				System.out.println("Please insert new starting word");
				start = stringCheck(scan.nextLine().toLowerCase().trim(),dictionary);
			}
			
			System.out.println("What four letter word would you like to end with?");
			end = stringCheck(scan.nextLine().toLowerCase().trim(), dictionary); //Method for data integrity 
			while(end =="ERROR")
			{
				System.out.println("Please insert new ending word");
				end = stringCheck(scan.nextLine().toLowerCase().trim(), dictionary);
			}
			
			final double startTime = System.currentTimeMillis();
			findChain(start,end,dictionary); //Searches for word chains
			final double duration = (System.currentTimeMillis() - startTime)/1000; //Calculate how long it took to find the word chain
			System.out.println("The search took " + duration + " seconds");
			
			//Asks user if they want to look for another word chain. If not then exit the loop.
			System.out.println("\n\nWould you like to find another? (y/n)");
			userResponse = scan.nextLine().toLowerCase().trim();
			if( userResponse.charAt(0) != 'y')
				flag = false;
		}
		scan.close();
	}
	
	public static HashSet<String> newDictionary() throws IOException{
		//Finds how many words are in four.txt so the HashSet can have the proper amount of memory allocated
		int wordCount = 0; 
		BufferedReader br = new BufferedReader(new FileReader("four.txt"));
	    try {
	        while (br.readLine() != null) {wordCount++;}
	    } finally {
	        br.close();
	    }
		
	    HashSet<String> myWords = new HashSet<String>(wordCount); // create HashSet based on amount of words found
	    
	    //Reads through four.txt a second time to import the words into the data structure
	    br = new BufferedReader(new FileReader("four.txt"));
	    try {
	    	String line = br.readLine();
	        while (line != null) 
	        {
	        	myWords.add(line);
	        	line = br.readLine();
	        }
	    } finally {
	        br.close();
	    }
		
		return myWords; // return HashSet with all words from four.txt
	}
	
	public static String stringCheck (String input, HashSet<String> dictionary){
		// This method makes sure user input is four letters, is in the dictionary HashSet, and is lower case, and informs user of specific errors 
		boolean error = false;
		if (input.length() != 4){
			System.out.println("Error: Your word is not four letters");
			error = true;
		}
		else if (!dictionary.contains(input)){
			System.out.println("Error: Your word is not in my dictionary");
			error = true;
		}
		if (error)
			return "ERROR";
		else
			return input.toLowerCase();
	}
	
	public static void findChain(String start, String end, HashSet<String> dictionary){
		
		if(start.equals(end)) // Avoids the search if starting and ending words are the same
			System.out.println("Chain length: 0\nStarting and ending words are the same");
		
		else{
			LinearNode<String> startingNode = new LinearNode<String>(start); //LinearNode structure with starting word set as element
			HashSet<String> usedWords = new HashSet<String>(); //Used to keep up with all used words during search
			LinkedQueue<LinearNode<String>> openNodes = new LinkedQueue<LinearNode<String>>(); //Holds word chains that can potentially link to the ending word 
			LinkedQueue<LinearNode<String>> closedNodes = new LinkedQueue<LinearNode<String>>(); //Holds word chains that have completed a link to the ending word
			boolean foundShortest = false; //Boolean to mark when all of the shortest word chains have been found
			int smallestNode = -1; //Will hold the value of the value of the minimal number of words a word chain can have 
			
			usedWords.add(start);
			openNodes.enqueue(startingNode);
			
			while(!openNodes.isEmpty()){
				LinearNode<String> currentNode = openNodes.dequeue(); //Dequeues the last found openNode to search for a word link
				String currentWord = currentNode.getElement(); 
				usedWords.add(currentWord); //Ensures we find all possible links without getting a word previously used

				for(int i = 0; i <currentWord.length();i++){
					char[] temp = currentWord.toCharArray();//temp resets to currentWord every time it changes what letter position it iterates
					
					for(int j = (int)'a'; j<=(int)'z';j++){ 
						temp[i] = (char) j;
						
						//Checks if a combination of letters is in my dictionary and if it's been used before
						if(!usedWords.contains(String.valueOf(temp)) && dictionary.contains(String.valueOf(temp))){
							LinearNode<String> newNode = new LinearNode<String>(String.valueOf(temp)); //Makes a LinearNode to hold the word found
							newNode.setNext(currentNode); //Links the word found to the original word used
							
							if(String.valueOf(temp).equals(end)){
								//If smaillestNode hasn't been set then set it to the length of this word chain and add it to the closedNodes queue
								if(smallestNode == -1){
									smallestNode = newNode.getNumLinks();//*Note* getNumLinks is a method I added to LinearNode to search more efficiently
									closedNodes.enqueue(newNode);
								}
								//If smallestNode has been found then check if word chain is the same length
								else if(newNode.getNumLinks() == smallestNode)
									closedNodes.enqueue(newNode);
								//If word chain is bigger then previous word chains then there is no more need to search
								else
									foundShortest = true;
							}
							//If the word found isn't the ending word then add to the openNodes queue for further searching
							else
								openNodes.enqueue(newNode);
						}
					}
				}
				//if the shortest word chains have been found then break the while loop
				if(foundShortest)
					break;
			}
			
			//If we found any word chains
			if(!closedNodes.isEmpty()){
				
				//Because grammar
				if(closedNodes.size() == 1)
					System.out.println("\nFound 1 word chain that is " + (smallestNode) + " words long:\n");
				else
					System.out.println("\nFound " + closedNodes.size() + " word chains that are " + (smallestNode) + " words long:\n");
				
				// Dequeues all nodes from closed nodes
				while(!closedNodes.isEmpty()){
					Stack<String> printNode = new Stack<String>();
					printNode.add(end);//Stack structure for printing from starting word to ending word
					LinearNode<String> wordChain = closedNodes.dequeue();
					
					//While not at the end of the word chain add each element to the Stack for printing
					while(!wordChain.endOfNode()){ //*Note* I added the endOfNode() method to LinearNode to simplify my code 
						LinearNode<String> temp = wordChain.getNext();
						String currentString = temp.getElement();
						printNode.add(currentString);
						wordChain = temp;
					}
					//Prints out the word chain
					while(printNode.size()>0)
						System.out.println("|"+printNode.pop()+"|");
					System.out.println("\n");
				}
				
			}
			//If there was not any word chains saved in the queue print that nothing was found
			else
				System.out.println("No word chain found");
		}
	}

}
