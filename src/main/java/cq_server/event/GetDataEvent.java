/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package cq_server.event;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import cq_server.model.BaseEvent;
import cq_server.model.EventType;

@XmlRootElement(name = "GETDATA")
@XmlAccessorType(XmlAccessType.PROPERTY)
public final class GetDataEvent extends BaseEvent {
	private String query;

	public GetDataEvent() {
		super(EventType.GETDATA);
	}

	public GetDataEvent(final String query) {
		super(EventType.GETDATA);
		this.query = query;
	}

	@XmlAttribute(name = "QUERY")
	@NotNull
	public String getQuery() {
		return this.query;
	}

	public void setQuery(final String query) {
		this.query = query;
	}
}
