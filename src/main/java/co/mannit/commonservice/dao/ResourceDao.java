package co.mannit.commonservice.dao;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import co.mannit.commonservice.common.CollectionName;
import co.mannit.commonservice.common.MongokeyvaluePair;
import co.mannit.commonservice.pojo.PaginationReqParam;

@Repository
public class ResourceDao {
	
	@Autowired
	private CommonDao commonDao;
	
	public String insertDocument(String json, List<MongokeyvaluePair<? extends Object>> keyValuPairs) {
		return commonDao.insertDocument(CollectionName.RESOURCE, json, keyValuPairs);
	}
	
	public List<Document> findDoc(List<MongokeyvaluePair<? extends Object>> pairs, PaginationReqParam paginationReq) throws Exception {
		return commonDao.findDoc(CollectionName.RESOURCE, pairs, paginationReq);
	}
	
	public long deleteDoc(List<MongokeyvaluePair<? extends Object>> pairs) throws Exception {
		return commonDao.deleteDoc(CollectionName.RESOURCE, pairs);
	}
	
	public void replaceDoc(String json, List<MongokeyvaluePair<? extends Object>> fields, List<MongokeyvaluePair<? extends Object>> criteria) throws Exception {
		commonDao.replaceDoc(CollectionName.RESOURCE, json, fields, criteria);
	}
	
	public List<Document> search(List<MongokeyvaluePair<? extends Object>> criteria, Map<String, String> filters, PaginationReqParam paginationReq)
			throws Exception {
		return commonDao.search(CollectionName.RESOURCE, criteria, filters, paginationReq);
	}
	
	/*public boolean isUserAlreadyExist(String key, Object value) {
		return commonDao.isUserAlreadyExist(CollectionName.RESOURCE, key, value);
	}
	
	public void insertDocument(String json) {
		commonDao.insertDocument(CollectionName.RESOURCE, json);
	}
	
	public void insertDocument(String json, List<MongokeyvaluePair<? extends Object>> keyValuPairs) {
		commonDao.insertDocument(CollectionName.RESOURCE, json, keyValuPairs);
	}
	
	public void saveDocument(String json, List<MongokeyvaluePair<? extends Object>> keyValuPairs) {
		commonDao.saveDocument(CollectionName.RESOURCE, json, keyValuPairs);
	}
	
	public void printAllDoc() {
		commonDao.printAllDoc(CollectionName.RESOURCE);
	}
	
	public String findDoc(List<MongokeyvaluePair<? extends Object>> pairs) throws Exception {
		return commonDao.findOneDoc(CollectionName.RESOURCE, pairs);
	}
	
	public String findDoc(MongokeyvaluePair<? extends Object> pair) throws Exception {
		return commonDao.findOneDoc(CollectionName.RESOURCE, pair);
	}*/


}
