import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class homework
{
	static String mode="",player1="",player2="",s="Stake",r="Raid";
	static int size=0,gamescore=0,counter=0;
	public static void main(String[] args)
	{
		String file = "C:\\input.txt";
		try (BufferedReader in = new BufferedReader(new FileReader(file)))
		{
			String str = null;
			ArrayList<String> lines = new ArrayList<String>();
			while ((str = in.readLine()) != null) 
			{
				lines.add(str.trim());
			}
			String[] linesArray = lines.toArray(new String[lines.size()]);
			size = Integer.parseInt(linesArray[0]);
			mode = linesArray[1];
			player1 = linesArray[2];
			player2 = player1.equals("X") ? "O" : "X";
			int depth = Integer.parseInt(linesArray[3]);
			int game[][] = new int[size][size];
			String boardstate[][] = new String[size][size];
			for (int x = 4; x < size + 4; x++) 
			{
				String splitline[] = linesArray[x].split(" ");
				for (int temp = 0; temp < size; temp++) 
				{
					game[x - 4][temp] = Integer.parseInt(splitline[(temp)]);
				}
			}
			for (int x1 = (size + 4); x1 < linesArray.length; x1++) 
			{
				char splitline1[] = linesArray[x1].toCharArray();
				for (int temp = 0; temp < size; temp++) 
				{
					boardstate[(x1 - 4 - size)][temp] = splitline1[temp] + "";
				}
			}
         	linesArray=null;
			Node root = new Node();
			root.setBoard(boardstate);
			switch (mode.trim())
			{
				case "MINIMAX":
				{
					Node goal = null;
					goal = Max_Value(game, 0, depth, root);
					WriteOutput(goal);
					break;
				}
				case "ALPHABETA": 
				{
					Node alpha = new Node();
					Node beta = new Node();
					alpha.score = Integer.MIN_VALUE;
					beta.score = Integer.MAX_VALUE;
					funcAlphaBeta(game, 0, depth, root, alpha, beta);
					break;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

   
	private static void funcAlphaBeta(int[][] game, int i, int depth, Node root, Node alpha, Node beta)
	{
		Node goal = Max_Value_Prune(game, 0, depth, root,alpha,beta);
		WriteOutput(goal);		
	}

	private static Node Max_Value_Prune(int[][] game, int d, int depth, Node node, Node alpha, Node beta) 
	{
		if(d==depth||termialState(node))
		{
			 node.score=Gamescore(game, node);
			 return node;
		}
		else
		{
			Node v=new Node();
			Node demo=null;
			v.score= Integer.MIN_VALUE;
			List<Node> MaxChildren = generateChild(node,d, game,"max");				
			for (Iterator<Node> iter = MaxChildren.iterator(); iter.hasNext();) 
			{
				counter++;
				demo = iter.next();
				iter.remove();
				Node temp = Min_Value_Prune(game, d + 1, depth, demo, alpha, beta);
				if (temp.score > v.score) 
				{
					v = demo;
					v.score = temp.score;
				}
				temp = null;
				if (v.score >= beta.score)
				{
					return v;
				}
				if (alpha.score < v.score)
				{
					alpha = v;
				}
			}
			return v;
		}
	}
	
	private static Node Min_Value_Prune(int[][] game, int d, int depth, Node node, Node alpha, Node beta) 
	{

		if(d==depth||termialState(node))
		{
			node.score=Gamescore(game, node);
			return node;
		}
		else
		{
			Node demo=null;
			Node v=new Node();
          	v.score=Integer.MAX_VALUE;
			List<Node> MaxChildren = generateChild(node,d, game,"min");
			for (Iterator<Node> iter = MaxChildren.iterator(); iter.hasNext();)
			{
				counter++;
				demo=iter.next();
				iter.remove();
				Node temp = Max_Value_Prune(game,d+1,depth,demo,alpha,beta);
				if (temp.score < v.score) 
				{
					v=demo;
					v.score=temp.score;
				}
				temp=null;
				if(v.score<=alpha.score)
				{
					return v;
				}
				if(beta.score>v.score)
					beta=v;
			}
			return v;
		}
	}

	private static int Gamescore(int[][] game, Node node)
	{
		int scorex=0,score0=0;
		String opponent=player1.equals("X")?"O":"X";
		for (int i = 0; i < size; i++) 
        {
		    for (int j = 0; j < size; j++) 
            {
              if(!node.boardstatetree[i][j].trim().equals("."))
              {
		    	
		    	if(node.boardstatetree[i][j].trim().equals(player1))
		    	{
		    		scorex+=game[i][j];
		     	}
		    	else if(node.boardstatetree[i][j].trim().equals(opponent))
		    	{	
		    		score0+=game[i][j];
		       	}
              }                
		     
            }
		}
		return (scorex-score0);
	}
	
	private static void WriteOutput(Node goal) {

		try
		{
			String destpath = "C:\\Users\\Sneha Salvi\\Desktop\\output.txt";
			File file1 = new File(destpath);
			if (!file1.exists())
			{
				file1.delete();
			}
			PrintWriter writer = new PrintWriter(new FileOutputStream(destpath));
			String colr = ((char) ('A' + goal.col)) + "";
			writer.println(colr + "" + (goal.row + 1) + " " + goal.bestmove);
			for (int i = 0; i < size; i++)
			{
				for (int j = 0; j < size; j++)
				{
					writer.print(goal.boardstatetree[i][j]);
				}
				writer.println();
			}
			writer.close();
		}
		catch (Exception e) 
		{
			System.out.println(counter);
			e.printStackTrace();
		}
	}
	
	private static void FindRaid(int[][] game,String currplayer, int i, int j, Node node)
	{
		String opponent=(currplayer.equals("X")?"O":"X");
		node.boardstatetree[i][j] = currplayer;
		node.row = i;
		node.col = j;
		node.bestmove = r;
		
		if (i > 0)
		{
			if (node.boardstatetree[i - 1][j].trim().equals(opponent)) 
			{
				node.boardstatetree[i - 1][j] = currplayer;
			}
		}
		if (i < size - 1)
		{
			if (node.boardstatetree[i + 1][j].trim().equals(opponent)) 
			{
				node.boardstatetree[i + 1][j] = currplayer;
			}
		}
		if (j > 0)
		{
			if (node.boardstatetree[i][j - 1].trim().equals(opponent)) 
			{
				node.boardstatetree[i][j - 1] = currplayer;
			}
		}
		if (j < size - 1) 
		{
			if (node.boardstatetree[i][j + 1].trim().equals(opponent)) 
			{
				node.boardstatetree[i][j + 1] = currplayer;
			}
		}
    }
	
	public static void PrintBoard(String[][] boardstate) {
			for(int i11=0;i11<homework.size;i11++)
		{
			for(int j=0;j<homework.size;j++)
			{
				System.out.print(boardstate[i11][j]);
			}
		System.out.println();
		}
		
	}
	
	private static Node Max_Value(int[][] game, int d,int depth,Node node)
	{
		if(d==depth||termialState(node))
		{
			 node.score=Gamescore(game,node);
			 return node;
		}
		else
		{
			Node best=null;
			Node demo=null;
			int bestSoFar = Integer.MIN_VALUE;
			List<Node> MaxChildren = generateChild(node,d, game,"max");
			for (Iterator<Node> iter = MaxChildren.iterator(); iter.hasNext();)
			{
				counter++;
				demo=iter.next();
				iter.remove();			
				Node temp = Min_Value(game,d+1,depth,demo);
				if(temp.score>bestSoFar)
				{
					bestSoFar = temp.score;
					best = demo;
					best.score = bestSoFar;			
				}
				temp=null;
			}
			return best;
		}
	}
		
	private static Node Min_Value(int[][] game,  int d,int depth, Node node)
	{
		if(d==depth||termialState(node))
		{
			node.score=Gamescore(game, node );
			return node;
		}
		else
		{
			Node best=null;
			Node demo=null;
			int bestSoFar = Integer.MAX_VALUE;
			List<Node> MaxChildren = generateChild(node,d, game,"min");
			for (Iterator<Node> iter = MaxChildren.iterator(); iter.hasNext();)
			{
				counter++;
				demo=iter.next();
				iter.remove();			
				Node temp = Max_Value(game,d+1,depth,demo);
				if (temp.score < bestSoFar)
				{
					bestSoFar = temp.score;
					best = demo;
					best.score = bestSoFar;
					
				}
				temp=null;
			}
			return best;
		}
}	
	
	public static boolean termialState(Node node) 
	{
		for(int i11=0;i11<homework.size;i11++)
		{
			for(int j=0;j<homework.size;j++)
			{
				if(node.boardstatetree[i11][j].equals("."))
					return false;
					
			}
		}
		return true;
	}

	private static List<Node> generateChild(Node parent,int d,int [][] game,String mode)
	{
		boolean raidneighbour=false,raidnopponentneighbour=false;
      	List<Node> raidChildren = new ArrayList<>();
		for (int i1 = 0; i1 < size; i1++)
		{
			for (int j1 = 0; j1 < size; j1++) 
			{	
				if(parent.boardstatetree[i1][j1].trim().equals("."))
				{  					
					Node node = new Node();
					node.setBoard(parent.boardstatetree);
					node.row=i1;
					node.col=j1;
					node.bestmove=s;                 	
					if(mode.equals("max"))
					{
						node.boardstatetree[i1][j1]=player1;                      
						raidneighbour=checkNeighbours(parent.boardstatetree,i1,j1,player1);
						raidnopponentneighbour=checkNeighbours(parent.boardstatetree,i1,j1,player2);
						if(raidneighbour && raidnopponentneighbour)
						{
							Node raidNode = new Node();
							raidNode.setBoard(parent.boardstatetree);
							FindRaid(game, player1, i1, j1, raidNode);
							raidChildren.add(raidNode);
						}
					}
					else
					{
						node.boardstatetree[i1][j1]=player2;	
                     	raidneighbour=checkNeighbours(parent.boardstatetree,i1,j1,player2);
						raidnopponentneighbour=checkNeighbours(parent.boardstatetree,i1,j1,player1);
						if(raidneighbour && raidnopponentneighbour)
						{
							Node raidNode = new Node();
							raidNode.setBoard(parent.boardstatetree);
							FindRaid(game,player2,i1,j1,raidNode);	
							raidChildren.add(raidNode);
						}
					}
					parent.getChildren().add(node);
                  	
				}
			}
		}
		if(!raidChildren.isEmpty())
		{
			Node demo=null;
			for (Iterator<Node> iter = raidChildren.iterator(); iter.hasNext();) 
			{
				demo = iter.next();
				iter.remove();
				parent.getChildren().add(demo);
				raidChildren=null;
			}
		}
		return parent.getChildren();
}
			
	private static boolean checkNeighbours(String[][] boardstate, int i, int j, String opponent) 
	{
		if(i>0)
		{ 
			if(boardstate[i-1][j].trim().equals(opponent))
			{
				return true;
			}
		}
		if(i<size-1)
		{
			if(boardstate[i+1][j].trim().equals(opponent))
			{
				return true;
			}
		}
		if(j>0)
		{
			if(boardstate[i][j-1].trim().equals(opponent))
			{
				return true;
			}
		}
		if(j<size-1)
		{
			if(boardstate[i][j+1].trim().equals(opponent))
			{
				return true;
			}		
		}
		return false;	
	}

}
