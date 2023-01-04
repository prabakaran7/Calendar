package org.tem.calendar.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DayViewModel {

    private final List<LocalDate> list = new ArrayList<>();
    private static final int SIZE = 5;

    public DayViewModel(LocalDate date){
        for(int st = -2; st < 2; st++){
            list.add(date.plusDays(st));
        }
    }

    public void shiftForward(){
        LocalDate date = list.get(list.size() - 1);
        list.clear();
        for(int index = 0; index < SIZE; index++){
            list.add(date.plusDays(index));
        }
    }

    public void shiftBackward(){
        LocalDate date = list.get(0);
        list.clear();
        for(int index = SIZE - 1; index >= 0; index--){
            list.add(date.minusDays(index));
        }
    }

    public List<LocalDate> getList(){
        return list;
    }
}
