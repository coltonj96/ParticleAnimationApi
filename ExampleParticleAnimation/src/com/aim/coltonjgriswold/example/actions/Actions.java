package com.aim.coltonjgriswold.example.actions;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAAction;

public class Actions extends PAAction {
    
    private int[] a;

    public Actions() {
	a = new int[] { 0, 0 };
    }

    @Override
    public void run(PAObject object) {
	if (a[0] < 10) {
	    object.move(0.0, 0.1, 0);
	    a[0] += 1;
	}
	if (a[0] == 10 && a[1] < 10) {
	    object.move(0.0, -0.1, 0.0);
	    a[1] += 1;
	}
	if (a[0] == 10 && a[1] == 10)
	    a = new int[] { 0, 0 };
	object.rotate(0.0, 2.0, 0.0);
    }
}
