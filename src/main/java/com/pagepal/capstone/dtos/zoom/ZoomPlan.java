package com.pagepal.capstone.dtos.zoom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ZoomPlan {
    public PlanBase plan_base;
    public PlanRecording plan_recording;
}