package rpgProject;

import java.util.Comparator;

public class DepthComparator implements Comparator<BattleUnit>{

	@Override
	public int compare(BattleUnit o1, BattleUnit o2) {
		return o1.getDepth() - o2.getDepth();
	}

}
