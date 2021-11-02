package org.lt.conquer.utils;

public class RandomNum
{
    private static int int_lastRandom;
    private static float float_lastRandom;

    public static int random(int min, int max)
    {
        int_lastRandom = (int) (Math.random() * (max - min + 1)) + min;
        return int_lastRandom;
    }

    public static float random(float min, float max)
    {
        float_lastRandom = (float) (Math.random() * (max - min + 1)) + min;
        return float_lastRandom;
    }

    public static int getLastIntRandom()
    {
        return int_lastRandom;
    }
    public static float getLastFloatRandom() { return float_lastRandom; }
}
