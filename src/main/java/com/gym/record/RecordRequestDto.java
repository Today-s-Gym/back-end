package com.gym.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RecordRequestDto {
    private String content;
}
