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
public class UpdateResource {

	private static final Logger logger = LogManager.getLogger(UpdateResource.class);
	
	@Autowired
	private ResourceDao resourceDao;
	
	public String updateResource(String domain, String subDomain, String userId, String resourceId, String resourceJson) throws Exception {
		logger.debug("<updateResource>");
		
		if(!Optional.of(ValidationUtil.validateDomainAndId(domain, subDomain, userId)).get()) {
			throw new ServiceCommonException("106");
		}
		
		List<MongokeyvaluePair<? extends Object>> fields = new ArrayList<>();
		fields.add(new MongokeyvaluePair<String>("domain", domain));
		fields.add(new MongokeyvaluePair<String>("subdomain", subDomain));
		fields.add(new MongokeyvaluePair<ObjectId>("userId", new ObjectId(userId)));
		fields.add(new MongokeyvaluePair<ObjectId>("_id", new ObjectId(resourceId)));
		
		/*List<MongokeyvaluePair<? extends Object>> criteria = new ArrayList<>();
		criteria.add(new MongokeyvaluePair<String>("domain", domain));
		criteria.add(new MongokeyvaluePair<String>("subdomain", subDomain));
		criteria.add(new MongokeyvaluePair<ObjectId>("userId", new ObjectId(userId)));
		criteria.add(new MongokeyvaluePair<ObjectId>("_id", new ObjectId(resourceId)));*/
		
		resourceDao.replaceDoc(resourceJson, fields, fields);
		
		logger.debug("</updateResource>");
		return "Resource Updated Successfully";
	}
}
