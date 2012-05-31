package models;

import exceptions.EmtipsetException;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import securesocial.provider.SocialUser;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class User extends GenericModel {

    private static final Long RESULT_USER_ID = 0L;

    // Extending GenericModel so we can define the id field ourselves and use the DB sequence.
    @Id
    @SequenceGenerator(name="users_seq", sequenceName="users_id_seq", allocationSize=0)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="users_seq")
    public Long id;

    @Required
    public String name;

    public String email;

    @Column(name = "user_group")
    public String group;

    @Required
    @Column(name = "social_user_id")
    public String socialUserId;

    @Required
    @Column(name = "social_provider")
    public String socialProvider;

    @Column(name = "avatar_url")
    public String avatarUrl;

    public int points;

    public Date created = new Date();

    // Returns the EM-tipset user from the socialUser.
    // If no user is found, one is created that represents the socialUser.
    public static User getUser(SocialUser socialUser) throws EmtipsetException {
        User user = find("select u from User u " +
                "where u.socialUserId = ? " +
                "and u.socialProvider = ? ", socialUser.id.id, socialUser.id.provider.name()).first();
        if (user == null) {
            user = storeUser(socialUser);
        }
        return user;
    }

    private static User storeUser(SocialUser socialUser) throws EmtipsetException {
        User user = new User();
        user.name = socialUser.displayName;
        user.email = socialUser.email;
        user.socialUserId = socialUser.id.id;
        user.socialProvider = socialUser.id.provider.name();
        user.avatarUrl = socialUser.avatarUrl;

        return user.save();
    }

    public static User getResultUser() {
        return findById(RESULT_USER_ID);
    }

    public boolean isResultUser() {
        return (id.equals(RESULT_USER_ID));
    }
}
