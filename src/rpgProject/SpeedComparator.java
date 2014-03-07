package rpgProject;

import java.util.Comparator;

public class SpeedComparator implements Comparator<Unit>{

	@Override
	public int compare(Unit arg0, Unit arg1) {
		return arg0.getSpeed() - arg1.getSpeed();
	}

}
