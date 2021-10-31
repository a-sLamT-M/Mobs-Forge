package org.lt.conquer.utils;

public class Random
{
    private static int lastRandom;
    public static int random(int min, int max)
    {
        lastRandom = (int) (Math.random() * (max - min + 1)) + min;
        return lastRandom;
    }

    public static int getLastRandom()
    {
        return lastRandom;
    }
}
