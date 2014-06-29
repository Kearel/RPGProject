package rpgProject.battle;

import java.util.ArrayList;

public class Team {
	private ArrayList<Unit> team;
	
	public Team()
	{
		team = new ArrayList<Unit>();
	}
	
	public Team(ArrayList<Unit> t)
	{
		team = t;
	}
	
	public boolean onTeam(Unit u)
	{
		return team.contains(u);
	}
	
	public void add(Unit u)
	{
		team.add(u);
	}
	
	public void remove(Unit u)
	{
		team.remove(u);
	}
}
