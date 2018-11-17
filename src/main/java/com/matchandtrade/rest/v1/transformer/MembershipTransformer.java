package com.matchandtrade.rest.v1.transformer;

import com.matchandtrade.persistence.entity.MembershipEntity;
import com.matchandtrade.persistence.facade.TradeRepositoryFacade;
import com.matchandtrade.persistence.facade.UserRepositoryFacade;
import com.matchandtrade.rest.v1.json.MembershipJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MembershipTransformer extends Transformer<MembershipEntity, MembershipJson> {

	@Autowired
	private UserRepositoryFacade userRepositoryFacade;
	@Autowired
	private TradeRepositoryFacade tradeRepositoryFacade;

	@Override
	public MembershipJson transform(MembershipEntity entity) {
		MembershipJson result = new MembershipJson();
		result.setMembershipId(entity.getMembershipId());
		result.setType(transform(entity.getType()));
		if (entity.getTrade() == null) {
			result.setTradeId(null);
		} else {
			result.setTradeId(entity.getTrade().getTradeId());
		}
		if (entity.getUser() == null) {
			result.setUserId(null);
		} else {
			result.setUserId(entity.getUser().getUserId());
		}
		return result;
	}

	@Override
	public MembershipEntity transform(MembershipJson json) {
		MembershipEntity result;
		result = new MembershipEntity();
		result.setMembershipId(json.getMembershipId());
		result.setTrade(tradeRepositoryFacade.get(json.getTradeId()));
		result.setUser(userRepositoryFacade.get(json.getUserId()));
		return result;
	}

	private MembershipJson.Type transform(MembershipEntity.Type type) {
		if (type == null) {
			return null;
		}
		return MembershipJson.Type.valueOf(type.name());
	}

}
