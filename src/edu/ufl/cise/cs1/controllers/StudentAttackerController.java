package edu.ufl.cise.cs1.controllers;
import game.controllers.AttackerController;
import game.models.*;
import java.awt.*;
import java.util.List;

public final class StudentAttackerController implements AttackerController {
	public void init(Game game) {
	}

	public void shutdown(Game game) {
	}

	public int update(Game game, long timeDue) {
		int action;


		boolean thereAreVuln = false;


		Node closestPill = game.getAttacker().getTargetNode(game.getPillList(), true);
		int distanceFromPill = game.getAttacker().getLocation().getPathDistance(closestPill);

		boolean inLair = true;
		int shortestDistanceDF = game.getAttacker().getLocation().getPathDistance(game.getDefender(0).getLocation());
		Defender closestDF = game.getDefender(0);
		Defender furthestDF = game.getDefender(3);
		int longestDistanceDF = game.getAttacker().getLocation().getPathDistance(game.getDefender(3).getLocation());






//			closest PP
		int distanceFromPP1 = Integer.MAX_VALUE;
		Node closestPP1 = null;
		if(game.getPowerPillList().size() != 0) {
			for (Node n : game.getPowerPillList()) {
				int temp = game.getAttacker().getLocation().getPathDistance(n);
				if (temp < distanceFromPP1) {
					closestPP1 = n;
					distanceFromPP1 = temp;
				}
			}
		}

		// closest defender location
		for(Defender defender: game.getDefenders()) {
			// waits until defender are out of lair.
			if (defender.getLairTime() <= 0) {
				int temp = game.getAttacker().getLocation().getPathDistance(defender.getLocation());
				if (temp < shortestDistanceDF) {
					shortestDistanceDF = temp;
					closestDF = defender;

				}
				if (defender.isVulnerable()) {
					thereAreVuln = true;
				}

			}
		}

		// furthest defender location
		for(Defender defender: game.getDefenders()) {
			// waits until defender are out of lair.
			if (defender.getLairTime() <= 0) {
				int temp = game.getAttacker().getLocation().getPathDistance(defender.getLocation());
				if (temp > longestDistanceDF) {
					longestDistanceDF = temp;
					furthestDF = defender;
				}

			}
			if (defender.isVulnerable()) {
				thereAreVuln = true;
			}



		}

//		powerpilll exist
		if(game.getPowerPillList().size() != 0)  {
			if(thereAreVuln) {
				if (closestDF.isVulnerable()) {
					if (distanceFromPP1 < 10) {
						return game.getAttacker().getNextDir(closestPP1, false);
					}
					return game.getAttacker().getNextDir(closestDF.getLocation(), true);
				}
			}
				else {
//			waiting for defender before eating Power Pill.
					if(distanceFromPP1 < 5 & shortestDistanceDF > 10) {
						return game.getAttacker().getReverse();
					}
/// finding closest Power Pill
					return game.getAttacker().getNextDir(closestPP1, true);
				}


		}

		// no more power pills
		if(game.getPowerPillList().size() == 0){
//		eating vulnerable defender
			if(closestDF.isVulnerable()) {

				return action = game.getAttacker().getNextDir(closestDF.getLocation(), true);
			}

//			avoiding defenders
			if (shortestDistanceDF < 7 ) {

				if (distanceFromPill < 5) {
					return game.getAttacker().getNextDir(closestPill, true);
				}
				if(longestDistanceDF < 20) {
					return  game.getAttacker().getNextDir(furthestDF.getLocation(), false);
				}
				return  game.getAttacker().getNextDir(closestDF.getLocation(), false);
			}

		}
// finding closest Pill
		return game.getAttacker().getNextDir(closestPill, true);
	}
}