package com.pagepal.capstone.dtos.zoom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlanRecording {
    public String type;
    public String free_storage;
    public String free_storage_usage;
    public String plan_storage;
    public String plan_storage_usage;
    public String plan_storage_exceed;
    public String max_exceed_date;
}
