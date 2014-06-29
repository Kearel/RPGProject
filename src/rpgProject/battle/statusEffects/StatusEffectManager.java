package rpgProject.battle.statusEffects;

import java.util.ArrayList;
import java.util.Iterator;

import rpgProject.battle.Unit;

public class StatusEffectManager {
	private Unit target;
	public ArrayList<StatusEffect> statusEffects = new ArrayList<StatusEffect>();
	public StatusEffectManager(Unit target)
	{
		this.target = target;
	}
	public void run()
	{
		Iterator<StatusEffect> iterator = statusEffects.iterator();
		while(iterator.hasNext())
		{
			StatusEffect e = iterator.next();
			if(!e.run())
				iterator.remove();
		}
	}
	
	public void addEffect(Class<StatusEffect> ef, int stacks) throws InstantiationException, IllegalAccessException
	{
		for(StatusEffect e : statusEffects)
		{
			if(e.getClass() == ef)
			{
				e.addStack(stacks);
				return;
			}
		}
		StatusEffect effect = ef.newInstance();
		effect.setTarget(target);
		effect.addStack(stacks);
		statusEffects.add(effect);
	}
}
