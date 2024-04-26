package com.pagepal.capstone.dtos.zoom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlanBase {
    public String type;
    public int hosts;
    public int usage;
    public int pending;
}
