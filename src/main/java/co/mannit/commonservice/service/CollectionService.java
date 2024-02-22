package co.mannit.commonservice.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.mannit.commonservice.ServiceCommonException;
import co.mannit.commonservice.common.CollectionName;
import co.mannit.commonservice.common.MongokeyvaluePair;
import co.mannit.commonservice.common.util.TextUtil;
import co.mannit.commonservice.dao.CommonDao;

@Service
public class CollectionService {

	private static final Logger logger = LogManager.getLogger(CreateResource.class);
	
	@Autowired
	private CommonDao commonDao;
	
	public Document retrieveCollection(String domain, String subDomain, String collName) throws Exception {
		logger.debug("<retrieveCollection>");
		
		if(TextUtil.isEmpty(domain) || TextUtil.isEmpty(subDomain) || TextUtil.isEmpty(collName))throw new ServiceCommonException("111");
		
		List<MongokeyvaluePair<? extends Object>>  keyValuePairs = new ArrayList<>();
		keyValuePairs.add(new MongokeyvaluePair<String>("domain", domain));
		keyValuePairs.add(new MongokeyvaluePair<String>("subdomain", subDomain));
		keyValuePairs.add(new MongokeyvaluePair<String>("collname", collName));
		
		List<Document> lstDoc= commonDao.findDoc(CollectionName.COMBOCOLLECTION, keyValuePairs, null);
		
		System.out.println(lstDoc);
		
		logger.debug("</retrieveCollection>");
		return lstDoc == null || lstDoc.size() == 0? null : lstDoc.get(0);
	}
	
}
