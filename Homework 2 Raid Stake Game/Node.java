import java.util.ArrayList;
import java.util.List;
 
public class Node 
{
	List<Node> children = new ArrayList<>();
	String[][] boardstatetree = new String[homework.size][homework.size];
	//private final Node parent;
	int row = 0, col = 0, score = 0, alpha = 0, beta = 0;
	String bestmove = "";
/*
	public Node(Node parent) 
	{
		this.parent = parent;
		//this.boardstatetree=null;
	}

	*/
	public List<Node> getChildren()
	{
		return children;
	}
/*
	public Node getParent() 
	{
		return parent;
	}
*/
	public void setBoard(String board[][]) 
	{
		//boardstatetree = new String[homework.size][homework.size];
		for (int i11 = 0; i11 < homework.size; i11++)
		{
			for (int j = 0; j < homework.size; j++)
			{
				this.boardstatetree[i11][j] = board[i11][j];
			}
		}
	}
}