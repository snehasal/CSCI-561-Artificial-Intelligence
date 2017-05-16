
public class UCSDemo {
//public int cost;
public String parent;
public int heuristiccost,pathcost,cost;
UCSDemo(int cost,String parent)
{
	this.cost=cost;
	this.parent=parent;
}
UCSDemo(int cost,String parent,int heuristiccost,int pathcost)
{
	this.cost=cost;
	this.parent=parent;
	this.heuristiccost=heuristiccost;
	this.pathcost=pathcost;
}
public UCSDemo() {
	// TODO Auto-generated constructor stub
}
}
