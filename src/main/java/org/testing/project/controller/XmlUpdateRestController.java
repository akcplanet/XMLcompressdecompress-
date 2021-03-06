package org.testing.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.testing.project.entity.Nugget;
import org.testing.project.service.XmlUpdateRestService;

@RestController
@RequestMapping("/xml")
@CrossOrigin(origins = "*")
public class XmlUpdateRestController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	XmlUpdateRestService xmlUpdateRestService;

	@PostMapping
	public @ResponseBody ResponseEntity<?> updateXMLNugget(Nugget nugget) {
		logger.info("Logging in @RestController Update Nugget : " + nugget);
		xmlUpdateRestService.updateXMLNugget(nugget);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping
	public @ResponseBody ResponseEntity<?> getXMLNugget(@RequestParam("cusip") String cusip,
			@RequestParam("invnum") String invnum, @RequestParam("portfolioName") String portfoliosPortfolioName,
			@RequestParam("touchCount") String touchCount, @RequestParam("inputFolderPath") String inputFolderPath,
			@RequestParam("outputFolderPath") String outputFolderPath,
			@RequestParam("nuggetCount") String nuggetCount) {
		Nugget nugget = new Nugget();
		nugget.setCusip(cusip);
		nugget.setInvnum(invnum);
		nugget.setPortfoliosPortfolioName(portfoliosPortfolioName);
		nugget.setTouchCount(touchCount);
		nugget.setInputFolderPath(inputFolderPath);
		nugget.setOutputFolderPath(outputFolderPath);
		nugget.setNuggetCount(Integer.valueOf(nuggetCount));
		logger.info("Logging in @RestController Update Nugget : " + nugget);
		xmlUpdateRestService.updateXMLNugget(nugget);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping("/main")
	public @ResponseBody ResponseEntity<?> mainXMLNugget() {
		Nugget nugget = new Nugget();
		nugget.setCusip("500TESTCUSIPTEST");
		nugget.setInvnum("500");
		nugget.setPortfoliosPortfolioName("500portfoliosPortfolioName");
		nugget.setTouchCount("500touchCount");
		nugget.setInputFolderPath("E:/xml2");
		nugget.setOutputFolderPath("E:/xml3/new/test");
		nugget.setNuggetCount(10);
		logger.info("Logging in @RestController Update Nugget : " + nugget);
		xmlUpdateRestService.updateXMLNugget(nugget);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
