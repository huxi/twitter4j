package twitter4j.api;

import twitter4j.Paging;
import twitter4j.TwitterListener;

public interface TimelineMethodsAsync
{
	/**
	 * Returns the 20 most recent statuses from non-protected users who have set a custom user icon. <a href="http://groups.google.com/group/twitter-development-talk/browse_thread/thread/f881564598a947a7#">The public timeline is cached for 60 seconds</a> so requesting it more often than that is a waste of resources.
	 * <br>This method calls http://api.twitter.com/1/statuses/public_timeline
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-public_timeline">Twitter API Wiki / Twitter REST API Method: statuses public_timeline</a>
	 */
	void getPublicTimelineAsync(TwitterListener listener);

	/**
	 * Returns only public statuses with an ID greater than (that is, more recent than) the specified ID.
	 * <br>This method calls http://api.twitter.com/1/statuses/public_timeline
	 * @param sinceID String
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-public_timeline">Twitter API Wiki / Twitter REST API Method: statuses public_timeline</a>
	 */
	void getPublicTimelineAsync(long sinceID, TwitterListener listener);

	/**
	 * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.
	 * <br>This method calls http://api.twitter.com/1/statuses/home_timeline
	 *
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-home_timeline">Twitter API Wiki / Twitter REST API Method: statuses home_timeline</a>
	 * @since Twitter4J 2.0.10
	 */
	void getHomeTimelineAsync(TwitterListener listener);

	/**
	 * Returns the 20 most recent statuses, including retweets, posted by the authenticating user and that user's friends. This is the equivalent of /timeline/home on the Web.
	 * <br>This method calls http://api.twitter.com/1/statuses/home_timeline
	 *
	 * @param paging   controls pagination
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-home_timeline">Twitter API Wiki / Twitter REST API Method: statuses home_timeline</a>
	 * @since Twitter4J 2.0.10
	 */
	void getHomeTimelineAsync(Paging paging, TwitterListener listener);


	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user and that user's friends.
	 *  It's also possible to request another user's friends_timeline via the id parameter below.
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
	 */
	void getFriendsTimelineAsync(TwitterListener listener);

	/**
	 * Returns the 20 most recent statuses posted in the last 24 hours from the authenticating user and that user's friends.
	 *  It's also possible to request another user's friends_timeline via the id parameter below.
	 * <br>This method calls http://api.twitter.com/1/statuses/friends_timeline
	 * @param paging controls pagination
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-friends_timeline">Twitter API Wiki / Twitter REST API Method: statuses friends_timeline</a>
	 * @since Twitter4J 2.0.1
	 */
	void getFriendsTimelineAsync(Paging paging, TwitterListener listener);

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified screenName.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @param screenName Specifies the screen name of the user for whom to return the user_timeline.
	 * @param paging controls pagination
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
	 * @since Twitter4J 2.0.1
	 */
	void getUserTimelineAsync(String screenName, Paging paging, TwitterListener listener);

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified screenName.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @param userId Specifies the ID of the user for whom to return the user_timeline.
	 * @param paging controls pagination
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
	 * @since Twitter4J 2.1.0
	 */
	void getUserTimelineAsync(int userId, Paging paging, TwitterListener listener);

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified user id.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @param paging   controls pagination
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
	 * @since Twitter4J 2.0.1
	 */
	void getUserTimelineAsync(Paging paging, TwitterListener listener);

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified user id.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @param screenName Specifies the screen name of the user for whom to return the user_timeline.
	 * @param listener   a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
	 */
	void getUserTimelineAsync(String screenName, TwitterListener listener);

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the specified user id.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 *
	 * @param userId   Specifies the ID of the user for whom to return the user_timeline.
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
	 * @since Twitter4J 2.1.0
	 */
	void getUserTimelineAsync(int userId, TwitterListener listener);

	/**
	 * Returns the most recent statuses posted in the last 24 hours from the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/statuses/user_timeline
	 * @param listener a listener object that receives the response
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-user_timeline">Twitter API Wiki / Twitter REST API Method: statuses user_timeline</a>
	 */
	void getUserTimelineAsync(TwitterListener listener);

	/**
	 * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
	 * <br>This method calls http://api.twitter.com/1/statuses/mentions
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-mentions">Twitter API Wiki / Twitter REST API Method: statuses mentions</a>
	 */
	void getMentionsAsync(TwitterListener listener);

	/**
	 * Returns the 20 most recent replies (status updates prefixed with @username) to the authenticating user.  Replies are only available to the authenticating user; you can not request a list of replies to another user whether public or protected.
	 * <br>This method calls http://api.twitter.com/1/statuses/mentions
	 * @param paging controls pagination
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.1
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method:-statuses-mentions">Twitter API Wiki / Twitter REST API Method: statuses mentions</a>
	 */
	void getMentionsAsync(Paging paging, TwitterListener listener);

	/**
	 * Returns the 20 most recent retweets posted by the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_me
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_by_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_by_me</a>
	 */
	void getRetweetedByMeAsync(TwitterListener listener);

	/**
	 * Returns the 20 most recent retweets posted by the authenticating user.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweeted_by_me
	 * @param paging controls pagination
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_by_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_by_me</a>
	 */
	void getRetweetedByMeAsync(Paging paging, TwitterListener listener);

	/**
	 * Returns the 20 most recent retweets posted by the authenticating user's friends.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_me
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_to_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_to_me</a>
	 */
	void getRetweetedToMeAsync(TwitterListener listener);

	/**
	 * Returns the 20 most recent retweets posted by the authenticating user's friends.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweeted_to_me
	 * @param paging controls pagination
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweeted_to_me">Twitter API Wiki / Twitter REST API Method: statuses/retweeted_to_me</a>
	 */
	void getRetweetedToMeAsync(Paging paging, TwitterListener listener);

	/**
	 * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweets_of_me">Twitter API Wiki / Twitter REST API Method: statuses/retweets_of_me</a>
	 */
	void getRetweetsOfMeAsync(TwitterListener listener);

	/**
	 * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
	 * <br>This method calls http://api.twitter.com/1/statuses/retweets_of_me
	 * @param paging controls pagination
	 * @param listener a listener object that receives the response
	 * @since Twitter4J 2.0.10
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-statuses-retweets_of_me">Twitter API Wiki / Twitter REST API Method: statuses/retweets_of_me</a>
	 */
	void getRetweetsOfMeAsync(Paging paging, TwitterListener listener);
}
