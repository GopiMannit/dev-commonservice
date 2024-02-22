package co.mannit.commonservice.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoException;
import com.mongodb.client.result.DeleteResult;

import co.mannit.commonservice.common.MongokeyvaluePair;
import co.mannit.commonservice.pojo.PaginationReqParam;
import co.mannit.commonservice.search.PaginationQueryBuilder;
import co.mannit.commonservice.search.SearchQueryBuilder;
import co.mannit.commonservice.search.SortQueryBuilder;

@Repository
public class CommonDao {

	private static final Logger logger = LogManager.getLogger(CommonDao.class);
			
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private SearchQueryBuilder searchQueryBuilder;
	
	public boolean isUserAlreadyExist(String colName, String key, Object value) {
		logger.debug("<isUserAlreadyExist> colName:{} key:{} value:{}",colName,key,value);
		
		boolean isExist = false;
		
		Query query = new Query();
		query.addCriteria(Criteria.where(key).is(value));
		isExist = mongoTemplate.exists(query, colName);
		
		logger.debug("</isUserAlreadyExist> isExist:{}",isExist);
		return isExist;
	}
	
	public boolean isUserAlreadyExist(String colName, MongokeyvaluePair<? extends Object> pair) {
		logger.debug("<isUserAlreadyExist> colName:{} pair:{}",colName, pair);
		
		boolean isExist = false;
		
		Query query = new Query();
		query.addCriteria(Criteria.where(pair.getKey()).is(pair.getValue()));
		isExist = mongoTemplate.exists(query, colName);
		
		logger.debug("</isUserAlreadyExist> isExist:{}",isExist);
		return isExist;
	}
	
	public String findDocAsString(String colName, List<MongokeyvaluePair<? extends Object>>  keyValuePairs) throws Exception {
		logger.debug("<findDoc> colName:{} keyValuePairs:{}",colName,keyValuePairs);
		
		Query query = new Query();
		if(keyValuePairs != null && keyValuePairs.size() > 0) {
			
			keyValuePairs.forEach(pair -> {
				query.addCriteria(Criteria.where(pair.getKey()).is(pair.getValue()));
			});
			
			/*Stream.of(keyValuePairs).forEach(pair -> {
			  query.addCriteria(Criteria.where(pair.getKey()).is(pair.getValue())); 
			  });*/

		}
		
		List<Document> lsftDoc = new ArrayList<>();
		mongoTemplate.executeQuery(query, colName, new DocumentCallbackHandler() {
			@Override
			public void processDocument(Document document) throws MongoException, DataAccessException {
				lsftDoc.add(document);
			}
		});
		
		if(lsftDoc.size() > 1) {
			throw new Exception("More than one document found for keyValuePairs:{}".formatted(keyValuePairs)); 
		}
		
		String doc = null;
		if(lsftDoc.size() == 1) {
			lsftDoc.get(0).remove("password");
			doc = lsftDoc.get(0).toJson();
		}
		
		logger.debug("</isUserAlreadyExist> Doc:{}",doc);
		return doc;
	}
	
	public Document findDoc(String colName, List<MongokeyvaluePair<? extends Object>>  keyValuePairs) throws Exception {
		logger.debug("<findDoc> colName:{} keyValuePairs:{}",colName,keyValuePairs);
		
		Query query = new Query();
		if(keyValuePairs != null && keyValuePairs.size() > 0) {
			
			keyValuePairs.forEach(pair -> {
				query.addCriteria(Criteria.where(pair.getKey()).is(pair.getValue()));
			});
		}
		
		logger.debug("query {}",query);
		
		List<Document> lsftDoc = new ArrayList<>();
		mongoTemplate.executeQuery(query, colName, new DocumentCallbackHandler() {
			@Override
			public void processDocument(Document document) throws MongoException, DataAccessException {
				lsftDoc.add(document);
			}
		});
		
		if(lsftDoc.size() > 1) {
			throw new Exception("More than one document found for keyValuePairs:{}".formatted(keyValuePairs)); 
		}
		
		
		logger.debug("</isUserAlreadyExist> Doc:{}",lsftDoc);
		return lsftDoc.size() ==0 ? null : lsftDoc.get(0);
	}
	
	public String findDocAsString(String colName, MongokeyvaluePair<? extends Object>  keyValuePairs) throws Exception {
		List<MongokeyvaluePair<? extends Object>> lstKeyValuePairs = new ArrayList<>();
		lstKeyValuePairs.add(keyValuePairs);
		return findDocAsString(colName, lstKeyValuePairs);
	}
	
	public void insertDocument(String colName, String json) {
		logger.debug("<insertDocument> colName:{} json:{}",colName,json);
		mongoTemplate.insert(json, colName);
		logger.debug("</insertDocument>");
	}
	
	public String insertDocument(String colName, String json, List<MongokeyvaluePair<? extends Object>> keyValuPairs) {
		logger.debug("<insertDocument> colName:{} json:{} keyValuPairs:{}",colName,json, keyValuPairs);
		
		Document document = Document.parse(json);
		
		if(keyValuPairs != null && keyValuPairs.size()>0) {
			keyValuPairs.stream().forEach(pair -> {
				document.append(pair.getKey(), pair.getValue());
			});
		}
		
		/*
		 * Optional.of(keyValuPairs).flatMap(lstKeyValue ->
		 * lstKeyValue.stream().forEach(pair->{ document.append(pair.getKey(),
		 * pair.getValue()); }));
		 */
		
		/*
		 * stream().forEach(pair ->{ document.append(pair.g, keyValuPairs) });
		 */

		
		Document document1 = mongoTemplate.insert(document, colName);
		logger.debug("</insertDocument>");
		
		return document1 == null ? "" : document1.toJson();
	}
	
	public void saveDocument(String colName, String json, List<MongokeyvaluePair<? extends Object>> keyValuPairs) {
		logger.debug("<saveDocument> colName:{} json:{} keyValuPairs:{}",colName,json, keyValuPairs);
		
		Document document = Document.parse(json);
		
		if(keyValuPairs != null && keyValuPairs.size()>0) {
			keyValuPairs.stream().forEach(pair -> {
				document.append(pair.getKey(), pair.getValue());
			});
		}
		
		mongoTemplate.save(document, colName);
		logger.debug("</saveDocument>");
	}
	
	public void saveDocument(String colName, Document document) {
		logger.debug("<saveDocument> colName:{} Document:{} ",colName,document);
		mongoTemplate.save(document, colName);
		logger.debug("</saveDocument>");
	}
	
	public void printAllDoc(String colName) {
		logger.debug("<printAllDoc> colName:{}",colName);
		
		mongoTemplate.executeQuery(new Query(), "user", new DocumentCallbackHandler() {

			@Override
			public void processDocument(org.bson.Document document) throws MongoException, DataAccessException {
				// TODO Auto-generated method stub
				logger.debug(document);
			}
			
		});
		
		logger.debug("</printAllDoc>");
	}
	
	public List<Document> findDoc(String colName, List<MongokeyvaluePair<? extends Object>>  keyValuePairs, PaginationReqParam paginationReq) throws Exception {
		logger.debug("<findDoc> colName:{} keyValuePairs:{}",colName,keyValuePairs);
		
		Query query = new Query();
		if(keyValuePairs != null && keyValuePairs.size() > 0) {
			
			List<Criteria> lstCriteria = new ArrayList<>();
			
			keyValuePairs.forEach(pair -> {
				if("userId".equalsIgnoreCase(pair.getKey()) || "adminId".equalsIgnoreCase(pair.getKey())) {
					lstCriteria.add(Criteria.where(pair.getKey()).is(pair.getValue()));
//					query.addCriteria(new Criteria().orOperator(Criteria.where(pair.getKey()).is(pair.getValue())));
				}else {
					query.addCriteria(Criteria.where(pair.getKey()).is(pair.getValue()));
				}
			});
			
			if(lstCriteria.size() > 0) {
				query.addCriteria(new Criteria().orOperator(lstCriteria));
			}
		}
		
		
		
		PaginationQueryBuilder.buildPaginationQuery(query, paginationReq);
		SortQueryBuilder.buildSortQuery(query, paginationReq);
		
		logger.debug("Query : {}",query);
		
		List<Document> lsftDoc = new ArrayList<>();
		mongoTemplate.executeQuery(query, colName, new DocumentCallbackHandler() {
			@Override
			public void processDocument(Document document) throws MongoException, DataAccessException {
				lsftDoc.add(document);
			}
		});


		
		logger.debug("</isUserAlreadyExist> Doc:{}",lsftDoc.size());
		return lsftDoc;
	}
	
	public List<Document> finDoc(String colName, MongokeyvaluePair<? extends Object>  keyValuePairs) throws Exception {
		List<MongokeyvaluePair<? extends Object>> lstKeyValuePairs = new ArrayList<>();
		lstKeyValuePairs.add(keyValuePairs);
		return findDoc(colName, lstKeyValuePairs, null);
	}
	
	public long deleteDoc(String colName, List<MongokeyvaluePair<? extends Object>>  keyValuePairs) throws Exception {
		logger.debug("<deleteDoc> colName:{} keyValuePairs:{}",colName,keyValuePairs);
		
		Query query = new Query();
		if(keyValuePairs != null && keyValuePairs.size() > 0) {
			
			keyValuePairs.forEach(pair -> {
				query.addCriteria(Criteria.where(pair.getKey()).is(pair.getValue()));
			});
		}
		
		DeleteResult deleResult = mongoTemplate.remove(query, colName);
		
		logger.debug("</deleteDoc> count {}",deleResult.getDeletedCount());
		
		return deleResult.getDeletedCount();
	}
	
	public void replaceDoc(String colName, String json, List<MongokeyvaluePair<? extends Object>>  fields, List<MongokeyvaluePair<? extends Object>>  criteria) throws Exception {
		logger.debug("<replaceDoc> colName:{} json:{} keyValuePairs:{}",colName,json, criteria);
		
		Document document = Document.parse(json);
		
		if(fields != null && fields.size()>0) {
			fields.stream().forEach(pair -> {
				document.append(pair.getKey(), pair.getValue());
			});
		}
		
		Query query = new Query();
		if(criteria != null && criteria.size() > 0) {
			
			criteria.forEach(pair -> {
				query.addCriteria(Criteria.where(pair.getKey()).is(pair.getValue()));
			});
		}
		mongoTemplate.findAndReplace(query, document, colName);
		logger.debug("</replaceDoc>");
	}
	
	public List<Document> search(String colName, List<MongokeyvaluePair<? extends Object>> keyValuePairs, Map<String, String> filters, PaginationReqParam paginationReq)
			throws Exception {
		logger.debug("<search>");
		
		Query query = new Query();
		
		if(keyValuePairs != null && keyValuePairs.size() > 0) {
			
			keyValuePairs.forEach(pair -> {
				query.addCriteria(Criteria.where(pair.getKey()).is(pair.getValue()));
			});
			
		}		
		
		searchQueryBuilder.buildSearchQuery(query, filters);
		
		PaginationQueryBuilder.buildPaginationQuery(query, paginationReq);
		
		SortQueryBuilder.buildSortQuery(query, paginationReq);
		
		Collation coll = Collation.of("en").strength(2);
//		query.collation(coll);
		
		logger.debug("query => {}", query);
		
		List<Document> lsftDoc = new ArrayList<>();
		mongoTemplate.executeQuery(query, colName, new DocumentCallbackHandler() {
			@Override
			public void processDocument(Document document) throws MongoException, DataAccessException {
				lsftDoc.add(document);
			}
		});

		logger.debug("</search> Doc:{}",lsftDoc.size());
		return lsftDoc;		
	}
	
	/*public List<Document> search(String colName, List<MongokeyvaluePair<? extends Object>> keyValuePairs, String op,
			Map<String, String> filter1, Map<String, String> filter2, PaginationReqParam pagenationReq)
			throws Exception {
		logger.debug("<findDoc> colName:{} keyValuePairs:{}",colName,keyValuePairs);
		
		Query query = new Query();
		if(keyValuePairs != null && keyValuePairs.size() > 0) {
			
			keyValuePairs.forEach(pair -> {
				query.addCriteria(Criteria.where(pair.getKey()).is(pair.getValue()));
			});
			
			if(op == null || "null".equals(op)) {
				query.addCriteria(buildCriteria(filter1));
			}else if("or".equals(op)) {
				query.addCriteria(
						new Criteria().orOperator(buildCriteria(filter1),
								buildCriteria(filter2)));
			}else if("and".equals(op)) {
				query.addCriteria(
						new Criteria().andOperator(buildCriteria(filter1),
								buildCriteria(filter2)));
			}
		}
		
		final Pageable pageableRequest = PageRequest.of(pagenationReq.getPage()-1, pagenationReq.getSizePerPage());
		query.with(pageableRequest);
		
		logger.debug("Query {}",query);
		List<Document> lsftDoc = new ArrayList<>();
		mongoTemplate.executeQuery(query, colName, new DocumentCallbackHandler() {
			@Override
			public void processDocument(Document document) throws MongoException, DataAccessException {
				lsftDoc.add(document);
			}
		});

		logger.debug("</search> Doc:{}",lsftDoc.size());
		return lsftDoc;
	}
	
	
	private Map<String, Function<String, ? extends Object>> dataTypeConverter = null;
	{
		dataTypeConverter = new HashMap<>();
		dataTypeConverter.put("L", (t)-> Long.parseLong(t));
		dataTypeConverter.put("I", (t)-> Integer.parseInt(t));
		dataTypeConverter.put("S", (t)-> t);
		dataTypeConverter.put("D", (t)-> {
			SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date d = sd.parse(t);
				
				Calendar calendar = Calendar.getInstance();
			    calendar.setTime(d);
			    calendar.add(Calendar.MINUTE, 330);
			    
			    d = calendar.getTime();
			    logger.debug("Date : {}",d );
				return d;
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		});
	}
	
	
	private Criteria buildCriteria(Map<String, String> filter) throws ServiceCommonException {
		Criteria cri = null;
		String operator = filter.get("op");
		
		switch(operator) {
			case "eq":
				cri = Criteria.where(filter.get("name")).is(dataTypeConverter.get(filter.get("dt")).apply(filter.get("value")));
				break;
			case "lt":
				cri = Criteria.where(filter.get("name")).lt(dataTypeConverter.get(filter.get("dt")).apply(filter.get("value")));
				break;
			case "lte":
				cri = Criteria.where(filter.get("name")).lte(dataTypeConverter.get(filter.get("dt")).apply(filter.get("value")));
				break;
			case "gt":
				cri = Criteria.where(filter.get("name")).gt(dataTypeConverter.get(filter.get("dt")).apply(filter.get("value")));
				break;
			case "gte":
				cri = Criteria.where(filter.get("name")).gte(dataTypeConverter.get(filter.get("dt")).apply(filter.get("value")));
				break;
			case "ne":
				cri = Criteria.where(filter.get("name")).ne(dataTypeConverter.get(filter.get("dt")).apply(filter.get("value")));
			case "in":
				cri = Criteria.where(filter.get("name")).in(dataTypeConverter.get(filter.get("dt")).apply(filter.get("value")));
			case "nin":
				cri = Criteria.where(filter.get("name")).nin(dataTypeConverter.get(filter.get("dt")).apply(filter.get("value")));
				break;
			case "regex":
				cri = Criteria.where(filter.get("name")).regex(String.valueOf(dataTypeConverter.get(filter.get("dt")).apply(filter.get("value"))));
				break;
		}
		
		if(cri == null) {
			throw new ServiceCommonException(String.format("This \"%s\" filter option not available", operator));
		}
		
		return cri;
	}*/
}
