package co.mannit.commonservice.search;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import co.mannit.commonservice.ServiceCommonException;
import co.mannit.commonservice.pojo.PaginationReqParam;

public class PaginationQueryBuilder {

	private static final Logger logger = LogManager.getLogger(PaginationQueryBuilder.class);
			
	static public void buildPaginationQuery(Query query, PaginationReqParam paginationReq) throws ServiceCommonException {
		logger.debug("<buildPaginationQuery>");
		
		if(paginationReq == null) return;
		
		if(paginationReq.getPage() < 1) throw new ServiceCommonException("107", new String[]{String.valueOf(paginationReq.getPage())});
		
		if(paginationReq.getSizePerPage() > 1000) throw new ServiceCommonException("108", new String[]{String.valueOf(paginationReq.getSizePerPage())});
		
		final Pageable pageableRequest = PageRequest.of(paginationReq.getPage()-1, paginationReq.getSizePerPage());
		query.with(pageableRequest);
		
		logger.debug("</buildPaginationQuery>");
	}
}
