package trafficInCity;

import java.util.Iterator;

import environment.Junction;
import environment.Road;
import jade.core.AID;
import javafx.util.Pair;
import main.ContextManager;
import repast.simphony.engine.schedule.ScheduledMethod;

public class Radio extends AgentTraffi {
	private RoadTrafficIntensity carTrafficInfo;
    private RoadTrafficIntensity carPassed;
	public Radio() {
		carTrafficInfo = new RoadTrafficIntensity();
		carPassed = new RoadTrafficIntensity();

	}

	@ScheduledMethod(start = 1, interval = 550)
	public void updateAllRoadWeight() {
		Iterator<Road> roads = ContextManager.roadContext.getObjects(Road.class).iterator();
		int load, passed;

		while (roads.hasNext()) {
			load = 0;
			passed = 0;

			Road current = roads.next();
			Junction source = current.getJunctions().get(0);
			Junction target = current.getJunctions().get(1);

			load = carTrafficInfo.getRoadLoad(new Pair<Junction, Junction>(source, target));
			load += carTrafficInfo.getRoadLoad(new Pair<Junction,Junction> (target,source));
			current.setLoad(load);
			
			passed = carPassed.getRoadLoad(new Pair<Junction, Junction> (source,target));
			passed += carPassed.getRoadLoad(new Pair<Junction,Junction> (target,source));

			current.setPassed(passed);
		}

		Iterator<AgentTraffi> cars = ContextManager.agentTraffiContext.getObjects(LowestTrafficCar.class).iterator();
		String msg = generateMessage();

		while (cars.hasNext()) {
			Car c = (Car) cars.next();

			AID receiver = (AID) c.getAID();
			sendMessage(receiver, msg);
		}
	}

	private String generateMessage() {
		return carTrafficInfo.data();
	}

	public synchronized void addIndexjunctionsCars(Pair<Junction, Junction> road) {
		carTrafficInfo.addIndexjunctionsCars(road);
		carPassed.addIndexjunctionsCars(road);
	}

	public synchronized void subIndexjunctionsCars(Pair<Junction, Junction> road) {
		carTrafficInfo.subIndexjunctionsCars(road);
	}
}
