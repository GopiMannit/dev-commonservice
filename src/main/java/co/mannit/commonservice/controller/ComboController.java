package co.mannit.commonservice.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.mannit.commonservice.common.Response;
import co.mannit.commonservice.service.CollectionService;

@RestController
public class ComboController {

	private static final Logger logger = LogManager.getLogger(ComboController.class);
	
	@Autowired
	private CollectionService collectionService;
			
	@GetMapping("redcombo")
	public Response<?> getCollection(@RequestParam("domain") String domain,
			@RequestParam("subdomain") String subdomain, @RequestParam("collname") String collectionName) throws Exception {
		logger.debug("<createResource> domain {} subdomain {} collname {} json{}",domain,subdomain, collectionName);
		
		Document doc = collectionService.retrieveCollection(domain, subdomain, collectionName);
		
		return Response.buildSuccessMsg(200, "Combo Value Retrieved Successfully", doc);
	}
}
