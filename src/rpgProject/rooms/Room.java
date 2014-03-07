package rpgProject.rooms;

import org.jsfml.window.event.Event;

public interface Room {
	public void run();
	
	public void input(Event e);
}
