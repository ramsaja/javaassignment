package com.afs.models;

public class ModuleClass {
    private String id; // e.g., "CS101-A"
    private String moduleId;
    private String day;
    private String time;
    private String room;

    public ModuleClass(String id, String moduleId, String day, String time, String room) {
        this.id = id;
        this.moduleId = moduleId;
        this.day = day;
        this.time = time;
        this.room = room;
    }

    public String getId() { return id; }
    public String getModuleId() { return moduleId; }
    public String getDay() { return day; }
    public String getTime() { return time; }
    public String getRoom() { return room; }

    public String toFileString() {
        return String.join("|", id, moduleId, day, time, room);
    }
}
