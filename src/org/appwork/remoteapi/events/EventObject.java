/**
 * Copyright (c) 2009 - 2013 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.remoteapi.events.json
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.remoteapi.events;

/**
 * @author daniel
 * 
 */
public interface EventObject {

    public String getCollapseKey();

    public Object getEventdata();

    public String getEventid();

    public EventPublisher getPublisher();
}
