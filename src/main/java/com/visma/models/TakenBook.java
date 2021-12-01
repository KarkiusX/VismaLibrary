package com.visma.models;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TakenBook {

    private String Who;
    private long Period;

    public TakenBook(String Who, long Period)
    {
        this.Who = Who;
        this.Period = Period;
    }
}
