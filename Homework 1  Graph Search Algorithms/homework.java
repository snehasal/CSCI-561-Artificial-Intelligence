import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
/**
 * @author Sneha Salvi
 * CSCI 560: Artificial Intelligence
 * SHowework1: Search Algorithms
 *
 */

public class homework {
	//static int dfscounter=0;
	static ArrayList<String> arr = new ArrayList<String>();
	static int no_lines = 0;
	static int fileucscount=0;
	private Map<String, LinkedHashSet<String>> map = new HashMap();
	public static Map<String,UCSDemo> mapucs = new HashMap();	 
    
	public void addEdge(String node1, String node2)
    {	LinkedHashSet<String> adjacent = map.get(node1);
        if (adjacent == null)
        {
            adjacent = new LinkedHashSet();
            map.put(node1, adjacent);
            
        }
        adjacent.add(node2);
    }	
    
    public LinkedList<String> adjacentNodes(String last)
    {
        LinkedHashSet<String> adjacent = map.get(last);
        if (adjacent == null)
        {
            return new LinkedList();
        }
        return new LinkedList<String>(adjacent);
    }
 	
    public static void main(String[] args) {
		String file = "C:\\input.txt"; //input file
		homework graph = new homework();
		try (BufferedReader in = new BufferedReader(new FileReader(file))) 
		{
			String str = null;
			ArrayList<String> lines = new ArrayList<String>();
			while ((str = in.readLine()) != null) 
			{
				lines.add(str.trim());
			}
			Object[] output = {};
			LinkedList<String> visited = new LinkedList();
			String[] linesArray = lines.toArray(new String[lines.size()]);
			String algo = linesArray[0];
			String start = linesArray[1];
			String goal = linesArray[2];
			if (start.equals(goal)) 
			{
				arr.add(goal);
				arr.add("0");
				output = arr.toArray();
				WriteOutput(output,null,null,null);
			}
			else
			{
	// storing path
				no_lines = Integer.parseInt(linesArray[3]);
				String[][] path = new String[no_lines][3];
				
				for (int x = 4; x < no_lines + 4; x++) 
				{
					String splitline[] = linesArray[x].split(" ");
					for (int temp = 0; temp < 3; temp++)
					{
							path[x - 4][temp] = splitline[(temp)];
					}
				}
	//add edges
				for(int x1=0;x1<path.length;x1++)		
				{
					graph.addEdge(path[x1][0], path[x1][1]);
				}
				
	//storing h(n)	    
				String[][]  sunday_path={};
				if(algo.trim().equals("A*"))
				{
						int sunday_lines = Integer.parseInt(linesArray[4 + no_lines]);
						sunday_path = new String[sunday_lines][2];
						for (int x1 = (no_lines + 5); x1 < linesArray.length; x1++) 
						{
							String splitline1[] = linesArray[x1].split(" ");
							for (int temp = 0; temp < 2; temp++)
							{
								sunday_path[(x1 - no_lines - 5)][temp] = splitline1[(temp)];
							}
						}
				}
				visited.add(start);
				switch (algo.trim())
				{
					case "BFS": 
					{
						funcBFS(graph,visited,goal);
						break;
					}
					case "DFS": 
					{
						funcDFS(graph,visited,goal);
						break;
					}
					case "UCS": 
					{
						funcUCS(graph, visited, goal, path);
						break;
					}
					case "A*": 
					{
						funcA(graph, visited, goal, path,sunday_path);
						break;
						
					}
				}
			} 
		}
		catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
 	private static void funcA(homework graph, LinkedList<String> visited, String goal, String[][] path, String[][] sunday_path) {

		 UCSDemo ucs = new UCSDemo();
		 Queue<String> open = new LinkedList<String>();
		 Queue<Object> closed= new LinkedList<Object>();
		 open.add(visited.getLast());
		 ucs.cost=0;
		 ucs.parent=null;
		 ucs.heuristiccost=0;
		 ucs.pathcost=0;
		 mapucs.put(visited.getLast(), ucs);
		 int cost=0,pathcost=0,hcostchild=0;
		 String currnode = "",parent="",child="";
		 LinkedList<String> children = null ;

		 do 
		 {
			 open=opensort(open,"Astar"); //sorting open queue
			 pathcost=0;
			 if(open.isEmpty())
				 return;
			 currnode = open.poll().toString();
			 if(currnode.equals(goal))
			 {
				WriteOutput(null,null,currnode,"Astar");
        	   	System.exit(0);
    	  	 }	
			
			children= (graph.adjacentNodes(currnode.toString()));
			while(!children.isEmpty())
			{
				child=children.removeFirst();				
				for(int i=0;i<sunday_path.length;i++)
				{	
					if(sunday_path[i][0].equals(child))
					{
						hcostchild = Integer.parseInt(sunday_path[i][1]);								
					} 
				}
				for(int row=0;row<path.length;row++)
				{
					if((path[row][0].equals(currnode)) && (path[row][1].equals(child)))
					{	
						cost=Integer.parseInt(path[row][2]);
					}
				}
				if((!open.contains(child) )&&(!closed.contains(child)))
				{
					open.add(child);
					parent=currnode;
					pathcost=(cost+(mapucs.get(parent).pathcost));
					mapucs.put(child,new UCSDemo(cost,parent,hcostchild,pathcost));
				}
				else if (open.contains(child))
				{
					if((mapucs.get(currnode).pathcost+cost)<(mapucs.get(child).pathcost))
					{
							parent=currnode;
							pathcost=(cost+(mapucs.get(parent).pathcost));
							mapucs.put(child,new UCSDemo(cost,parent,hcostchild,pathcost));
					}
				}
				else if (closed.contains(child))
				{
					if((mapucs.get(currnode).pathcost+cost)<(mapucs.get(child).pathcost))
					{
						closed.remove(child);
						open.add(child);
						mapucs.remove(child);
						parent=currnode;
						pathcost=(cost+(mapucs.get(parent).pathcost));
						mapucs.put(child,new UCSDemo(cost,parent,hcostchild,pathcost));
					}
				} 
			}
		closed.add(currnode);		
		}while(!open.isEmpty());
 	}
	
 	private static Queue<String> opensort(Queue<String> open1,String str) {
 		String[][] temp=new String[open1.size()][2];
		String temp1="",temp11="",	opencost="";
			for(int i=0;i<temp.length;i++)
			{
				temp[i][0]=open1.poll();
				if(str.equals("Astar"))
					opencost=(mapucs.get(temp[i][0]).heuristiccost+mapucs.get(temp[i][0]).pathcost+"");
				else
					opencost=(mapucs.get(temp[i][0]).cost+"");
				temp[i][1]=opencost;
			}
			for(int j =0;j<=temp.length-1;j++)
			{
				for(int k=j+1;k<=temp.length-1;k++)
				{
					if(Integer.parseInt(temp[j][1])>Integer.parseInt(temp[k][1]))
					{
						temp1=temp[j][0];
						temp[j][0]=temp[k][0];
						temp[k][0]=temp1;
						
						temp11=temp[j][1];
						temp[j][1]=temp[k][1];
						temp[k][1]=temp11;
					}
				}
			}
			open1.clear();
			for(int i=0;i<=temp.length-1;i++)
			{
				open1.add(temp[i][0]);
			}
			return open1;
		}

	public static void funcUCS(homework graph, LinkedList<String> visited, String goal, String[][] path) 
 	{		
 		 UCSDemo ucs = new UCSDemo();
		 Queue<String> open = new LinkedList<String>();
		 Queue<Object> closed= new LinkedList<Object>();
		 open.add(visited.getLast());
		 ucs.cost=0;
		 ucs.parent=null;
		 mapucs.put(visited.getLast(), ucs);
		 int cost1=0,cost=0;
		 String currnode = "",parent="",child="";
		 LinkedList<String> children = null ;
		 do 
		 {
			 open=opensort(open,"ucs");
			 if(open.isEmpty())
				 return;
			 currnode = open.poll().toString();
			 if(currnode.equals(goal))
			 {
				WriteOutput(null,null,currnode,"ucs");
         	   	System.exit(0);
     	  	 }	
			children= (graph.adjacentNodes(currnode.toString()));
			while(!children.isEmpty())
			{
				child=children.removeFirst();				
				for(int row=0;row<path.length;row++)
				{
					if((path[row][0].equals(currnode)) && (path[row][1].equals(child)))
					{	
						cost=Integer.parseInt(path[row][2]);
						break;
					}
					
				}
				if((!open.contains(child) )&&(!closed.contains(child)))
				{
					open.add(child);
					cost1=Integer.parseInt(cost+ mapucs.get(currnode).cost+"");
					parent=currnode;
					mapucs.put(child,new UCSDemo(cost1,parent));
				}
				else if (open.contains(child))
				{
					if((mapucs.get(currnode).cost+cost)<(mapucs.get(child).cost))
					{
							cost1=Integer.parseInt(cost+ mapucs.get(currnode).cost+"");
							parent=currnode;
							mapucs.put(child,new UCSDemo(cost1,parent));
					}
				}
				else if (closed.contains(child))
				{
					if((mapucs.get(currnode).cost+cost)<(mapucs.get(child).cost))
					{
						closed.remove(child);
						open.add(child);
						mapucs.remove(child);
						cost1=Integer.parseInt(cost+ mapucs.get(currnode).cost+"");
						parent=currnode;
						mapucs.put(child,new UCSDemo(cost1,parent));
					}				 
				} 
			}
			closed.add(currnode);		
	}while(!open.isEmpty());
}	
 	
	private static void answer(String x, PrintWriter writer, String string)
	{	
		int printcost=0;
		if(string.equals("ucs"))
			printcost=(mapucs.get(x).cost);
		else if(string.equals("Astar"))
			printcost=(mapucs.get(x).pathcost);
		if(mapucs.get(x).parent==null)
		{
			writer.print(x + " ");
			writer.println(printcost);	
			return; 
		}
		else
			answer(mapucs.get(x).parent,writer,string);
		writer.print(x + " ");
		writer.println(printcost);		
	}

	private static void funcDFS(homework graph, LinkedList<String> visited, String goal) {
				
		
		LinkedList<String> frontier = new LinkedList();
		LinkedList<String> tempstack = new LinkedList();
		Map <String,String> parentmap=new HashMap();
		frontier.add(visited.getLast());
		parentmap.put(frontier.get(0),null);
		String node="";
		LinkedList<String> explored = new LinkedList();
		
		do
		{
			node=frontier.removeFirst();
			if(node.equals(goal))
			{
				visited.clear();
        	   	visited.addFirst(node);
				while(true)
				{
					node = parentmap.get(node);
					visited.addFirst(node);
					if(parentmap.get(node) == null)
					{
						WriteOutput(null,visited,null,null);
						System.exit(0);
					}
				}
			}
			explored.add(node);
			LinkedList<String> nodes = graph.adjacentNodes(node);
	        for (String node1 : nodes)
	        {	
	    		if((!explored.contains(node1))&&(!frontier.contains(node1)))
	        	{
	        		tempstack.add(node1);
	           	}
	        		
	        }	    
			
	        while(!tempstack.isEmpty())
	        {  	
	        	parentmap.put(tempstack.getLast(), node+"");
	        	frontier.addFirst(tempstack.removeLast());	        	
	        }
	    }while(!frontier.isEmpty());
}	 

	private static void funcBFS(homework graph, LinkedList<String> visited, Object goal) {
	

		LinkedList<String> frontier = new LinkedList();
	//	LinkedList<String> tempstack = new LinkedList();
		Map <String,String> parentmap=new HashMap();
		frontier.add(visited.getLast());
		parentmap.put(frontier.get(0),null);
		String node="";
		LinkedList<String> explored = new LinkedList();
		
		do
		{
			node=frontier.removeFirst();
			if(node.equals(goal))
			{
				visited.clear();
        	   	visited.addFirst(node);
				while(true)
				{
					node = parentmap.get(node);
					visited.addFirst(node);
					if(parentmap.get(node) == null)
					{
						WriteOutput(null,visited,null,null);
						System.exit(0);
					}
				}
			}
			explored.add(node);
			LinkedList<String> nodes = graph.adjacentNodes(node);
	        for (String node1 : nodes)
	        {	
	    		if((!explored.contains(node1))&&(!frontier.contains(node1)))
	        	{
	    			parentmap.put(node1, node+"");
		        	frontier.add(node1);	        	
	           	}
	        		
	        }	    
			
	    }while(!frontier.isEmpty());

		
	}
	
	public static void WriteOutput(Object[] output,LinkedList<String> visited, String x, String string) 
	{
		
		try {	
		String destpath = "C:\\Users\\Sneha Salvi\\Desktop\\output.txt";
		File file1 = new File(destpath);		
		if (!file1.exists())
		{	
			file1.delete();
		}
		PrintWriter writer = new PrintWriter(new FileOutputStream(destpath));
			if(visited!=null)
			{
				int count =0;
				for (String node : visited)
				{
					writer.println(node +" " +count);					
					count++;
				}
			}
			if(output!=null)
			{		
				for (int i = 0; i < output.length; i += 2)
				writer.println(output[i] + " " + output[i + 1]);
			}
			if((x!=null)&&(string.equals("ucs")))
			{            	answer(x,writer,string);
			
			}
			if((x!=null)&&(string.equals("Astar")))
			{            	answer(x,writer,string);
			
			}
			writer.close();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	}