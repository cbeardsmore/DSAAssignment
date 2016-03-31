 /***************************************************************************
 *	FILE: BinarySearchTree.java													   
 *	AUTHOR: Connor Beardsmore - 15504319								  
 *	UNIT: DSA120 Assignment S2- 2015  														   
 *	PURPOSE: Stores a Binary Tree in Memory with Generic key and value
 *	LAST MOD: 18/10/15	
 *  REQUIRES: java.util					   
 ***************************************************************************/
package connorLib;
import java.util.*;

public class BinarySearchTree<K extends Comparable<K>, V> 
{
//----------------------------------------------------------------------------
	//PRIVATE INNER CLASS TreeNode
	//COMPRESSED FOR SIMPLICITY

	public class TreeNode<K extends Comparable<K>, V> 
	{
		private K key;
		private V value;
		private TreeNode<K,V> leftChild;
		private TreeNode<K,V> rightChild;

		public TreeNode(K inKey, V inValue)
		{
			if ( inKey == null )
			{	
				throw new IllegalArgumentException("Key cannot be null");
			}	
			key = inKey;
			value = inValue;
			leftChild = null;
			rightChild = null;	
		}
		public K getKey()
		{
			return key;
		}
		public V getValue()
		{
			return value;
		}
		public TreeNode<K,V> getLeft()
		{
			return leftChild;
		}
		public void setLeft( TreeNode<K,V> newLeft )
		{
			leftChild = newLeft;
		}
		public TreeNode<K,V> getRight()
		{
			return rightChild;
		}
		public void setRight( TreeNode<K,V> newRight )
		{
			rightChild = newRight;
		}
		public String toStrings()
		{
			return ("Key: " + key + " Value: " + value);
		}
	}
//----------------------------------------------------------------------------
	//CLASSFIELDS
	private TreeNode<K,V> root;
//----------------------------------------------------------------------------
	//DEFAULT Constructor

	public BinarySearchTree()
	{
		root = null;
	}
//----------------------------------------------------------------------------
	//ACCESSOR find
	//IMPORT: key (String)
	//EXPORT: value (Object)
	//PURPOSE: Wrapper Method. Kickstarts Recursive Find

	public V find(K key)
	{
		return findRec( key, root );
	}

	//ACCESSOR findRec
	//IMPORT: key (String), currNode (TreeNode)
	//EXPORT: value (Object)
	//PURPOSE: Recursively Traverses Tree to Find Specific Node

	private V findRec(K key, TreeNode<K,V> currNode)
	{
		V value = null;

		//Element doesn't exist in list
		//Throwing an exception limits flexibility, avoid it here
		if ( currNode == null )
		{	
			value = null;
		}	
		//Base Case: Element Found. Return it
		else if ( key.equals(currNode.getKey()) )
		{
			value = currNode.getValue();
		}
		//Follow Left Child Recursively
		else if ( key.compareTo(currNode.getKey()) < 0 )
		{
			value = findRec( key, currNode.getLeft() );
		}	
		//Follow Right Child Recursively
		else
		{	
			value = findRec( key, currNode.getRight() );	
		}	
		return value;		
	}
//----------------------------------------------------------------------------
	//MUTATOR insert
	//IMPORT: key (String), value (Object)
	//EXPORT: status (int). 0 = success. -1 = failure
	//PURPOSE: Wrapper method, kickstarts recursive insert

	public int insert(K key, V value)
	{	
		int status = 0;
		try 
		{	
			root = insertRec( key, value, root );
		}
		catch(IllegalStateException e)
		{	
			status = -1;
		}
		return status;
	}

	//MUTATOR insertRec
	//IMPORT: key (String), value (Object), currNode (TreeNode)
	//EXPORT: updateNode (TreeNode)
	//PURPOSE: Recursively inserts New Node Into Tree At Bottom Level

	//THIS IS INEFFICIENT. COULD DO EASIER ITERATIVELY
	//HAVE KEPT SINCE IT WAS IN THE LECTURE SLIDES

	private TreeNode<K,V> insertRec(K key, V value, TreeNode<K,V> currNode)
	{
		TreeNode<K,V> updateNode = currNode;

		//Create New Node At Bottom Level
		if ( currNode == null )
		{	
			updateNode = new TreeNode<K,V>( key, value );
		}	

		//Key Already Exists in Tree
		else if ( key.equals( currNode.getKey() ) )
		{	
				throw new IllegalStateException("Key Already Exists in Tree");
		}	
		//Remake parent links. Pretty much unrequired if using iterative method
		else if ( key.compareTo( currNode.getKey() ) < 0 )
		{
			currNode.setLeft( insertRec( key, value, currNode.getLeft() ) );	
		}

		else 
		{
			currNode.setRight( insertRec( key, value, currNode.getRight() ) );	
		}

		return updateNode;
	}

//----------------------------------------------------------------------------
	//MUTATOR delete
	//IMPORT: key (String)
	//PURPOSE: Wrapper method, kickstarts recursive insert

	public void delete(K key) 
	{
		root = deleteRec( key, root );
	}	

	//MUTATOR deleteRec
	//IMPORT: key (String), currNode (TreeNode)
	//EXPORT: updateNode (TreeNode)
	//PURPOSE: Recursively Deletes A Given Node From The Tree

	private TreeNode<K,V> deleteRec( K key, TreeNode<K,V> currNode )
	{
		TreeNode<K,V> updateNode = currNode;

		//Can't Delete If Element Doesn't Exist In Tree
		if ( currNode == null )
			throw new NoSuchElementException("Element not in tree. Cannot Delete");

		//Base Case. Found The Node To Delete
		else if ( key.equals( currNode.getKey() ) )
			updateNode = deleteNode( key, currNode );

		//Recurse Left
		else if ( key.compareTo( currNode.getKey() ) < 0 )
			currNode.setLeft( deleteRec( key, currNode.getLeft() ) );	

		//Recurse Right
		else 
			currNode.setRight( deleteRec( key, currNode.getRight() ) );	

		return updateNode;		
	}
//----------------------------------------------------------------------------
	//MUTATOR deleteNode
	//IMPORT: key (String), deNode (TreeNode)
	//EXPORT: updateNode (TreeNode)
	//PURPOSE: RDeletes Given Node From Tree, Fixes Required Links

	private TreeNode<K,V> deleteNode( K key, TreeNode<K,V> delNode )
	{
		TreeNode<K,V> updateNode = null;

		//No Children - Simply Delete
		if ( ( delNode.getLeft() == null ) && ( delNode.getRight() == null ) )
			updateNode = null;

		//Left Child - Adopt Orphan 
		else if ( ( delNode.getLeft() != null ) && ( delNode.getRight() == null ) )
			updateNode = delNode.getLeft();

		//Right Child - Adopt Orphan
		else if ( ( delNode.getLeft() == null ) && ( delNode.getRight() != null ) )
			updateNode = delNode.getRight();

		//Two Children
		else 
		{
			//Sort Out The Successor 
			updateNode = promoteSucc( delNode.getRight() );

			//No Cycles
			if ( updateNode != delNode.getRight() )
			{	
				//Update Right
				updateNode.setRight( delNode.getRight() );
			}	

			//Update Left
			updateNode.setLeft( delNode.getLeft() );			
		}	

		return updateNode;
	}
//----------------------------------------------------------------------------
	//MUTATOR promoteSucc
	//IMPORT: currNode (TreeNode)
	//EXPORT: successor (TreeNode)
	//PURPOSE: Finds Successor To Promote In Node Deletion 

	private TreeNode<K,V> promoteSucc( TreeNode<K,V> currNode )
	{
		TreeNode<K,V> successor = currNode;

		if ( currNode.getLeft() != null )
		{
			successor = promoteSucc( currNode.getLeft() );

			if ( successor == currNode.getLeft() )
			{
				currNode.setLeft( successor.getRight() );
			}	
		}	

		return successor;
	}
//----------------------------------------------------------------------------
	//ACCESSOR calcHeight
	//EXPORT: height (int)
	//PURPOSE: Wrapper Method, kickstarts height recursive height calculation

	public int calcHeight()
	{
		return heightRec( root );
	}

	//heightRec
	//IMPORT: currNode (TreeNode)
	//EXPORT height
	//PURPOSE: Recursively calculate height of binary tree

	private int heightRec( TreeNode<K,V> currNode )
	{
		int height, leftHt, rightHt;

		//Base Case - no more along this branch
		if ( currNode == null )
			height = -1;
		else
		{
			//Calc left and right subheights from here
			leftHt = heightRec( currNode.getLeft() );
			rightHt = heightRec( currNode.getRight() );

			//Get highest of the two branches
			if ( leftHt > rightHt )
			{
				height = leftHt + 1;
			}	
			else
			{
				height = rightHt + 1;
			}	
		}	

		return height;
	}
//----------------------------------------------------------------------------
	//traverse
	//IMPORT: traverseType (int)
	//PURPOSE: Traverse Tree And Output Data in pre/in/post order

	public void traverse( int traverseType )
	{
		switch ( traverseType )
		{
			case 1: System.out.print("\nPreOrder Traversal: ");
					preOrder(root);
					break;
			case 2: System.out.print("\nInOrder Traversal: ");
					inOrder(root);
					break;
			case 3: System.out.print("\nPostOrder Traversal: ");
					postOrder(root);
					break;										
		}
		System.out.println();
		System.out.println();		
	}

	//preOrder
	//IMPORT: localRoot (TreeNode)
	//PURPOSE: Recursively Prints PreOrder of Binary Tree

	private void preOrder( TreeNode<K,V> localRoot )
	{
		if ( localRoot != null )
		{
			System.out.print( localRoot.value + " ");
			preOrder( localRoot.getLeft() );
			preOrder( localRoot.getRight() );			
		}	
	}

	//inOrder
	//IMPORT: localRoot (TreeNode)
	//PURPOSE: Recursively Prints InOrder of Binary Tree

	private void inOrder( TreeNode<K,V> localRoot )
	{
		if ( localRoot != null )
		{
			inOrder( localRoot.getLeft() );
			System.out.print( localRoot.value + " " );			
			inOrder( localRoot.getRight() );			
		}	
	}

	//postOrder
	//IMPORT: localRoot (TreeNode)
	//PURPOSE: Recursively Prints PostOrder of Binary Tree

	private void postOrder( TreeNode<K,V> localRoot )
	{
		if ( localRoot != null )
		{
			postOrder( localRoot.getLeft() );
			postOrder( localRoot.getRight() );		
			System.out.print( localRoot.value + " " );				
		}	
	}
//----------------------------------------------------------------------------
	//printTree
	//PURPOSE: Wrapper method to recursively call printSubTree

	public void printTree()
	{
    	printSubTree(root, "");
    }

    //printSubTree
    //IMPORT: root (TreeNode), indent (String)
    //PURPOSE: Recursively prints Binary Tree in readable format

    private void printSubTree(TreeNode<K,V> root, String indent)
    {  
    	if(root != null)
        {
            if(root.getLeft() != null)
            {
            	printSubTree(root.getLeft(), indent + "      ");
            	System.out.println(indent + "     /");
            }

         	System.out.println(indent + root.toStrings());

            if(root.getRight() != null)
            {
                System.out.println(indent + "     \\");
                printSubTree(root.getRight(), indent + "      ");
            }
      	}
   	}

//----------------------------------------------------------------------------
}	