package co.mannit.commonservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.mannit.commonservice.ServiceCommonException;
import co.mannit.commonservice.common.MongokeyvaluePair;
import co.mannit.commonservice.common.util.TextUtil;
import co.mannit.commonservice.common.util.ValidationUtil;
import co.mannit.commonservice.dao.ResourceDao;
import co.mannit.commonservice.dao.UserDao;
import co.mannit.commonservice.pojo.PaginationReqParam;

@Service
public class ReadResource {

	private static final Logger logger = LogManager.getLogger(ReadResource.class);
			
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private UserDao userDao;
	
	public List<String> readResource(PaginationReqParam paginationReq) throws Exception {
		logger.debug("<readResource>");
		
		if(!ValidationUtil.validateDomainAndId(paginationReq)) {
			throw new ServiceCommonException("106");
		}
		
		List<MongokeyvaluePair<? extends Object>> lstKeyValuePairs = new ArrayList<>();
		lstKeyValuePairs.add(new MongokeyvaluePair<String>("domain", paginationReq.getDomain()));
		lstKeyValuePairs.add(new MongokeyvaluePair<String>("subdomain", paginationReq.getSubdomain()));
		lstKeyValuePairs.add(new MongokeyvaluePair<ObjectId>("_id", new ObjectId(paginationReq.getUserId())));
		
		Document document = userDao.findDoc(lstKeyValuePairs);
		
		lstKeyValuePairs.remove(2);
		lstKeyValuePairs.add(new MongokeyvaluePair<ObjectId>("userId", new ObjectId(paginationReq.getUserId())));
		if( document != null && (TextUtil.isEmpty(document.getString("adminId")) || "admin".equalsIgnoreCase(document.getString("role"))) ) {
			lstKeyValuePairs.add(new MongokeyvaluePair<String>("adminId", paginationReq.getUserId()));
		}
		
		
		
		List<Document> listDoc = resourceDao.findDoc(lstKeyValuePairs, paginationReq);
		
		List<String> lst = listDoc.stream().map(doc->doc.toJson()).collect(Collectors.toCollection(ArrayList::new));
		
		logger.debug("</readResource>");
		
		return lst;
	}
	
}
