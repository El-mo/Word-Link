/**
 * Name: Jacob Wolfe
 * E-mail address: jmwolfe@go.olemiss.edu
 * Course number and section: CSCI 211 Section 1
 * Program number and title: Program 2 "WordChain"
 * Due date: 10/15/14
 * Sources consulted: None
 * An honor code statement. Something like "In keeping with the Honor Code policies of the University of Mississippi, the School of Engineering, and the Department of Computer and Information Science, I affirm that I have neither given nor received inappropriate assistance on this programming assignment."
 * Version of Java used: Version 7 Update 67 (build 1.7.0_67-b01)
 * Description of the program: Modified version of LinearNode
 * 
 * ::NOTE TO GRADER::
 *   Dr. Wilkins wanted to make sure that I told you that I went beyond the scope of the original project. Instead of returning the first word link the search
 * finds, I have made it return all of the shortest word chains (i.e. if the shortest possible word chain is 4 words long then it returns all the possible chains
 * that are 4 words long.) In order to make this efficient I had to modify the books version of LinearNode so I could keep up with the length of the current chain.
 * I also included a time taken for the search in the printed results to prove that my way was efficient. I tried to document all my changes the best I could so as
 * not to be confusing for you. 
 */

package wordchain;

/**
 * Represents a node in a linked list.
 * 
 * @author Java Foundations
 * @version 4.0
 */
public class LinearNode<T>
{
    private LinearNode<T> next;
    private T element;
    private int numLinks; //Keeps track of node depth
 
    /**
     * Creates an empty node.
     */
    public LinearNode()
    {
        next = null;
        element = null;
        numLinks = 1; //set to 1 when created
	}
    
  
 
    /**
     * Creates a node storing the specified element.
     * @param elem element to be stored
     */
    public LinearNode(T elem)
    {
        next = null;
        element = elem;
        numLinks = 1; //set to 1 when created
    }
 
    /**
     * Returns the node that follows this one.
     * @return reference to next node
     */
    public LinearNode<T> getNext()
    {
        return next;
    }
 
    /**
     * Sets the node that follows this one.
     * @param node node to follow this one
     */
    public void setNext(LinearNode<T> node)
    {
        next = node;
        numLinks = node.getNumLinks() + 1;
    }
 
    /**
     * Returns the element stored in this node.
     * @return element stored at the node
     */
    public T getElement()
    {
        return element;
    }
 
    /**
     * Sets the element stored in this node.
     * @param elem element to be stored at this node
     */
    public void setElement(T elem)
    {
        element = elem;
    }
    
    /**
     * Checks if current node is the end of a set of nodes
     * @return true if node.next == null
     */
    public boolean endOfNode(){
    	if(next == null)
    		return true;
    	else
    		return false;
    }
    
    /**
     * Returns the number of links until node.next == null
     * @return number of links until node.next == null
     */
    public int getNumLinks(){
    	return numLinks;
    }
}
