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

    // Extending GenericModel so we can define the id field ourselves and use the DB sequence.
    @Id
    @SequenceGenerator(name="users_seq", sequenceName="users_id_seq", allocationSize=0)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="users_seq")
    public Long id;

    @Required
    public String name;

    public String email;

    @Required
    @Column(name = "social_user_id")
    public String socialUserId;

    @Required
    @Column(name = "social_provider")
    public String socialProvider;

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

        return user.save();
    }

}
