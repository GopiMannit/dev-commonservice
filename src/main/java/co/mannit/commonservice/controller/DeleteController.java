package co.mannit.commonservice.controller;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.mannit.commonservice.common.Response;
import co.mannit.commonservice.pojo.BaseReqParam;
import co.mannit.commonservice.service.DeleteResource;

@RestController
public class DeleteController {

	private static final Logger logger = LogManager.getLogger(DeleteController.class);
			
	@Autowired
	private DeleteResource deleteResource;
	
	/*@DeleteMapping("eDelete")
	public Response<?> deleteResource(@RequestParam("domain")String domain, @RequestParam("subdomain")String subdomain, @RequestParam("resourceId")String resourceId) throws Exception {
		logger.debug("<deleteResource> domain {} subdomain {} resourceId {}",domain,subdomain, resourceId);
		
		deleteResource.deleteResource(domain, subdomain, resourceId);
		
		return Response.buildSuccessMsg(200, "Deleted Successfully", null);
	}*/
	
	@DeleteMapping("eDelete")
	public Response<?> deleteResource(@Validated BaseReqParam reqParam, @RequestParam Map<String, String> requestParams) throws Exception {
		logger.debug("<deleteResource> reqParam {} requestParams {}",reqParam, requestParams);
		
		long count = deleteResource.deleteResource(reqParam, requestParams);
		
		return Response.buildSuccessMsg(200, "Deleted Successfully", String.format("%s records deleted successfully", count));
	}
}
