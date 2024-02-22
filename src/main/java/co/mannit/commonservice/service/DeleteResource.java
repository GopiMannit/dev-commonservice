package co.mannit.commonservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.mannit.commonservice.ServiceCommonException;
import co.mannit.commonservice.common.MongokeyvaluePair;
import co.mannit.commonservice.common.util.TextUtil;
import co.mannit.commonservice.common.util.ValidationUtil;
import co.mannit.commonservice.dao.ResourceDao;
import co.mannit.commonservice.pojo.BaseReqParam;
import co.mannit.commonservice.search.TypeConvertor;

@Service
public class DeleteResource {

	private static final Logger logger = LogManager.getLogger(DeleteResource.class);
			
	@Autowired
	private ResourceDao resourceDao;
	
	public void deleteResource(String domain, String subDomain, String resourceId) throws Exception {
		logger.debug("<deleteResource>");
		
		if(!ValidationUtil.validateDomainAndId(domain, subDomain, resourceId)) {
			throw new ServiceCommonException("106");
		}
		
		List<MongokeyvaluePair<? extends Object>> lstKeyValuePairs = new ArrayList<>();
		lstKeyValuePairs.add(new MongokeyvaluePair<String>("domain", domain));
		lstKeyValuePairs.add(new MongokeyvaluePair<String>("subdomain", subDomain));
		lstKeyValuePairs.add(new MongokeyvaluePair<ObjectId>("_id", new ObjectId(resourceId)));
		
		
		resourceDao.deleteDoc(lstKeyValuePairs);
		logger.debug("</deleteResource>");
	}
	
	public long deleteResource(BaseReqParam reqParam, Map<String, String> params) throws Exception {
		logger.debug("<deleteResource>");
		
		if(!ValidationUtil.validateDomainAndId(reqParam)) {
			throw new ServiceCommonException("106");
		}
		
		List<MongokeyvaluePair<? extends Object>> lstKeyValuePairs = new ArrayList<>();
		lstKeyValuePairs.add(new MongokeyvaluePair<String>("domain", reqParam.getDomain()));
		lstKeyValuePairs.add(new MongokeyvaluePair<String>("subdomain", reqParam.getSubdomain()));
		lstKeyValuePairs.add(new MongokeyvaluePair<ObjectId>("userId", new ObjectId(reqParam.getUserId())));
		
		addPrimaryKeys(lstKeyValuePairs, params);
		
		long deletedCount = resourceDao.deleteDoc(lstKeyValuePairs);
		logger.debug("</deleteResource> count {}",deletedCount);
		
		return deletedCount;
	}
	
	private void addPrimaryKeys(List<MongokeyvaluePair<? extends Object>> lstKeyValuePairs, Map<String, String> params)throws ServiceCommonException {
		
		if(params.get("resourceId") != null) {
			lstKeyValuePairs.add(new MongokeyvaluePair<ObjectId>("_id", new ObjectId(params.get("resourceId"))));
		}
		
		String pkKeys = params.get("pkKeys");
		
		
		if (TextUtil.isNotEmpty(pkKeys)) {
			String[] arrKeys = pkKeys.split(",");
			Stream.of(arrKeys).forEach(keys -> {

				String[] arrKey = keys.trim().split("_");

				String colName = arrKey[0].trim();
				String type = arrKey[1].trim();
				if(TextUtil.isNotEmpty(params.get(colName))) {
						lstKeyValuePairs.add(new MongokeyvaluePair<Object>(colName, TypeConvertor.convert(type, params.get(colName))));
				}
			});
		}
		
		
	}
	
}
