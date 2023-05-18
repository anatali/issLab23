package unibo.springIntro23;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//@Controller
//@RequestMapping("/Api")
public class HIControllerAppl {
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("arg", appName);
        return "GuiNoImages";
    }
    //@GetMapping("/img")
    @RequestMapping("/img")
    public String alsoImg(Model model) {
        model.addAttribute("arg", appName);
        return "GuiAlsoImages";
    }
    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity(
                "HIControllerAppl ERROR " + ex.getMessage(),
                responseHeaders, HttpStatus.CREATED);
    }
}
