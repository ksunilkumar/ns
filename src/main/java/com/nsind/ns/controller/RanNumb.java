package com.nsind.ns.controller;

import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nsind.ns.Constant.PanchayatData;

@RestController
@RequestMapping("/rn")
public class RanNumb {

    @GetMapping("/getNum/{count}")
    public Set<Integer> getNum(@PathVariable Integer count) {
    	Random random = new Random();
        Set<Integer> numbers = new LinkedHashSet<>();

        // Keep adding until we have 15 unique numbers
        while (numbers.size() < count) {
            int num = random.nextInt(36) + 1; // 1 to 36
            numbers.add(num);
        }

        return numbers;
        
    }
}
