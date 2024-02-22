package co.mannit.commonservice.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ValueExtracterFromJSON {

	private static final Logger logger = LogManager.getLogger(ValueExtracterFromJSON.class);
			
	@Autowired
	private ObjectMapper objectMapper;
	
	private Map<Class<?>, Function<JsonNode, ?>> parser = null;
	
	
	public ValueExtracterFromJSON() {
		parser = new HashMap<>();
		parser.put(String.class, (jsonNode) -> jsonNode.textValue());
		parser.put(Long.class, (jsonNode) -> jsonNode.asLong());
		parser.put(Integer.class, (jsonNode) -> jsonNode.asInt());
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(String jsonString, String kay, Class<T> retType) {
		logger.debug("<getValue> jsonString:{} kay:{}",jsonString, kay);
		T value = null;
		JsonNode tmp = null;
		try {
			JsonNode jsonNode = objectMapper.readTree(jsonString);
			value = (tmp =jsonNode.get(kay)) == null ? null : (T) parser.get(retType).apply(tmp);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		logger.debug("</getValue> value:{}",value);
		return value;
	}
	
	
}
