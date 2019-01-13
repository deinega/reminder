package com.torontodjango.reminder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.Comparable;
import java.util.Calendar;

import android.content.Intent;
import android.content.Context;

public class Task implements Comparable<Task>{

    private long id;
    private String name;
    private long date; // start date
    private boolean enabled;
    private long occurence; // next alarm date

    // used while populating intent
    private String prefix = "com.torontodjango.task";

    public Task()
    {
        id = 0;
        name = "";
        date = System.currentTimeMillis();
        enabled = true;
        update();
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public long getDate()
    {
        return date;
    }

    public void setDate(long date)
    {
        this.date = date;
        update();
    }

    public boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void toIntent(Intent intent)
    {
        intent.putExtra(prefix + ".id", id);
        intent.putExtra(prefix + ".name", name);
        intent.putExtra(prefix + ".date", date);
        intent.putExtra(prefix + ".enabled", enabled);
        intent.putExtra(prefix + ".occurence", occurence);
    }

    // reserved for the case if there are multiple alarms for the same task
    public void update()
    {
        occurence = date;
    }

    public void fromIntent(Intent intent)
    {
        id = intent.getLongExtra(prefix + ".id", 0);
        name = intent.getStringExtra(prefix + ".name");
        date = intent.getLongExtra(prefix + ".date", 0);
        enabled = intent.getBooleanExtra(prefix + ".enabled", true);
        occurence = intent.getLongExtra(prefix + ".occurence", 0);
        update();
    }

    // reserved for multiple alarms for the same task
    public long getNextOccurence()
    {
        return occurence;
    }

    public boolean getOutdated()
    {
        return occurence < System.currentTimeMillis();
    }

    // used to sort while displaying tasks
    public int compareTo(Task other)
    {
        final long thisNext = getNextOccurence();
        final long thatNext = other.getNextOccurence();
        final int BEFORE = -1;
        final int EQUAL = 0;
        final int AFTER = 1;

        if (this == other)
            return EQUAL;

        if (thisNext > thatNext)
            return AFTER;
        else if (thisNext < thatNext)
            return BEFORE;
        else
            return EQUAL;
    }

    public void serialize(DataOutputStream dos) throws IOException
    {
        dos.writeLong(id);
        dos.writeUTF(name);
        dos.writeLong(date);
        dos.writeBoolean(enabled);
        dos.writeLong(occurence);
    }

    public void deserialize(DataInputStream dis) throws IOException
    {
        id = dis.readLong();
        name = dis.readUTF();
        date = dis.readLong();
        enabled = dis.readBoolean();
        occurence = dis.readLong();
        update();
    }
}
