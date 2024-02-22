package co.mannit.commonservice.external;

import co.mannit.commonservice.common.LoginMethod;
import co.mannit.commonservice.po.MsgDetails;

public interface MsgSender {

	
	public boolean sendMessage(MsgDetails msgDetails) ;
	
	public static MsgSender createMsgSender(LoginMethod method) {
		MsgSender msgSender = null;
		if(LoginMethod.EMAIL  == method) {
			msgSender = new EmailSender();
		}else if(LoginMethod.MOBILENO == method) {
			msgSender = new SMSSender();
		}
		return msgSender;
	}
}
