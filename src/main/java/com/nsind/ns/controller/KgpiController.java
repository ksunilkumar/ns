package com.nsind.ns.controller;

import java.io.IOException;
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
import com.nsind.ns.Constant.panchayatToBlockMap;

import jakarta.servlet.http.HttpServletResponse;

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
    
    @GetMapping("/getJobCard/{panchayatName}")
    public void getJobCard(@PathVariable String panchayatName,HttpServletResponse response) throws IOException {
    	String url1=getUrl1(panchayatName);
    	String url2=getUrl2(panchayatName);
    	System.out.println("url1: "+url1);
    	System.out.println("url2: "+url2);
    	
    	response.sendRedirect(url1);
    	 
    }
    
    private String getUrl1(String panchayatName) {
    	String panchCode= PanchayatData.findCodeByName(panchayatName);
      	String blkName=panchayatToBlockMap.getBlock(panchayatName);
      	String blkCode=PanchayatData.blockMap.get(blkName);
      	blkName=blkName.replace(" ", "+");
    	String panchname=panchayatName.replace(" ", "+");
    	String url1="https://nregastrep.nic.in/netnrega/IndexFrame.aspx?lflag=eng&District_Code=2011&district_name=KANGPOKPI&state_name=MANIPUR&state_Code=20&block_name="+blkName+"&block_code="+blkCode+"&fin_year=2025-2026&check=1&Panchayat_name="+panchname+"&Panchayat_Code="+panchCode;
    	return url1;
    }
    
    private String getUrl2(String panchayatName) {
    	String panchCode= PanchayatData.findCodeByName(panchayatName);
    	String url2="https://nregastrep.nic.in/netnrega/writereaddata/state_out/jobcardreg_"+panchCode+"_eng.html";
    	return url2;
    }
    
}
