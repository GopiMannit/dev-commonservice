package co.mannit.commonservice.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

import co.mannit.commonservice.common.util.TextUtil;
import co.mannit.commonservice.pojo.PaginationReqParam;

public class SortQueryBuilder {

	private static final Logger logger = LogManager.getLogger(SortQueryBuilder.class);
			
	static public void buildSortQuery(Query query, PaginationReqParam paginationReq) {
		logger.debug("<buildSortQuery>");
		
		if(paginationReq == null || TextUtil.isEmpty(paginationReq.getSortField())) return;
		
		if(TextUtil.isEmpty(paginationReq.getSortDirection()) || "ASC".equalsIgnoreCase(paginationReq.getSortDirection())) {
			query.with(Sort.by(Sort.Direction.ASC, paginationReq.getSortField()));
		}else {
			query.with(Sort.by(Sort.Direction.DESC, paginationReq.getSortField()));
		}
		
		logger.debug("</buildSortQuery>");
	}
}
