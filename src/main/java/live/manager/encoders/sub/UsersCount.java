package live.manager.encoders.sub;

import live.manager.encoders.SendMessage;

public class UsersCount  extends SendMessage {

	public UsersCount(Integer usersCount) {
		super(null, usersCount);
	}
	
}
