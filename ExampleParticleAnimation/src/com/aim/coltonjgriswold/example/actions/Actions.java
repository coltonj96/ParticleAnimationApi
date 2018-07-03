package com.aim.coltonjgriswold.example.actions;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAAction;

public class Actions extends PAAction {
    
    private PAObject a;
    private int[] b;

    public Actions(PAObject object) {
	a = object;
	b = new int[] { 0, 0 };
    }

    @Override
    public void run() {
	if (b[0] < 9) {
	    a.rotateZ(-10.0);
	    a.move(1.0, 0, 0);
	    b[0] += 1;
	}
	if (b[0] == 9 && b[1] < 9) {
	    a.rotateZ(10.0);
	    a.move(-1.0, 0, 0);
	    b[1] += 1;
	}
	if (b[0] == 9 && b[1] == 9)
	    b = new int[] { 0, 0 };
	a.draw();
    }
}
