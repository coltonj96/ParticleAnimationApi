package com.aim.coltonjgriswold.example.actions;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;
import com.aim.coltonjgriswold.paapi.api.graphics.utilities.PAAction;

public class Actions extends PAAction {
    
    private PAObject a;
    private int[] b;

    public Actions(PAObject object) {
	a = object;
	b = new int[] { 0, 0, 0 };
    }

    @Override
    public void run() {
	if (b[0] < 8) {
	    a.rotateX(10.0);
	    b[0] += 1;
	}
	if (b[0] == 9 && b[1] < 8) {
	    a.rotateY(10.0);
	    b[1] += 1;
	}
	if (b[1] == 9 && b[2] < 8) {
	    a.rotateZ(10.0);
	    b[2] += 1;
	}
	if (b[2] == 9)
	    b = new int[] { 0, 0, 0 };
	a.draw();
    }
}
