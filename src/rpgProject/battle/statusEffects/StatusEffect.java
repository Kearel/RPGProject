package rpgProject.battle.statusEffects;

import rpgProject.battle.Unit;

public abstract class StatusEffect {
	private String name,description;
	private Unit target;
	private boolean refreshes;
	/*
	 * if this status effect doesn't stack
	 *  true - it refreshes on new stack
	 *  false - it doesn't refresh on a new stack
	 * otherwise
	 * 	true - it refreshes duration on every stack decay
	 *  false - loose all stacks on decay
	 */
	private int stacks, currentDuration, duration;
	/*
	 * if stacks is equal to -1, it does not stack.
	 */
	public StatusEffect(String name, String description, int duration)
	{
		this.name = name;
		this.description = description;
		this.duration = duration;
		currentDuration = duration;
	}
	
	public void setTarget(Unit u)
	{
		target = u;
		onCreate();
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public Unit getTarget()
	{
		return target;
	}
	
	public boolean run()
	{
		currentDuration--;
		if(currentDuration == 0)
			return removeStack();
		return true;
	}
	
	public boolean onCreate()
	{
		return true;
	}
	
	public boolean onEnd()
	{
		return true;
	}
	
	public void addStack(int amount)
	{
		if( stacks == -1 && refreshes)
		{
			currentDuration = duration;
		} else if(stacks != -1)
		{
			for(int i = 0; i < amount; i++)
				onCreate();
			stacks += amount;
			currentDuration = duration;
		}
	}
	
	public boolean removeStack()
	{
		if(stacks == -1 || !refreshes)
		{
			stacks = 0;
		} else {
			currentDuration = duration;
			stacks--;
		}
		return stacks != 0;
	}
	
	public int getStacks()
	{
		return stacks;
	}
}
