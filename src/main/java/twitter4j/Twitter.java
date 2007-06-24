package twitter4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import twitter4j.http.HttpClient;
import twitter4j.http.PostParameter;

/**
 * A java reporesentation of the <a href="http://twitter.com/help/api">Twitter API</a>
 */
public class Twitter implements java.io.Serializable {
    HttpClient http = null;
    private String baseURL = "http://twitter.com/";
    public Twitter() {
        http = new HttpClient();
        http.addRequestHeader("X-Twitter-Client", "Twitter4J");
        http.addRequestHeader("X-Twitter-Client-Version", "1.0");
        http.addRequestHeader("X-Twitter-Client-URL",
                              "http://yusuke.homeip.net/twitter4j/en/twitter4j.xml");

        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public Twitter(String baseURL) {
        this();
        this.baseURL = baseURL;
    }

    public Twitter(String id, String password) {
        http = new HttpClient(id, password);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public Twitter(String id, String password, String baseURL) {
        this(id, password);
        this.baseURL = baseURL;
    }

    /**
     * Returns the 20 most recent statuses from non-protected users who have set a custom user icon.
     * @return List<Status> List of statuses of the Public Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized List<Status> getPublicTimeline() throws
        TwitterException {
        return Status.constructStatuses(http.get(baseURL +
                                                 "statuses/public_timeline.xml", false).
                                        asDocument(), this);
    }

    /**
     * Returns only public statuses with an ID greater than (that is, more recent than) the specified ID.
     * @param sinceID String
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getPublicTimeline(String sinceID) throws
        TwitterException {
        return Status.constructStatuses(http.get(baseURL +
                                                 "statuses/public_timeline.xml?since_id=" +
                                                 sinceID, false).
                                        asDocument(), this);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user and that user's friends.
     *  It's also possible to request another user's friends_timeline via the id parameter below.
     * @return List<Status> List of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized List<Status> getFriendsTimeline() throws
        TwitterException {
        return Status.constructStatuses(http.get(baseURL +
                                                 "statuses/friends_timeline.xml", true).
                                        asDocument(), this);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String user ID
     * @return List<Status> List of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized List<Status> getFriendsTimeline(String id) throws
        TwitterException {
        return Status.constructStatuses(http.get(baseURL +
                                                 "statuses/friends_timeline/" +
                                                 id + ".xml", true).asDocument(), this);
    }

    /**
     * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user.
     * @param since Date
     * @return List<Status> List of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getFriendsTimeline(Date since) throws
        TwitterException {
        return Status.constructStatuses(http.get(baseURL +
                                                 "statuses/friends_timeline.xml?since=" +
                                                 formatDate(since), true).
                                        asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String user ID
     * @param since Date
     * @return List<Status> List of the Friends Timeline
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getFriendsTimeline(String id,
        Date since) throws TwitterException {
        return Status.constructStatuses(http.get(baseURL +
                                                 "statuses/friends_timeline/" +
                                                 id + ".xml?since=" +
                                                 formatDate(since), true).
                                        asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String
     * @param count int
     * @param since Date
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(String id, int count,
        Date since) throws TwitterException {
        return Status.constructStatuses(http.get(baseURL +
                                                 "statuses/user_timeline/" + id +
                                                 ".xml?since=" +
                                                 formatDate(since) + "&count=" +
                                                 count, true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String
     * @param since Date
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(String id,
        Date since) throws TwitterException {
        return Status.constructStatuses(http.get(baseURL +
                                                 "statuses/user_timeline/" + id +
                                                 ".xml?since=" +
                                                 formatDate(since), true).
                                        asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String
     * @param count int
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(String id, int count) throws
        TwitterException {
        return Status.constructStatuses(http.get(baseURL +
                                                 "statuses/user_timeline/" + id +
                                                 ".xml?count=" + count, true).
                                        asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     * @param count int
     * @param since Date
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(int count,
        Date since) throws TwitterException {
        return Status.constructStatuses(http.get(baseURL +
                                                 "statuses/user_timeline.xml?since=" +
                                                 formatDate(since) + "&count=" +
                                                 count, true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the specified user id.
     * @param id String
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline(String id) throws
        TwitterException {
        return Status.constructStatuses(http.get(baseURL +
                                                 "statuses/user_timeline/" + id +
                                                 ".xml", true).asDocument(), this);
    }

    /**
     * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getUserTimeline() throws
        TwitterException {
        return Status.constructStatuses(http.get(baseURL +
                                                 "statuses/user_timeline.xml", true).
                                        asDocument(), this);
    }

    /**
     * Returns a single status, specified by the id parameter. The status's author will be returned inline.
     * @param id int
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized Status show(int id) throws TwitterException {
        return new Status(http.get(baseURL + "statuses/show/" + id + ".xml", true).
                          asDocument().getDocumentElement(), this);
    }

    /**
     * Updates the user's status.
     * The text will be trimed if the length of the text is exceeding 160 characters.
     * @param status String
     * @return Status
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public Status update(String status) throws TwitterException {
        if (status.length() > 160) {
            status = status.substring(0, 160);
        }
        return new Status(http.post(baseURL + "statuses/update.xml",
                                    new
                                    PostParameter[] {new PostParameter("status",
            status), new PostParameter("source", "Twitter4J")}, true).
                          asDocument().getDocumentElement(), this);
    }

    /**
     * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<Status> getReplies() throws TwitterException {
        return Status.constructStatuses(http.get(baseURL +
                                                 "statuses/replies.xml", true).
                                        asDocument(), this);
    }

    /**
     * Returns the specified user's friends, each with current status inline.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFriends() throws TwitterException {
        return User.constructUsers(http.get(baseURL + "statuses/friends.xml", true).
                                   asDocument(), this);
    }

    /**
     * Returns the user's friends, each with current status inline.
     * @param id String
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFriends(String id) throws
        TwitterException {
        return User.constructUsers(http.get(baseURL +
                                            "statuses/friends.xml?id=" + id, true).
                                   asDocument(), this);
    }

    /**
     * Returns the authenticating user's followers, each with current status inline.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFollowers() throws TwitterException {
        return User.constructUsers(http.get(baseURL + "statuses/followers.xml", true).
                                   asDocument(), this);
    }

    /**
     * Returns a list of the users currently featured on the site with their current statuses inline.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<User> getFeatured() throws TwitterException {
        return User.constructUsers(http.get(baseURL + "statuses/featured.xml", true).
                                   asDocument(), this);
    }

    /**
     * Returns extended information of a given user, specified by ID or screen name as per the required id parameter below.  This information includes design settings, so third party developers can theme their widgets according to a given user's preferences.
     * @param id String
     * @return User
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized UserWithStatus getUserDetail(String id) throws
        TwitterException {
        return new UserWithStatus(http.get(baseURL + "users/show/" + id +
                                           ".xml", true).asDocument().
                                  getDocumentElement(), this);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getDirectMessages() throws
        TwitterException {
        return DirectMessage.constructDirectMessages(http.get(baseURL +
            "direct_messages.xml", true).asDocument(), this);
    }

    /**
     * Returns a list of the direct messages sent to the authenticating user.
     * @param since Date
     * @return List
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized List<DirectMessage> getDirectMessages(Date since) throws
        TwitterException {
        return DirectMessage.constructDirectMessages(http.get(baseURL +
            "direct_messages.xml?since=" + formatDate(since), true).asDocument(), this);
    }

    /**
     * Sends a new direct message to the specified user from the authenticating user.  Requires both the user and text parameters below.
     * The text will be trimed if the length of the text is exceeding 140 characters.
     * @param id String
     * @param text String
     * @return DirectMessage
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized DirectMessage sendDirectMessage(String id,
        String text) throws TwitterException {
        if (text.length() > 160) {
            text = text.substring(0, 160);
        }
        return new DirectMessage(http.post(baseURL + "direct_messages/new.xml",
                                           new PostParameter[] {new
                                           PostParameter("user", id),
                                           new PostParameter("text", text)}, true).
                                 asDocument().getDocumentElement(), this);
    }


    /**
     * Destroys the direct message specified in the required ID parameter.  The authenticating user must be the recipient of the specified direct message.
     * @param id
     * @return DirectMessage
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized DirectMessage deleteDirectMessage(int id) throws
        TwitterException {
        return new DirectMessage(http.get(baseURL +
            "direct_messages/destroy/"+id+".xml", true).asDocument().getDocumentElement(), this);
    }

    /**
     * Befriends the user specified in the ID parameter as the authenticating user.  Returns the befriended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
     * @param id String
     * @return DirectMessage
     * @throws TwitterException when Twitter service or network is unavailable
     */

    public final synchronized User create(String id) throws TwitterException {
        return new User(http.get(baseURL + "friendships/create/" + id + ".xml", true).
                        asDocument().getDocumentElement(), this);
    }

    /**
     * Discontinues friendship with the user specified in the ID parameter as the authenticating user.  Returns the un-friended user in the requested format when successful.  Returns a string describing the failure condition when unsuccessful.
     * @param id String
     * @return DirectMessage
     * @throws TwitterException when Twitter service or network is unavailable
     */
    public final synchronized User destroy(String id) throws TwitterException {
        return new User(http.get(baseURL + "friendships/destroy/" + id + ".xml", true).
                        asDocument().getDocumentElement(), this);
    }

    private SimpleDateFormat format = new SimpleDateFormat(
        "EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
    private String formatDate(Date date) {
        try {
            return URLEncoder.encode(format.format(date), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return URLEncoder.encode(format.format(date));
        }
    }

    public void setRetryCount(int retryCount) {
        http.setRetryCount(retryCount);
    }

    public void setRetryIntervalSecs(int retryIntervalSecs) {
        http.setRetryIntervalSecs(retryIntervalSecs);
    }

    @Override public int hashCode() {
        return http.hashCode() + this.baseURL.hashCode();
    }

    @Override public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof Twitter) {
            Twitter that = (Twitter) obj;
            return this.http.equals(that.http)
                && this.baseURL.equals(that.baseURL);
        }
        return false;
    }
}