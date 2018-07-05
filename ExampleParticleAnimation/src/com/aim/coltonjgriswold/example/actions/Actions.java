package com.aim.coltonjgriswold.example.actions;

import org.bukkit.entity.Player;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAAction;

public class Actions extends PAAction {
    
    private int[] a;
    private Player b;

    public Actions(Player player) {
	a = new int[] { 0, 0 };
	b = player;
    }

    @Override
    public void run(PAObject object) {
	if (a[0] < 10) {
	    object.moveRelative(0, 0.1, 0);
	    a[0] += 1;
	}
	if (a[0] == 10 && a[1] < 10) {
	    object.moveRelative(0, -0.1, 0);
	    a[1] += 1;
	}
	if (a[0] == 10 && a[1] == 10)
	    a = new int[] { 0, 0 };
	object.rotate(0.0, 2.0, 0.0);
	object.setLocation(b.getEyeLocation().add(0, 1, 0));
    }
}
