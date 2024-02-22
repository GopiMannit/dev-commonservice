package co.mannit.commonservice.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import co.mannit.commonservice.ServiceCommonException;

@Component
public class SearchQueryBuilder {

	private static final Logger logger = LogManager.getLogger(SearchQueryBuilder.class);
			
	@Autowired
	private FilterBuilder filterBuilder;
	
	public Query buildSearchQuery(Query query, Map<String, String> params) throws ServiceCommonException {
		
		List<Search<? extends Object>> lstSearch = filterBuilder.buildSearch(params);
		
		if(lstSearch == null || lstSearch.size() == 0) return query;
		
		List<Criteria> lstFilter = lstSearch.stream().map(search->{
			return buildCriteria(search);
		}).collect(Collectors.toCollection(ArrayList::new));
		
		query.addCriteria(
				new Criteria().andOperator(lstFilter));
			
		return query;
	}
	
	
	public Query buildSearchQuery(Query query, String filterStr) throws ServiceCommonException {
		logger.debug("<buildSearchQuery> {}", filterStr);
		
		List<Search<? extends Object>> lstFilter = filterBuilder.buildSearch(filterStr);
		
		if(lstFilter == null) return query;
		
		if(lstFilter.size() == 1) {
			query.addCriteria(buildCriteria(lstFilter.get(0)));
		}else if(lstFilter.get(0).isOrFilter()) {
			query.addCriteria(
					new Criteria().orOperator(buildCriteria(lstFilter.get(0)),
							buildCriteria(lstFilter.get(1))));
		}else if(lstFilter.get(0).isAndFilter()) {
			query.addCriteria(
					new Criteria().andOperator(buildCriteria(lstFilter.get(0)),
							buildCriteria(lstFilter.get(1))));
		}
		
		logger.debug("</buildSearchQuery>");
		return query;
	}
	
	private Criteria buildCriteria(Search<?> search) throws ServiceCommonException {
		Criteria cri = null;
		
		switch(search.getOperator()) {
			case "eq":
				cri = Criteria.where(search.getName()).is(search.getValue());
				break;
			case "lt":
				cri = Criteria.where(search.getName()).lt(search.getValue());
				break;
			case "lte":
				cri = Criteria.where(search.getName()).lte(search.getValue());
				break;
			case "gt":
				cri = Criteria.where(search.getName()).gt(search.getValue());
				break;
			case "gte":
				cri = Criteria.where(search.getName()).gte(search.getValue());
				break;
			case "ne":
				cri = Criteria.where(search.getName()).ne(search.getValue());
			case "in":
				cri = Criteria.where(search.getName()).in(search.getValue());
			case "nin":
				cri = Criteria.where(search.getName()).nin(search.getValue());
				break;
			case "regex":
				cri = Criteria.where(search.getName()).regex(String.valueOf(search.getValue()));
				break;
		}
		
		if(cri == null) {
			throw new ServiceCommonException(String.format("This \"%s\" filter option not available", search.getOperator()));
		}
		
		return cri;
	}
}
