package rpgProject;

public abstract class Action {
	private int frame, maxFrame;
	private String name, description;
	private Range range;

	public Action(int i, String n, String d) {
		frame = 0;
		maxFrame = i;
		name = n;
		description = d;
	}
	
	public void setRange(Range r)
	{
		range = r;
	}

	public void perform(Unit u) // We use a unit as an argument in order for all
								// other actions to work
	{
		frame++; // Probably figure out a better way to do it later. Maybe a
					// interface would work instead of abstract?
	}

	public String[] load() {
		return null;
	}

	public void reset() {
		frame = 0;
	}

	public int getFrame() {
		return frame;
	}

	public boolean isDone() {
		return frame >= maxFrame;

	}

	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Range getRange()
	{
		return range;
	}

}
