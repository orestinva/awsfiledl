package com.inva.ui.events;

/**
 * Created by inva on 11/23/2016.
 */
public class Event {

    private String eventID;
    public enum Type{
        BUTTON
    };

    private Type type;

    public Event(String eventID, Type type){
        this.eventID = eventID;
        this.type = type;
    }

    public String getEventID() {
        return eventID;
    }

    public Type getType(){
        return type;
    }
}
