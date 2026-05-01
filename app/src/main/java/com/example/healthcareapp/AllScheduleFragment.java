package com.example.healthcareapp;
import com.example.healthcareapp.ScheduleHelper;
import com.example.healthcareapp.Schedule;
import java.util.List;

public class AllScheduleFragment extends BaseScheduleFragment {
    @Override
    public List<Schedule> getSchedulesList() {
        return ScheduleHelper.getSchedules(getContext(), email);
    }
}