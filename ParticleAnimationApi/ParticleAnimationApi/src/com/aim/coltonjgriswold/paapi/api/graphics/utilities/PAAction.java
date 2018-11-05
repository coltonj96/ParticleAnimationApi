package com.aim.coltonjgriswold.paapi.api.graphics.utilities;

import com.aim.coltonjgriswold.paapi.api.graphics.geometry.PAObject;

public abstract interface PAAction {

    /**
     * Actions to run upon update of the object in an animation
     * 
     * @param object
     *            The object this action belongs to
     */
    public void run(PAObject object);
}
