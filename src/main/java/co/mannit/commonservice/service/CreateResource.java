package co.mannit.commonservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.mannit.commonservice.ServiceCommonException;
import co.mannit.commonservice.common.MongokeyvaluePair;
import co.mannit.commonservice.common.util.ValidationUtil;
import co.mannit.commonservice.dao.ResourceDao;

@Service
public class CreateResource {

	private static final Logger logger = LogManager.getLogger(CreateResource.class);
	
	@Autowired
	private ResourceDao resourceDao;
	
	public String createResource(String domain, String subDomain, String userId, String resourceJson) throws ServiceCommonException {
		logger.debug("<createResource>");
		
		if(!Optional.of(ValidationUtil.validateDomainAndId(domain, subDomain, userId)).get()) {
			throw new ServiceCommonException("106");
		}
		
		List<MongokeyvaluePair<? extends Object>> lstKeyValuePairs = new ArrayList<>();
		lstKeyValuePairs.add(new MongokeyvaluePair<String>("domain", domain));
		lstKeyValuePairs.add(new MongokeyvaluePair<String>("subdomain", subDomain));
		lstKeyValuePairs.add(new MongokeyvaluePair<ObjectId>("userId", new ObjectId(userId)));
		
		String json = resourceDao.insertDocument(resourceJson, lstKeyValuePairs);
		
		logger.debug("</createResource>");
		return json;
	}
}
