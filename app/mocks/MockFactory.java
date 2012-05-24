package mocks;

import securesocial.provider.ProviderType;
import securesocial.provider.SocialUser;
import securesocial.provider.UserId;

public class MockFactory {

    public static SocialUser getMockedSocialUser() {
        SocialUser user = new SocialUser();
        user.id = new UserId();
        user.id.id = "https://www.google.com/accounts/o8/id?id=AIlOawnRb8KYN7uFRvTJ6kLnxN2P1_el1fkMd98";
        user.id.provider = ProviderType.google;
        user.email = "nasse@luff.com";
        user.avatarUrl = "http://www.zifyoip.com/stuff/SQuirreL/squirrel.gif";
        user.displayName = "Nasse Luff";

        return user;
    }
}