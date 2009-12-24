/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package twitter4j;

import twitter4j.api.AccountMethods;
import twitter4j.api.BlockMethods;
import twitter4j.api.DirectMessageMethods;
import twitter4j.api.FavoriteMethods;
import twitter4j.api.FriendshipMethods;
import twitter4j.api.HelpMethods;
import twitter4j.api.ListMembersMethods;
import twitter4j.api.ListMethods;
import twitter4j.api.ListSubscribersMethods;
import twitter4j.api.LocalTrendsMethods;
import twitter4j.api.NotificationMethods;
import twitter4j.api.SavedSearchesMethods;
import twitter4j.api.SearchMethods;
import twitter4j.api.SocialGraphMethods;
import twitter4j.api.SpamReportingMethods;
import twitter4j.api.StatusMethods;
import twitter4j.api.TimelineMethods;
import twitter4j.api.UserMethods;
import twitter4j.http.AccessToken;
import twitter4j.http.HttpClient;
import twitter4j.http.HttpResponseEvent;
import twitter4j.http.HttpResponseListener;
import twitter4j.http.PostParameter;
import twitter4j.http.RequestToken;
import twitter4j.http.Response;
import twitter4j.impl.TwitterTransport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A java representation of the <a href="http://apiwiki.twitter.com/">Twitter API</a>
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Twitter extends TwitterSupport
        implements java.io.Serializable,
        SearchMethods,
        TimelineMethods,
        StatusMethods,
        UserMethods,
        ListMethods,
        ListMembersMethods,
        ListSubscribersMethods,
        DirectMessageMethods,
        FriendshipMethods,
        SocialGraphMethods,
        AccountMethods,
        FavoriteMethods,
        NotificationMethods,
        BlockMethods,
        SpamReportingMethods,
        SavedSearchesMethods,
        LocalTrendsMethods,
        HelpMethods {
    private static final long serialVersionUID = -1486360080128882436L;

    /**
     * Creates an unauthenticated Twitter instance
     */
    public Twitter() {
        super();
        HttpResponseListener httpResponseListener = new MyHttpResponseListener();
        http.addHttpResponseListener(httpResponseListener);

        http.setRequestTokenURL(Configuration.getScheme() + "twitter.com/oauth/request_token");
        http.setAuthorizationURL(Configuration.getScheme() + "twitter.com/oauth/authorize");
        http.setAccessTokenURL(Configuration.getScheme() + "twitter.com/oauth/access_token");
    }

    /**
     * Creates a Twitter instance with supplied id
     * @param screenName the screen name of the user
     * @param password the password of the user
     */
    public Twitter(String screenName, String password) {
        this();
        setUserId(screenName);
        setPassword(password);
    }

    /**
     * Returns the base URL
     *
     * @return the base URL
     */
    protected String getBaseURL() {
        return USE_SSL ? "https://api.twitter.com/1/" : "http://api.twitter.com/1/";
    }


    /**
     * Returns the search base url
     * @return search base url
     * @since Twitter4J 1.1.7
     */
    protected String getSearchBaseURL(){
        return USE_SSL ? "https://search.twitter.com/" : "http://search.twitter.com/";
    }

    /**
     *
     * @param consumerKey OAuth consumer key
     * @param consumerSecret OAuth consumer secret
     * @since Twitter 2.0.0
     */
    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret){
        this.http.setOAuthConsumer(consumerKey, consumerSecret);
    }

    /**
     * Retrieves a request token
     * @return generated request token.
     * @throws TwitterException when Twitter service or network is unavailable
     * @since Twitter 2.0.0
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ">Twitter API Wiki - OAuth FAQ</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step1">OAuth Core 1.0 - 6.1.  Obtaining an Unauthorized Request Token</a>
     */
    public RequestToken getOAuthRequestToken() throws TwitterException {
        return http.getOAuthRequestToken();
    }

    public RequestToken getOAuthRequestToken(String callback_url) throws TwitterException {
      return http.getOauthRequestToken(callback_url);
    }

    /**
     * Retrieves an access token associated with the supplied request token and sets userId.
     * @param requestToken the request token
     * @return access token associsted with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.0
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws TwitterException {
        AccessToken accessToken = http.getOAuthAccessToken(requestToken);
        setUserId(accessToken.getScreenName());
        return accessToken;
    }

    /**
     * Retrieves an access token associated with the supplied request token and sets userId.
     * @param requestToken the request token
     * @param oauth_verifier oauth_verifier or pin
     * @return access token associsted with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.0
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String oauth_verifier) throws TwitterException {
        AccessToken accessToken = http.getOAuthAccessToken(requestToken, oauth_verifier);
        setUserId(accessToken.getScreenName());
        return accessToken;
    }

    /**
     * Retrieves an access token associated with the supplied request token and sets userId.
     * @param token request token
     * @param tokenSecret request token secret
     * @return access token associated with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.1
     */
    public synchronized AccessToken getOAuthAccessToken(String token, String tokenSecret) throws TwitterException {
        AccessToken accessToken = http.getOAuthAccessToken(token, tokenSecret);
        setUserId(accessToken.getScreenName());
        return accessToken;
    }

    /**
     * Retrieves an access token associated with the supplied request token.
     * @param token request token
     * @param tokenSecret request token secret
     * @param pin pin
     * @return access token associated with the supplied request token.
     * @throws TwitterException when Twitter service or network is unavailable, or the user has not authorized
     * @see <a href="http://apiwiki.twitter.com/OAuth-FAQ#Howlongdoesanaccesstokenlast">Twitter API Wiki - How long does an access token last?</a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since Twitter 2.0.8
     */
    public synchronized AccessToken getOAuthAccessToken(String token
            , String tokenSecret, String pin) throws TwitterException {
        return http.getOAuthAccessToken(token, tokenSecret, pin);
    }

    /**
     * Sets the access token
     * @param accessToken accessToken
     * @since Twitter 2.0.0
     */
    public void setOAuthAccessToken(AccessToken accessToken){
        this.http.setOAuthAccessToken(accessToken);
    }

    /**
     * Sets the access token
     * @param token token
     * @param tokenSecret token secret
     * @since Twitter 2.0.0
     */
    public void setOAuthAccessToken(String token, String tokenSecret) {
        setOAuthAccessToken(new AccessToken(token, tokenSecret));
    }

    /**
     * Returns authenticating user's screen name.<br>
     * This method automatically retrieves userId using verifyCredentials if the instance is using OAuth based authentication.
     *
     * @return the authenticating screen name
	 * @throws TwitterException if verifyCredentials is throwing an exception.
     * @throws IllegalStateException if no credentials are supplied
     */
    protected String getScreenName() throws TwitterException , IllegalStateException{
        String userId = super.getUserId();
        if ((null == userId && http.isAuthenticationEnabled()) || -1 != userId.indexOf("@")) {
            // retrieve the screen name if this instance is authenticated with OAuth or email address
            userId = this.verifyCredentials().getScreenName();
            setUserId(userId);
        }
        if(null == userId){
            throw new IllegalStateException(
                    "Neither user ID/password combination nor OAuth consumer key/secret combination supplied");
        }
        return userId;
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */

    protected Response get(String url, boolean authenticate) throws TwitterException {
        return get(url, null, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @param name1        the name of the first parameter
     * @param value1       the value of the first parameter
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */

    protected Response get(String url, String name1, String value1, boolean authenticate) throws TwitterException {
        return get(url, new PostParameter[]{new PostParameter(name1, value1)}, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param name1        the name of the first parameter
     * @param value1       the value of the first parameter
     * @param name2        the name of the second parameter
     * @param value2       the value of the second parameter
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */

    protected Response get(String url, String name1, String value1, String name2, String value2, boolean authenticate) throws TwitterException {
        return get(url, new PostParameter[]{new PostParameter(name1, value1), new PostParameter(name2, value2)}, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param params       the request parameters
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */
    protected Response get(String url, PostParameter[] params, boolean authenticate) throws TwitterException {
        if (null != params && params.length > 0) {
            url += "?" + HttpClient.encodeParameters(params);
        }
        return http.get(url, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param params       the request parameters
     * @param pagingParams controls pagination
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws TwitterException when Twitter service or network is unavailable
     */
    protected Response get(String url, PostParameter[] params, List<PostParameter> pagingParams, boolean authenticate) throws TwitterException {
        if (null != params) {
            if (null != pagingParams) {
				pagingParams.addAll(Arrays.asList(params));
                return get(url, pagingParams.toArray(new PostParameter[pagingParams.size()]), authenticate);
            } else {
                return get(url, params, authenticate);
            }
        } else if (null != pagingParams) {
            return get(url, pagingParams.toArray(new PostParameter[pagingParams.size()]), authenticate);
        }else{
            return get(url, authenticate);
        }
    }

    class MyHttpResponseListener implements HttpResponseListener, java.io.Serializable {
        private static final long serialVersionUID = 5385389730784875997L;

        public void httpResponseReceived(HttpResponseEvent event) {
            if (0 < (accountRateLimitStatusListeners.size() + ipRateLimitStatusListeners.size())) {
                Response res = event.getResponse();
                RateLimitStatus rateLimitStatus = RateLimitStatus.createFromResponseHeader(res);
                if (null != rateLimitStatus) {
                    if (event.isAuthenticated()) {
                        fireRateLimitStatusListenerUpdate(accountRateLimitStatusListeners, rateLimitStatus);
                    } else {
                        fireRateLimitStatusListenerUpdate(ipRateLimitStatusListeners, rateLimitStatus);
                    }
                }
            }
        }

        private void fireRateLimitStatusListenerUpdate(List<RateLimitStatusListener> listeners,RateLimitStatus status){
            for(RateLimitStatusListener listener : listeners){
                listener.rateLimitStatusUpdated(status);
            }
        }

        public boolean equals(Object that){
            return that instanceof MyHttpResponseListener;
        }
    }

    /**
     * {@inheritDoc}
     */
    public QueryResult search(Query query) throws TwitterException {
        try{
        return TwitterTransport.createQueryResult(get(getSearchBaseURL() + "search.json", query.asPostParameters(), false));
        }catch(TwitterException te){
            if(404 == te.getStatusCode()){
                return new QueryResult(query);
            }else{
                throw te;
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public Trends getTrends() throws TwitterException {
        return TwitterTransport.createTrends(get(getSearchBaseURL() + "trends.json", false));
    }

    /**
     * {@inheritDoc}
     */
    public Trends getCurrentTrends() throws TwitterException {
        return TwitterTransport.createTrendsList(get(getSearchBaseURL() + "trends/current.json"
                , false)).get(0);
    }

    /**
     * {@inheritDoc}
     */
    public Trends getCurrentTrends(boolean excludeHashTags) throws TwitterException {
        return TwitterTransport.createTrendsList(get(getSearchBaseURL() + "trends/current.json"
                + (excludeHashTags ? "?exclude=hashtags" : ""), false)).get(0);
    }

    /**
     * {@inheritDoc}
     */
    public List<Trends> getDailyTrends() throws TwitterException {
        return TwitterTransport.createTrendsList(get(getSearchBaseURL() + "trends/daily.json", false));
    }

    /**
     * {@inheritDoc}
     */
    public List<Trends> getDailyTrends(Date date, boolean excludeHashTags) throws TwitterException {
        return TwitterTransport.createTrendsList(get(getSearchBaseURL()
                + "trends/daily.json?date=" + toDateStr(date)
                + (excludeHashTags ? "&exclude=hashtags" : ""), false));
    }

    private String toDateStr(Date date){
        if(null == date){
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * {@inheritDoc}
     */
    public List<Trends> getWeeklyTrends() throws TwitterException {
        return TwitterTransport.createTrendsList(get(getSearchBaseURL()
                + "trends/weekly.json", false));
    }

    /**
     * {@inheritDoc}
     */
    public List<Trends> getWeeklyTrends(Date date, boolean excludeHashTags) throws TwitterException {
        return TwitterTransport.createTrendsList(get(getSearchBaseURL()
                + "trends/weekly.json?date=" + toDateStr(date)
                + (excludeHashTags ? "&exclude=hashtags" : ""), false));
    }

    /* Status Methods */

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getPublicTimeline() throws
            TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() +
                "statuses/public_timeline.json", http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getPublicTimeline(long sinceID) throws
            TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() +
                "statuses/public_timeline.json", null, new Paging(sinceID).asPostParameterList(Paging.S)
                , http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getHomeTimeline() throws
            TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "statuses/home_timeline.json", true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getHomeTimeline(Paging paging) throws
            TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "statuses/home_timeline.json", null, paging.asPostParameterList(), true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFriendsTimeline() throws
            TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "statuses/friends_timeline.json", true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFriendsTimeline(Paging paging) throws
            TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "statuses/friends_timeline.json",null, paging.asPostParameterList(), true));
    }


    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(String screenName, Paging paging)
            throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL()
                + "statuses/user_timeline.json",
                new PostParameter[]{new PostParameter("screen_name",screenName)}
                , paging.asPostParameterList(), http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(int userId, Paging paging)
            throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL()
                + "statuses/user_timeline.json",
                new PostParameter[]{new PostParameter("user_id", userId)}
                , paging.asPostParameterList(), http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(String screenName) throws TwitterException {
        return getUserTimeline(screenName, new Paging());
    }
    
    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(int user_id) throws TwitterException {
        return getUserTimeline(user_id, new Paging());
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline() throws
            TwitterException {
        return getUserTimeline(new Paging());
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserTimeline(Paging paging) throws
            TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "statuses/user_timeline.json"
                , null, paging.asPostParameterList(), true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getMentions() throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "statuses/mentions.json",
                null, true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getMentions(Paging paging) throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "statuses/mentions.json",
                null, paging.asPostParameterList(), true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedByMe() throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "statuses/retweeted_by_me.json",
                null, true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedByMe(Paging paging) throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "statuses/retweeted_by_me.json",
                null, paging.asPostParameterList(), true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedToMe() throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "statuses/retweeted_to_me.json",
                null, true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetedToMe(Paging paging) throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "statuses/retweeted_to_me.json",
                null, paging.asPostParameterList(), true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "statuses/retweets_of_me.json",
                null, true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweetsOfMe(Paging paging) throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "statuses/retweets_of_me.json",
                null, paging.asPostParameterList(), true));
    }

    /**
     * {@inheritDoc}
     */
    public Status showStatus(long id) throws TwitterException {
        return TwitterTransport.createStatus(get(getBaseURL() + "statuses/show/" + id + ".json", http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status) throws TwitterException {
        return TwitterTransport.createStatus(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status), new PostParameter("source", source)}, true));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status, GeoLocation location) throws TwitterException {
        return TwitterTransport.createStatus(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status),
                        new PostParameter("lat", location.getLatitude()),
                        new PostParameter("long", location.getLongitude()),
                        new PostParameter("source", source)}, true));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status, long inReplyToStatusId) throws TwitterException {
        return TwitterTransport.createStatus(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status), new PostParameter("in_reply_to_status_id", String.valueOf(inReplyToStatusId)), new PostParameter("source", source)}, true));
    }

    /**
     * {@inheritDoc}
     */
    public Status updateStatus(String status, long inReplyToStatusId
            , GeoLocation location) throws TwitterException {
        return TwitterTransport.createStatus(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status),
                        new PostParameter("lat", location.getLatitude()),
                        new PostParameter("long", location.getLongitude()),
                        new PostParameter("in_reply_to_status_id",
                                String.valueOf(inReplyToStatusId)),
                        new PostParameter("source", source)}, true));
    }

    /**
     * {@inheritDoc}
     */
    public Status destroyStatus(long statusId) throws TwitterException {
        return TwitterTransport.createStatus(http.post(getBaseURL() + "statuses/destroy/" + statusId + ".json",
                new PostParameter[0], true));
    }

    /**
     * {@inheritDoc}
     */
    public Status retweetStatus(long statusId) throws TwitterException {
        return TwitterTransport.createStatus(http.post(getBaseURL() + "statuses/retweet/" + statusId + ".json",
                new PostParameter[0], true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getRetweets(long statusId) throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL()
                + "statuses/retweets/" + statusId + ".json", true));
    }

    /**
     * {@inheritDoc}
     */
    public User showUser(String screenName) throws TwitterException {
        return TwitterTransport.createUser(get(getBaseURL() + "users/show.json?screen_name="
                + screenName , http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public User showUser(int userId) throws TwitterException {
        return TwitterTransport.createUser(get(getBaseURL() + "users/show.json?user_id="
                + userId , http.isAuthenticationEnabled()));
    }

    /* User Methods */

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses() throws TwitterException {
        return getFriendsStatuses(-1l);
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses(long cursor) throws TwitterException {
        return TwitterTransport.createPagableUserResponseList(get(getBaseURL()
                + "statuses/friends.json?cursor=" + cursor, null,
                 http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses(String screenName) throws TwitterException {
        return getFriendsStatuses(screenName, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses(int userId) throws TwitterException {
        return getFriendsStatuses(userId, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses(String screenName, long cursor) throws TwitterException {
        return TwitterTransport.createPagableUserResponseList(get(getBaseURL()
                + "statuses/friends.json?screen_name=" + screenName + "&cursor=" + cursor
                , null, http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFriendsStatuses(int userId, long cursor) throws TwitterException {
        return TwitterTransport.createPagableUserResponseList(get(getBaseURL()
                + "statuses/friends.json?user_id=" + userId + "&cursor=" + cursor
                , null, http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses() throws TwitterException {
        return getFollowersStatuses(-1l);
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses(long cursor) throws TwitterException {
        return TwitterTransport.createPagableUserResponseList(get(getBaseURL()
                + "statuses/followers.json?cursor=" + cursor, null, http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses(String screenName) throws TwitterException {
        return getFollowersStatuses(screenName, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses(int userId) throws TwitterException {
        return getFollowersStatuses(userId, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses(String screenName, long cursor) throws TwitterException {
        return TwitterTransport.createPagableUserResponseList(get(getBaseURL() + "statuses/followers.json?screen_name=" + screenName +
                "&cursor=" + cursor, null, http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getFollowersStatuses(int userId, long cursor) throws TwitterException {
        return TwitterTransport.createPagableUserResponseList(get(getBaseURL() + "statuses/followers.json?user_id=" + userId +
                "&cursor=" + cursor, null, http.isAuthenticationEnabled()));
    }

    /*List Methods*/

    /**
     * {@inheritDoc}
     */
    public UserList createUserList(String listName, boolean isPublicList, String description) throws TwitterException {
        List<PostParameter> postParams = new ArrayList<PostParameter>();
        postParams.add(new PostParameter("name", listName));
        postParams.add(new PostParameter("mode", isPublicList ? "public" : "private"));
        if (description != null) {
            postParams.add(new PostParameter("description", description));
        }
        return TwitterTransport.createUserList(http.post(getBaseURL() + getScreenName() +
                                            "/lists.json",
											postParams.toArray(new PostParameter[postParams.size()]),
                                            true));
    }

    /**
     * {@inheritDoc}
     */
    public UserList updateUserList(int listId, String newListName, boolean isPublicList, String newDescription) throws TwitterException {
        List<PostParameter> postParams = new ArrayList<PostParameter>();
        if (newListName != null) {
            postParams.add(new PostParameter("name", newListName));
        }
        postParams.add(new PostParameter("mode", isPublicList ? "public" : "private"));
        if (newDescription != null) {
            postParams.add(new PostParameter("description", newDescription));
        }
        return TwitterTransport.createUserList(http.post(getBaseURL() + getScreenName() + "/lists/"
                + listId + ".json", postParams.toArray(new PostParameter[postParams.size()]), true));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<UserList> getUserLists(String listOwnerScreenName, long cursor) throws TwitterException {
        return TwitterTransport.createUserListList(get(getBaseURL() +
                listOwnerScreenName + "/lists.json?cursor=" + cursor, true));
    }

    /**
     * {@inheritDoc}
     */
    public UserList showUserList(String listOwnerScreenName, int id) throws TwitterException {
        return TwitterTransport.createUserList(get(getBaseURL() + listOwnerScreenName + "/lists/"
                + id + ".json", true));
    }

    /**
     * {@inheritDoc}
     */
    public UserList destroyUserList(int listId) throws TwitterException {
        return TwitterTransport.createUserList(http.delete(getBaseURL() + getScreenName() +
                "/lists/" + listId + ".json", true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getUserListStatuses(String listOwnerScreenName, int id, Paging paging) throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + listOwnerScreenName +
                "/lists/" + id + "/statuses.json", new PostParameter[0],
                paging.asPostParameterList(Paging.SMCP, Paging.PER_PAGE), http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<UserList> getUserListMemberships(String listOwnerScreenName, long cursor) throws TwitterException {
        return TwitterTransport.createUserListList(get(getBaseURL() +
                listOwnerScreenName + "/lists/memberships.json?cursor=" + cursor, true));
    }

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<UserList> getUserListSubscriptions(String listOwnerScreenName, long cursor) throws TwitterException {
        return TwitterTransport.createUserListList(get(getBaseURL() +
                listOwnerScreenName + "/lists/subscriptions.json?cursor=" + cursor, true));
    }

    /*List Members Methods*/

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getUserListMembers(String listOwnerScreenName, int listId
            , long cursor) throws TwitterException {
        return TwitterTransport.createPagableUserResponseList(get(getBaseURL() +
                listOwnerScreenName + "/" + listId + "/members.json?cursor=" + cursor, true));
    }

    /**
     * {@inheritDoc}
     */
    public UserList addUserListMember(int listId, int userId) throws TwitterException {
        return TwitterTransport.createUserList(http.post(getBaseURL() + getScreenName() +
                "/" + listId + "/members.json?id=" + userId, true));
    }

    /**
     * {@inheritDoc}
     */
    public UserList deleteUserListMember(int listId, int userId) throws TwitterException {
        return TwitterTransport.createUserList(http.delete(getBaseURL() + getScreenName() +
                "/" + listId + "/members.json?id=" + userId, true));
    }

    /**
     * {@inheritDoc}
     */
    public User checkUserListMembership(String listOwnerScreenName, int listId, int userId) throws TwitterException {
        return TwitterTransport.createUser(get(getBaseURL() + listOwnerScreenName + "/" + listId
                + "/members/"+ userId +".json", true));
    }

    /*List Subscribers Methods*/

    /**
     * {@inheritDoc}
     */
    public PagableResponseList<User> getUserListSubscribers(String listOwnerScreenName
            , int listId, long cursor) throws TwitterException {
        return TwitterTransport.createPagableUserResponseList(get(getBaseURL() +
                listOwnerScreenName + "/" + listId + "/subscribers.json?cursor=" + cursor, true));
    }

    /**
     * {@inheritDoc}
     */
    public UserList subscribeUserList(String listOwnerScreenName, int listId) throws TwitterException {
        return TwitterTransport.createUserList(http.post(getBaseURL() + listOwnerScreenName +
                "/" + listId + "/subscribers.json", true));
    }

    /**
     * {@inheritDoc}
     */
    public UserList unsubscribeUserList(String listOwnerScreenName, int listId) throws TwitterException {
        return TwitterTransport.createUserList(http.delete(getBaseURL() + listOwnerScreenName +
                "/" + listId + "/subscribers.json?id=" + verifyCredentials().getId(), true));
    }

    /**
     * {@inheritDoc}
     */
    public User checkUserListSubscription(String listOwnerScreenName, int listId, int userId) throws TwitterException {
        return TwitterTransport.createUser(get(getBaseURL() + listOwnerScreenName + "/" + listId
                + "/subscribers/" + userId + ".json", true));
    }

    /*Direct Message Methods */

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getDirectMessages() throws TwitterException {
        return TwitterTransport.createDirectMessageList(get(getBaseURL() + "direct_messages.json", true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getDirectMessages(Paging paging) throws TwitterException {
        return TwitterTransport.createDirectMessageList(get(getBaseURL()
                + "direct_messages.json", null, paging.asPostParameterList(), true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getSentDirectMessages() throws
            TwitterException {
        return TwitterTransport.createDirectMessageList(get(getBaseURL() +
                "direct_messages/sent.json", new PostParameter[0], true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<DirectMessage> getSentDirectMessages(Paging paging) throws
            TwitterException {
        return TwitterTransport.createDirectMessageList(get(getBaseURL() +
                "direct_messages/sent.json", new PostParameter[0],
                paging.asPostParameterList(), true));
    }

    /**
     * {@inheritDoc}
     */
    public DirectMessage sendDirectMessage(String screenName, String text) throws TwitterException {
        return TwitterTransport.createDirectMessage(http.post(getBaseURL() + "direct_messages/new.json",
                new PostParameter[]{new PostParameter("screen_name", screenName),
                        new PostParameter("text", text)}, true));
    }

    /**
     * {@inheritDoc}
     */
    public DirectMessage sendDirectMessage(int userId, String text)
            throws TwitterException {
        return TwitterTransport.createDirectMessage(http.post(getBaseURL() + "direct_messages/new.json",
                new PostParameter[]{new PostParameter("user_id", userId),
                        new PostParameter("text", text)}, true));
    }

    /**
     * {@inheritDoc}
     */
    public DirectMessage destroyDirectMessage(int id) throws
            TwitterException {
        return TwitterTransport.createDirectMessage(http.post(getBaseURL() +
                "direct_messages/destroy/" + id + ".json", new PostParameter[0], true));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(String screenName) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "friendships/create.json?screen_name=" + screenName, new PostParameter[0], true));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(int userId) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "friendships/create.json?user_id=" + userId, new PostParameter[0], true));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(String screenName, boolean follow) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "friendships/create.json?screen_name=" + screenName
                + "&follow=" + follow , true));
    }

    /**
     * {@inheritDoc}
     */
    public User createFriendship(int userId, boolean follow) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "friendships/create.json?user_id=" + userId
                + "&follow=" + follow , true));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyFriendship(String screenName) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "friendships/destroy.json?screen_name="
                + screenName, true));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyFriendship(int userId) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "friendships/destroy.json?user_id="
                + userId, true));
    }

    /**
     * {@inheritDoc}
     */
    public boolean existsFriendship(String userA, String userB) throws TwitterException {
        return -1 != get(getBaseURL() + "friendships/exists.json", "user_a", userA, "user_b", userB, http.isAuthenticationEnabled()).
                asString().indexOf("true");
    }

    /**
     * {@inheritDoc}
     */
    public Relationship showFriendship(String sourceScreenName, String targetScreenName) throws TwitterException {
        return TwitterTransport.createRelationship(get(getBaseURL() + "friendships/show.json", "source_screen_name", sourceScreenName,
                "target_screen_name", targetScreenName, http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public Relationship showFriendship(int sourceId, int targetId) throws TwitterException {
        return TwitterTransport.createRelationship(get(getBaseURL() + "friendships/show.json", "source_id", String.valueOf(sourceId),
                "target_id", String.valueOf(targetId), http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFriendsIDs() throws TwitterException {
        return getFriendsIDs(-1l);
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFriendsIDs(long cursor) throws TwitterException {
        return TwitterTransport.createFriendsIDs(get(getBaseURL() + "friends/ids.json?cursor=" + cursor, http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFriendsIDs(int userId) throws TwitterException {
        return getFriendsIDs(userId, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFriendsIDs(int userId, long cursor) throws TwitterException {
        return TwitterTransport.createFriendsIDs(get(getBaseURL() + "friends/ids.json?user_id=" + userId +
                "&cursor=" + cursor, http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFriendsIDs(String screenName) throws TwitterException {
        return getFriendsIDs(screenName, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFriendsIDs(String screenName, long cursor) throws TwitterException {
        return TwitterTransport.createFriendsIDs(get(getBaseURL() + "friends/ids.json?screen_name=" + screenName
                + "&cursor=" + cursor, http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFollowersIDs() throws TwitterException {
        return getFollowersIDs(-1l);
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFollowersIDs(long cursor) throws TwitterException {
        return TwitterTransport.createFriendsIDs(get(getBaseURL() + "followers/ids.json?cursor=" + cursor
                , http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFollowersIDs(int userId) throws TwitterException {
        return getFollowersIDs(userId, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFollowersIDs(int userId, long cursor) throws TwitterException {
        return TwitterTransport.createFriendsIDs(get(getBaseURL() + "followers/ids.json?user_id=" + userId
                + "&cursor=" + cursor, http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFollowersIDs(String screenName) throws TwitterException {
        return getFollowersIDs(screenName, -1l);
    }

    /**
     * {@inheritDoc}
     */
    public IDs getFollowersIDs(String screenName, long cursor) throws TwitterException {
        return TwitterTransport.createFriendsIDs(get(getBaseURL() + "followers/ids.json?screen_name="
                + screenName + "&cursor=" + cursor, http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public User verifyCredentials() throws TwitterException {
        return TwitterTransport.createUser(get(getBaseURL() + "account/verify_credentials.json"
                , true));
    }

    /**
     * {@inheritDoc}
     */
    public User updateProfile(String name, String email, String url
            , String location, String description) throws TwitterException {
        List<PostParameter> profile = new ArrayList<PostParameter>(5);
        addParameterToList(profile, "name", name);
        addParameterToList(profile, "email", email);
        addParameterToList(profile, "url", url);
        addParameterToList(profile, "location", location);
        addParameterToList(profile, "description", description);
        return TwitterTransport.createUser(http.post(getBaseURL() + "account/update_profile.json"
                , profile.toArray(new PostParameter[profile.size()]), true));
    }

    /**
     * {@inheritDoc}
     */
    public RateLimitStatus getRateLimitStatus() throws TwitterException {
        return RateLimitStatus.createFromJSONResponse(http.get(getBaseURL() + "account/rate_limit_status.json", http.isAuthenticationEnabled()));
    }

    /**
     * {@inheritDoc}
     */
    public User updateDeliveryDevice(Device device) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "account/update_delivery_device.json", new PostParameter[]{new PostParameter("device", device.getName())}, true));
    }


    /**
     * {@inheritDoc}
     */
    public User updateProfileColors(
            String profileBackgroundColor,
            String profileTextColor,
            String profileLinkColor,
            String profileSidebarFillColor,
            String profileSidebarBorderColor)
            throws TwitterException {
        List<PostParameter> colors = new ArrayList<PostParameter>(5);
        addParameterToList(colors, "profile_background_color"
                , profileBackgroundColor);
        addParameterToList(colors, "profile_text_color"
                , profileTextColor);
        addParameterToList(colors, "profile_link_color"
                , profileLinkColor);
        addParameterToList(colors, "profile_sidebar_fill_color"
                , profileSidebarFillColor);
        addParameterToList(colors, "profile_sidebar_border_color"
                , profileSidebarBorderColor);
        return TwitterTransport.createUser(http.post(getBaseURL() +
                "account/update_profile_colors.json",
                colors.toArray(new PostParameter[colors.size()]), true));
    }

    private void addParameterToList(List<PostParameter> colors,
                                      String paramName, String color) {
        if(null != color){
            colors.add(new PostParameter(paramName,color));
        }
    }

    /**
     * {@inheritDoc}
     */
    public User updateProfileImage(File image) throws TwitterException {
        checkFileValidity(image);
        return TwitterTransport.createUser(http.post(getBaseURL()
                + "account/update_profile_image.json",
                new PostParameter[]{new PostParameter("image", image)}, true));
    }

    /**
     * {@inheritDoc}
     */
    public User updateProfileBackgroundImage(File image, boolean tile)
            throws TwitterException {
        checkFileValidity(image);
        return TwitterTransport.createUser(http.post(getBaseURL()
                + "account/update_profile_background_image.json",
                new PostParameter[]{new PostParameter("image", image),
                new PostParameter("tile", tile)}, true));
    }

    /**
     * Check the existence, and the type of the specified file.
     * @param image image to be uploaded
     * @throws TwitterException when the specified file is not found (FileNotFoundException will be nested)
     * , or when the specified file object is not representing a file(IOException will be nested).
     */
    private void checkFileValidity(File image) throws TwitterException {
        if (!image.exists()) {
			//noinspection ThrowableInstanceNeverThrown
			throw new TwitterException(new FileNotFoundException(image +" is not found."));
        }
        if (!image.isFile()) {
			//noinspection ThrowableInstanceNeverThrown
            throw new TwitterException(new IOException(image +" is not a file."));
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites() throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "favorites.json", new PostParameter[0], true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites(int page) throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "favorites.json", "page", String.valueOf(page), true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites(String id) throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "favorites/" + id + ".json", new PostParameter[0], true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<Status> getFavorites(String id, int page) throws TwitterException {
        return TwitterTransport.createStatusList(get(getBaseURL() + "favorites/" + id + ".json", "page", String.valueOf(page), true));
    }

    /**
     * {@inheritDoc}
     */
    public Status createFavorite(long id) throws TwitterException {
        return TwitterTransport.createStatus(http.post(getBaseURL() + "favorites/create/" + id + ".json", true));
    }

    /**
     * {@inheritDoc}
     */
    public Status destroyFavorite(long id) throws TwitterException {
        return TwitterTransport.createStatus(http.post(getBaseURL() + "favorites/destroy/" + id + ".json", true));
    }

    /**
     * {@inheritDoc}
     */
    public User enableNotification(String screenName) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "notifications/follow.json?screen_name=" + screenName, true));
    }

    /**
     * {@inheritDoc}
     */
    public User enableNotification(int userId) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "notifications/follow.json?userId=" + userId, true));
    }

    /**
     * {@inheritDoc}
     */
    public User disableNotification(String screenName) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "notifications/leave.json?screen_name=" + screenName, true));
    }

    /**
     * {@inheritDoc}
     */
    public User disableNotification(int userId) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "notifications/leave.json?user_id=" + userId, true));
    }

    /* Block Methods */

    /**
     * {@inheritDoc}
     */
    public User createBlock(String screenName) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "blocks/create.json?screen_name=" + screenName, true));
    }

    /**
     * {@inheritDoc}
     */
    public User createBlock(int userId) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "blocks/create.json?user_id=" + userId, true));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyBlock(String screen_name) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "blocks/destroy.json?screen_name=" + screen_name, true));
    }

    /**
     * {@inheritDoc}
     */
    public User destroyBlock(int userId) throws TwitterException {
        return TwitterTransport.createUser(http.post(getBaseURL() + "blocks/destroy.json?user_id=" + userId, true));
    }

    /**
     * {@inheritDoc}
     */
    public boolean existsBlock(String screenName) throws TwitterException {
        try{
            // @todo this method looks to be always returning false as it's expecting an XML format.
            return -1 == get(getBaseURL() + "blocks/exists.json?screen_name=" + screenName, true).
                    asString().indexOf("<error>You are not blocking this user.</error>");
        }catch(TwitterException te){
            if(te.getStatusCode() == 404){
                return false;
            }
            throw te;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean existsBlock(int userId) throws TwitterException {
        try{
            return -1 == get(getBaseURL() + "blocks/exists.json?user_id=" + userId, true).
                    asString().indexOf("<error>You are not blocking this user.</error>");
        }catch(TwitterException te){
            if(te.getStatusCode() == 404){
                return false;
            }
            throw te;
        }
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<User> getBlockingUsers() throws
            TwitterException {
        return TwitterTransport.createUserResponseList(get(getBaseURL() +
                "blocks/blocking.json", true));
    }

    /**
     * {@inheritDoc}
     */
    public ResponseList<User> getBlockingUsers(int page) throws
            TwitterException {
        return TwitterTransport.createUserResponseList(get(getBaseURL() +
                "blocks/blocking.json?page=" + page, true));
    }

    /**
     * {@inheritDoc}
     */
    public IDs getBlockingUsersIDs() throws TwitterException {
        return TwitterTransport.createBlockIDs(get(getBaseURL() + "blocks/blocking/ids.json", true));
    }

    /* Saved Searches Methods */

    /**
     * {@inheritDoc}
     */
    public List<SavedSearch> getSavedSearches() throws TwitterException {
        return TwitterTransport.createSavedSearchList(get(getBaseURL() + "saved_searches.json", true));
    }

    /**
     * {@inheritDoc}
     */
    public SavedSearch showSavedSearch(int id) throws TwitterException {
        return TwitterTransport.createSavedSearch(get(getBaseURL() + "saved_searches/show/" + id
                + ".json", true));
    }

    /**
     * {@inheritDoc}
     */
    public SavedSearch createSavedSearch(String query) throws TwitterException {
        return TwitterTransport.createSavedSearch(http.post(getBaseURL() + "saved_searches/create.json"
                , new PostParameter[]{new PostParameter("query", query)}, true));
    }

    /**
     * {@inheritDoc}
     */
    public SavedSearch destroySavedSearch(int id) throws TwitterException {
        return TwitterTransport.createSavedSearch(http.post(getBaseURL()
                + "saved_searches/destroy/" + id + ".json", true));
    }

    /* Help Methods */

    /**
     * {@inheritDoc}
     */
    public boolean test() throws TwitterException {
        return -1 != get(getBaseURL() + "help/test.json", false).
                asString().indexOf("ok");
    }
    @Override
    public String toString() {
        return "Twitter{" +
                "http=" + http +
                ", source='" + source + '\'' +
                ", USE_SSL=" + USE_SSL +
                ", accountRateLimitStatusListeners=" + accountRateLimitStatusListeners +
                ", ipRateLimitStatusListeners=" + ipRateLimitStatusListeners +
                '}';
    }
}
