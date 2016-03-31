 /***************************************************************************
 *	FILE: DSALinkedList.java													   
 *	AUTHOR: Connor Beardsmore - 15504319								  
 *	UNIT: DSA120 Assignment S2- 2015  														   
 *	PURPOSE: General LinkedList class storing Objects
 *	LAST MOD: 20/10/15	
 *  REQUIRES: java.util.Iterator						   
 ***************************************************************************/
package connorLib;
import java.util.Iterator;

public class DSALinkedList implements Iterable
{
    //CLASSFIELDS
    private int numNodes;
    private DSALinkedList.DSAListNode head;
    private DSALinkedList.DSAListNode tail;
 
//---------------------------------------------------------------------------- 
             //PRIVATE CLASS, PROMOTES INFORMATION HIDING
             private class DSAListNode
            {
                //Class Fields
                public Object value;
                public DSAListNode next;
                public DSAListNode prev;
         
            //----------------------------------------------------------------
                //CONSTRUCTOR Alternate
                //IMPORT: inValue (Object)
         
                public DSAListNode( Object inValue )
                {
                    value = inValue;
                    next = null;
                    prev = null;
                }
             
            }
            //END OF PRIVATE DSAListNode CLASS
 
//----------------------------------------------------------------------------
            //PRIVATE CLASS, PROMOTES INFORMATION HIDING            
            private class DSALinkedListIterator implements Iterator
            {
                private DSALinkedList.DSAListNode iterNext;
                //------------------------------------------------------------
                public DSALinkedListIterator(DSALinkedList list)
                {
                    iterNext = list.head;
                }
                //------------------------------------------------------------
                public boolean hasNext()
                {
                    return (iterNext != null);
                }
                //------------------------------------------------------------
                public Object next()
                {
                    Object value;
                    if ( iterNext == null )
                    {
                        value = null;
                    }   
                    else
                    {
                        value = iterNext.value;
                        iterNext = iterNext.next;
                    } 
                    return value;
                }
                //------------------------------------------------------------
                public void remove()
                {
                    throw new UnsupportedOperationException("not supported");
                }
            }

//----------------------------------------------------------------------------
    //CONSTRUCTOR Default
     
    public DSALinkedList()
    {
        numNodes = 0;
        head = null;
        tail = null;
    }
//----------------------------------------------------------------------------
    //ACCESSOR getLength
    //EXPORT: numNodes (int)

    public int getLength()
    {
        return numNodes;
    }    
//----------------------------------------------------------------------------
    //MUTATOR insertFirst
    //IMPORT: newValue (Object)
    //PURPOSE: Insert New Element Into Start Of List
 
    public void insertFirst( Object newValue )
    {
        //Allocation New Node Using Node Constructor
        DSAListNode newNode = new DSAListNode(newValue);
         
        //If Empty, Set Tail To Node
        if ( isEmpty() )
        {
            tail = newNode;
        }
        else
        {
            newNode.next = head;
            head.prev = newNode;
        }   
 
        //Set Head to new node regardless
        head = newNode;  
        numNodes++;   
    }
//----------------------------------------------------------------------------
    //MUTATOR insertLast
    //IMPORT: newValue (Object)
    //PURPOSE: Insert New Element Into End Of List
 
    public void insertLast( Object newValue )
    {
        //Allocate New Node
        DSAListNode newNode = new DSAListNode(newValue);
         
        //If Empty, Set Tail To Node
        if ( isEmpty() )
        {
            head = newNode;
        }
        else
        {
            newNode.prev = tail;
            tail.next = newNode;
        }   
 
        //Set Tail to new node regardless
        tail = newNode;   
        numNodes++;                 
    }
//----------------------------------------------------------------------------
    //MUTATOR insertSorted
    //IMPORT: newValue (Object)
    //PURPOSE: Insert New Element Into Correct Place in list based on compareTo
 
    public void insertSorted( Object newValue )
    {
        //Allocation New Node Using Node Constructor
        DSAListNode newNode = new DSAListNode(newValue);
        boolean done = false;
        //If Empty, Set Tail To Node
        if ( isEmpty() )
        {
            tail = newNode;
            head = newNode;
        }
        else if ( ((Carton)(tail.value)).compareTo( (Carton)newValue ) <= 0 )
        {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }   
        else if ( head.next == null )
        {
            head = newNode;
            head.next = tail;
            tail.prev = newNode;  
        }         
        else
        {
            DSAListNode currNode = head;
            while ( (currNode != null) && (done == false) )
            {
                Carton item = (Carton)currNode.value;
                if ( (item).compareTo(  (Carton)newValue  ) >= 0 )
                {
                    if ( currNode == head )
                    {
                        head.prev = newNode;
                        newNode.next = head;
                        head = newNode;
                    }

                    (currNode.prev).next = newNode;
                    newNode.prev = currNode.prev;
                    newNode.next = currNode;
                    currNode.prev = newNode;
                    done = true;
                } 
                currNode = currNode.next;   
            }    
        }   
        numNodes++;
    }    
//---------------------------------------------------------------------------
    //ACCESSOR isEmpty
    //EXPORT: empty (boolean)
    //PURPOSE: Check if List is Empty (No Elements)
 
    public boolean isEmpty()
    {
        return ( numNodes == 0 );
    }
//----------------------------------------------------------------------------
    //ACCESSOR peekFirst
    //EXPORT: nodeValue (Object)
    //PURPOSE: View Value Within 1st Element of List
 
    public Object peekFirst()
    {
        //Can't peek if the list is empty
        if ( isEmpty() )
        {
            throw new IllegalStateException("Can't Peek Empty List");   
        }

        return head.value;   
    }
//----------------------------------------------------------------------------
    //ACCESSOR peekLast
    //EXPORT: nodeValue (Object)
    //PURPOSE: View Value Within Last Element of List
 
    public Object peekLast()
    {
        //Can't peek if the list is empty
        if ( isEmpty() )
        {
            throw new IllegalStateException("Can't Peek Empty List");   
        }

        return tail.value;   
    }
//----------------------------------------------------------------------------
    //MUTATOR removeFirst
    //EXPORT: nodeValue (Object)
    //PURPOSE: Remove First Element From Start of List
 
    public Object removeFirst()
    {
        Object nodeValue = null;
 
        if ( isEmpty() )
        {
            throw new IllegalStateException("Can't Remove If List Empty");
        }
        //List is only one node
        if ( head == tail )
        {
            tail = null;
        }
        //Covers all other situations
        nodeValue = head.value;
        head = head.next;   
 
        numNodes--;   
        return nodeValue;
    }
//----------------------------------------------------------------------------
    //MUTATOR removeLast
    //EXPORT: nodeValue (Object)
    //PURPOSE: Remove Last Element From End of List
 
    public Object removeLast()
    {
        Object nodeValue = null;
     
        if ( isEmpty() )
        {
            throw new IllegalStateException("Can't Remove If List Empty");
        }
 
        nodeValue = tail.value;
        
        //List is only one node 
        if ( head == tail )
        {
            head = null; 
            tail = null;
        }
        else
        {
            DSALinkedList.DSAListNode prevNode = head;
            while ( prevNode.next != tail )
            {
                prevNode = prevNode.next;
            }   
 
            tail = prevNode;
            tail.next = null;
        }   
             
        numNodes--;    
        return nodeValue;
    }
//---------------------------------------------------------------------------
    //MUTATOR removeSpecific
    //IMPORT: item (Object) 
    //PURPOSE: Remove node from list whose values matches memory address of item

   public void removeSpecific(Object item)
    {
        if ( isEmpty() )
        {
            throw new IllegalStateException("Can't Remove If List Empty");
        }
        if ( head == tail )
        {
            head = null; 
            tail = null;
            numNodes--;
        }  
        else if ( head.value == item )
        {
            removeFirst();
        }  
        else if ( tail.value == item )
        {
            removeLast();
        }  
        else
        {
            boolean done = false;
            DSAListNode currNode = head.next;
            while ( (currNode != null) && (done == false) )
            {
                if ( currNode.value == item )
                {
                    (currNode.prev).next = currNode.next;
                    (currNode.next).prev = currNode.prev;
                    done = true;
                }  
                currNode = currNode.next;  
            }
            numNodes--;
            if ( done == false )
            {
                throw new IllegalStateException("Item doesnt exist in list");
            }    
        } 
    }
//----------------------------------------------------------------------------    
    //IMPERATIVE iterator
    //EXPORT: New iterator
    //PURPOSE: Wraps Iterator constructor method to allow outer classes to 
    //          construct an instance of the private inner class

    public Iterator iterator()
    {
        return new DSALinkedListIterator(this);
    }
//----------------------------------------------------------------------------
}