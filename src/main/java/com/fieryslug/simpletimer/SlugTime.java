package com.fieryslug.simpletimer;

public class SlugTime {

    private int hour;
    private int minute;
    private int second;
    private int deci;

    public SlugTime(int h, int m, int s) {

        setTime(h, m, s, 0);

    }

    public boolean isZero() {

        return hour == 0 && minute == 0 && second == 0 && deci == 0;

    }

    public boolean decrementInDecis() {

        if(isZero())
            return false;

        if(deci == 0) {
            deci = 9;

            if(second == 0) {
                second = 59;

                if(minute == 0) {
                    minute = 59;
                    hour -= 1;
                }
                else {
                    minute -= 1;
                }
            }
            else {
                second -= 1;

            }

        }
        else {
            deci -= 1;
        }

        return true;
    }

    public boolean decrement() {

        if(isZero())
            return false;

        if(second == 0) {

            if(minute == 0) {

                hour -= 1;
                second = minute = 59;

            }
            else {

                minute -= 1;
                second = 59;

            }

        }
        else {

            second -= 1;

        }

        return true;

    }

    @Override
    public String toString() {

        return String.format("%d:%02d:%02d:%01d", hour, minute, second, deci);

    }

    public String toStringWithoutDecis() {

        int h = hour;
        int m = minute;
        int s = second;

        if(deci != 0) {

            s += 1;
            m += s / 60;
            s %= 60;
            h += m / 60;
            m %= 60;

        }

        String formattedHour = h < 10 ? ("0" + h) : ("" + h);

        return String.format(formattedHour + ":%02d:%02d", m, s);

    }

    public void setTime(int h, int m, int s, int cent) {

        if(h < 0 || m < 0 || m >= 60 || s < 0 || s >= 60 || cent < 0 || cent >= 10)
            throw new IllegalArgumentException();

        else {
            hour = h;
            minute = m;
            second = s;
            deci = cent;
        }

    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getDeci() {
        return deci;
    }

    @Override
    protected Object clone() {
        return new SlugTime(hour, minute, second);
    }
}
