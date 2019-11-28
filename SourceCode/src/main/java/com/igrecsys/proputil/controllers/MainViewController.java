package com.igrecsys.proputil.controllers;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.igrecsys.proputil.Utils.PropertyUtil;
import com.igrecsys.proputil.dto.DtoCompareResult;
import com.igrecsys.proputil.dto.DtoMergeResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class MainViewController {

    @GetMapping("/")
    public String main(Model model) {
        return "index"; //view
    }

    @PostMapping("/process")
    public String process(HttpSession session, @RequestParam(name = "src1", required = true, defaultValue = "") String src1, @RequestParam(name = "src2", required = true, defaultValue = "") String src2,@RequestParam(name = "submit", required = true, defaultValue = "") String mode, Model model) {

      if(mode.equals("compare"))
      {

          DtoCompareResult dtoCompareResult = null;
          try {
              dtoCompareResult = PropertyUtil.compare(src1,src2);
          } catch (Exception e) {
              e.printStackTrace();
          }
          model.addAttribute("dataresult",dtoCompareResult);

          return  "compare";

      }
      else {

          DtoMergeResult dtoMergeResult;
          dtoMergeResult = null;
          try {
              dtoMergeResult = PropertyUtil.merge(src1,src2);
          } catch (Exception e) {
              e.printStackTrace();
          }
          model.addAttribute("dataresult",dtoMergeResult);


          return "merge";
      }
    }
}
