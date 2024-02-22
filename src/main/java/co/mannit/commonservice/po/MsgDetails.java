package co.mannit.commonservice.po;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MsgDetails {

	private String toAddress;
	private String fromAddress;
	private String msg;
	private String timeStamp;
}
