package trafficInCity;

import java.util.Iterator;
import java.util.Random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

import jade.core.AID;
import main.ContextManager;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.gis.Geography;

public class Semaphore extends AgentTraffi {
	protected Geography<? extends AgentTraffi> space;
	protected int isGreen;
	protected int tickChange;
	protected int actualTick;
	protected Point pos;

	protected boolean a;

	public Semaphore(Geography<? extends AgentTraffi> space, Point pos, boolean isGreen, int tickChange) {
		super();
		this.space = space;
		this.isGreen = 10;
		this.tickChange = tickChange;
		Random r = new Random();
		int act = r.nextInt(tickChange);
		this.actualTick = act;

		this.pos = pos;
	}

	public int isSemaphoreGreen() {
		return isGreen;
	}

	public int getIsGreen() {
		return isGreen;
	}

	@ScheduledMethod(start = 1, interval = 500)
	public void run() {
		verifySemaphoreColor();

		Iterator<AgentTraffi> cars = ContextManager.agentTraffiContext.getObjects(Car.class).iterator();
		Coordinate pos = ContextManager.agentTraffiProjection.getGeometry(this).getCoordinate();
		String msg = pos.x + "%" + pos.y + "%" + isGreen;

		while (cars.hasNext()) {
			Car c = (Car) cars.next();

			AID receiver = (AID) c.getAID();
			sendMessage(receiver, msg);
		}
	}

	public void verifySemaphoreColor() {
		if (actualTick < tickChange)
			actualTick++;
		else {
			actualTick = 0;
			isGreen = Math.abs(isGreen - 10);
		}
	}
}
