package com.nsind.ns.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nsind.ns.Constant.PanchayatData;

@RestController
@RequestMapping("/kgpi")
public class KgpiController {

	@GetMapping("/test")
    public String getblk() throws Exception {
		return "testApis";
    }
	
    @GetMapping("/blocks")
    public Set<String> getAllBlocks() {
        return PanchayatData.DATA.keySet();
    }

    // 2) Panchayats by block name (case-insensitive)
    @GetMapping("/panchayat/{blockName}")
    public List<String> getPanchayatListByBlockName(@PathVariable String blockName) {
        Map<String, String> villages = PanchayatData.DATA.get(PanchayatData.norm(blockName));
        if (villages != null) {
            return new ArrayList<>(villages.keySet());
        }
        return Collections.emptyList();
    }

    // 3) Code by panchayat name (case-insensitive)
    @GetMapping("/code/{panchayatName}")
    public String getPanchayatCodeByName(@PathVariable String panchayatName) {
        String code = PanchayatData.findCodeByName(panchayatName);
        return (code != null) ? code : "Panchayat not found!";
    }
    
}
