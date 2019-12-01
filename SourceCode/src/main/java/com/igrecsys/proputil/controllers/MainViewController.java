package com.igrecsys.proputil.controllers;

import com.igrecsys.proputil.Utils.PropertyUtil;
import com.igrecsys.proputil.dto.DtoCompareResult;
import com.igrecsys.proputil.dto.DtoMergeResult;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
public class MainViewController {

    @GetMapping("/")
    public String main(Model model) {
        return "index"; //view
    }

    @GetMapping("/clipboard")
    public String clipboard(Model model) {
        return "clipboard"; //view
    }

    @GetMapping("/upload")
    public String upload(Model model) {
        return "upload"; //view
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

    @PostMapping("/processfile")
    public String processUpload(HttpSession session, @RequestParam("src1") MultipartFile file1
            , @RequestParam("src2") MultipartFile file2
            , RedirectAttributes redirectAttributes
            , @RequestParam(name = "submit"
            , required = true
            , defaultValue = "") String mode, Model model) {

        if (file1.isEmpty() || file2.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }


        try {
            return PropertyUtil.processRequest(PropertyUtil.multipartToString(file1), PropertyUtil.multipartToString(file2), model, mode);
        } catch (IOException e) {
            return "error";
        }
    }

    @RequestMapping(value = "/products/download", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource downloadFile(@RequestParam(value = "id") String id) {

        return new FileSystemResource(new File(id));
    }
}
