package co.mannit.commonservice.dao;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import co.mannit.commonservice.common.CollectionName;
import co.mannit.commonservice.common.MongokeyvaluePair;


@Repository
public class UserDao {
	
	@Autowired
	private CommonDao commonDao;
	
	public boolean isUserAlreadyExist(String key, Object value) {
		return commonDao.isUserAlreadyExist(CollectionName.USER, key, value);
	}
	
	public void insertDocument(String json) {
		commonDao.insertDocument(CollectionName.USER, json);
	}
	
	public void insertDocument(String json, List<MongokeyvaluePair<? extends Object>> keyValuPairs) {
		commonDao.insertDocument(CollectionName.USER, json, keyValuPairs);
	}
	
	public void saveDocument(String json, List<MongokeyvaluePair<? extends Object>> keyValuPairs) {
		commonDao.saveDocument(CollectionName.USER, json, keyValuPairs);
	}
	
	public void saveDocument(Document document) {
		commonDao.saveDocument(CollectionName.USER, document);
	}
	
	public void printAllDoc() {
		commonDao.printAllDoc(CollectionName.USER);
	}
	
	public String findDocAsString(List<MongokeyvaluePair<? extends Object>> pairs) throws Exception {
		return commonDao.findDocAsString(CollectionName.USER, pairs);
	}
	
	public Document findDoc(List<MongokeyvaluePair<? extends Object>> pairs) throws Exception {
		return commonDao.findDoc(CollectionName.USER, pairs);
	}
	
	public String findDocAsString(MongokeyvaluePair<? extends Object> pair) throws Exception {
		return commonDao.findDocAsString(CollectionName.USER, pair);
	}
	

}
