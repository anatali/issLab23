package unibo.springIntro23.STOMP;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

//@Controller
public class HIControllerSTOMP {
	@Value("${spring.application.name}")
	String appName;

	@RequestMapping("/")
	public String entry(Model model) {
		model.addAttribute("arg", appName);
		return "GuiStomp"; //usa wsStomp.js
	}
	@RequestMapping("/minimal")
	public String entryPlus(Model model) {
		model.addAttribute("arg", appName);
		//System.out.println("HIController | entryMinimal   "  );
		return "GuiStompMinimal"; //usa wsStompMinimal.js
	}

	@MessageMapping("/unibo")	 
	@SendTo("/demoTopic/output")
	public OutputMessage elabInput(InputMessage message) throws Exception {
		System.out.println("HIController | elabInput  message=" + message.getInput());
		return new OutputMessage("elaborated by HIControllerSTOMP-" +
				HtmlUtils.htmlEscape(message.getInput()) + " ");
		//HtmlUtils.htmlEscape prepara il nome nel messaggio di input
		// ad essere reso nel DOM lato client
	}
	
}
