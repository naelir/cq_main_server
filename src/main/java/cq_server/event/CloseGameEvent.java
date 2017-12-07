/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cq_server.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "CLOSEGAME")
@XmlAccessorType(XmlAccessType.FIELD)
public final class CloseGameEvent extends BaseEvent {
	public CloseGameEvent() {
		super(EventType.CLOSEGAME);
	}
}
