package twitter4j.impl;

import twitter4j.*;
import static twitter4j.impl.ParseUtil.*;

import twitter4j.http.Response;
import twitter4j.org.json.JSONArray;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

import java.util.*;


public final class TwitterTransport {

    private TwitterTransport(){}

    public static RateLimitStatus createRateLimitStatus(JSONObject json) throws TwitterException {
        return new RateLimitStatus(getInt("hourly_limit", json),
                getInt("remaining_hits", json),
                getInt("reset_time_in_seconds", json),
                getDate("reset_time", json, "EEE MMM d HH:mm:ss Z yyyy"));
    }

    public static RateLimitStatus createRateLimitStatus(Response res) {
        int remainingHits;//"X-RateLimit-Remaining"
        int hourlyLimit;//"X-RateLimit-Limit"
        int resetTimeInSeconds;//not included in the response header. Need to be calculated.
        Date resetTime;//new Date("X-RateLimit-Reset")


        String limit = res.getResponseHeader("X-RateLimit-Limit");
        if(null != limit){
            hourlyLimit = Integer.parseInt(limit);
        }else{
            return null;
        }
        String remaining = res.getResponseHeader("X-RateLimit-Remaining");
        if(null != remaining){
            remainingHits = Integer.parseInt(remaining);
        }else{
            return null;
        }
        String reset = res.getResponseHeader("X-RateLimit-Reset");
        if(null != reset){
            long longReset =  Long.parseLong(reset) * 1000;
            resetTime = new Date(longReset);
            resetTimeInSeconds =  (int)(longReset - System.currentTimeMillis()) / 1000;
        }else{
            return null;
        }
        return new RateLimitStatus(hourlyLimit, remainingHits, resetTimeInSeconds, resetTime);
    }


    public static Status createStatus(Response res) throws TwitterException
    {
        RateLimitStatus rateLimit = createRateLimitStatus(res);
        Status result = createStatus(res.asJSONObject());
        result.setRateLimitStatus(rateLimit);
		return result;
    }


    public static Status createStatus(JSONObject json) throws TwitterException {
        Status result=new Status();
        result.setId(getLong("id", json));
        result.setText(ParseUtil.getText("text", json));
        result.setSource(ParseUtil.getText("source", json));
        result.setCreatedAt(getDate("created_at", json));
        result.setTruncated(getBoolean("truncated", json));
        result.setFavorited(getBoolean("favorited", json));

        long replyToStatusId = getLong("in_reply_to_status_id", json);
        if(replyToStatusId >= 0)
        {
            Status.InReplyTo reply = new Status.InReplyTo();
            reply.setStatusId(replyToStatusId);
            reply.setUserId(getInt("in_reply_to_user_id", json));
            reply.setUserScreenName(ParseUtil.getText("in_reply_to_screen_name", json));
            result.setInReplyTo(reply);
        }
        try {
            if (!json.isNull("user")) {
                result.setUser(createUser(json.getJSONObject("user")));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        try {
            if (!json.isNull("geo")) {
                String coordinates = json.getJSONObject("geo")
                        .getString("coordinates");
                coordinates = coordinates.substring(1, coordinates.length() - 1);
                String[] point = coordinates.split(",");
                result.setGeoLocation(new GeoLocation(Double.parseDouble(point[0]),
                        Double.parseDouble(point[1])));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        if (!json.isNull("retweeted_status")) {
            try {
                result.setRetweetedStatus(createStatus(json.getJSONObject("retweeted_status")));
            } catch (JSONException ignore) {
            }
        }
        return result;
    }

    public static ResponseList<Status> createStatusList(Response res) throws TwitterException {
        try {
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<Status> statuses = new ResponseList<Status>(size);
            statuses.setRateLimitStatus(createRateLimitStatus(res));
            for (int i = 0; i < size; i++) {
                statuses.add(TwitterTransport.createStatus(list.getJSONObject(i)));
            }
            return statuses;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }


    public static User createUser(Response res) throws TwitterException
    {
        RateLimitStatus rateLimit = createRateLimitStatus(res);
        User result = createUser(res.asJSONObject());
        result.setRateLimitStatus(rateLimit);
        return result;
    }

    public static User createUser(JSONObject json) throws TwitterException {
        User result=new User();
        try {
            result.setId(json.getInt("id"));
            result.setName(json.getString("name"));
            result.setScreenName(json.getString("screen_name"));
            result.setLocation(json.getString("location"));
            result.setDescription(json.getString("description"));
            result.setURL(json.getString("url"));
            result.setProtected(json.getBoolean("protected"));
            result.setGeoEnabled(json.getBoolean("geo_enabled"));
            result.setVerified(json.getBoolean("verified"));
            result.setFollowersCount(json.getInt("followers_count"));

            User.Profile profile = new User.Profile();

            profile.setImageUrl(json.getString("profile_image_url"));
            profile.setBackgroundColor(json.getString("profile_background_color"));
            profile.setTextColor(json.getString("profile_text_color"));
            profile.setLinkColor(json.getString("profile_link_color"));
            profile.setSidebarFillColor(json.getString("profile_sidebar_fill_color"));
            profile.setSidebarBorderColor(json.getString("profile_sidebar_border_color"));
            profile.setBackgroundImageUrl(json.getString("profile_background_image_url"));
            profile.setBackgroundTile(json.getString("profile_background_tile"));

            result.setProfile(profile);

            result.setFriendsCount(json.getInt("friends_count"));
            result.setCreatedAt(parseDate(json.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy"));
            result.setFavouritesCount(json.getInt("favourites_count"));
            result.setUtcOffset(getInt("utc_offset", json));
            result.setTimeZone(json.getString("time_zone"));
            result.setStatusesCount(json.getInt("statuses_count"));
            if (!json.isNull("status")) {
                JSONObject status = json.getJSONObject("status");
                result.setStatus(createStatus(status));
            /*
                statusCreatedAt = parseDate(status.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy");
                statusId = status.getLong("id");
                statusText = status.getString("text");
                statusSource = status.getString("source");
                statusTruncated = status.getBoolean("truncated");
                statusInReplyToStatusId = getLong("in_reply_to_status_id", status);
                statusInReplyToUserId = getInt("in_reply_to_user_id", status);
                statusFavorited = status.getBoolean("favorited");
                statusInReplyToScreenName = status.getString("in_reply_to_screen_name");
            */
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
        return result;
    }

    public static PagableResponseList<User> createPagableUserResponseList(Response res) throws TwitterException {
        try {
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("users");
            int size = list.length();
            PagableResponseList<User> users = new PagableResponseList<User>(size);
            users.setRateLimitStatus(createRateLimitStatus(res));
            users.setPreviousCursor(ParseUtil.getLong("previous_cursor", json));
            users.setNextCursor(ParseUtil.getLong("next_cursor", json));

            for (int i = 0; i < size; i++) {
                users.add(createUser(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }
    public static ResponseList<User> createUserResponseList(Response res) throws TwitterException {
        try {
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<User> users =
                    new ResponseList<User>(size);
            users.setRateLimitStatus(createRateLimitStatus(res));
            for (int i = 0; i < size; i++) {
                users.add(createUser(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    public static UserList createUserList(Response res) throws TwitterException {
        RateLimitStatus rateLimit = createRateLimitStatus(res);
        UserList result = createUserList(res.asJSONObject());
        result.setRateLimitStatus(rateLimit);
        return result;
    }

    public static UserList createUserList(JSONObject json) throws TwitterException {
        UserList result = new UserList();
        try {
            result.setId(json.getInt("id"));
            result.setName(json.getString("name"));
            result.setFullName(json.getString("full_name"));
            result.setSlug(json.getString("slug"));
            result.setDescription(json.getString("description"));
            result.setSubscriberCount(json.getInt("subscriber_count"));
            result.setMemberCount(json.getInt("member_count"));
            result.setURI(json.getString("uri"));
            result.setMode(json.getString("mode"));
            if (!json.isNull("user")) {
                result.setUser(TwitterTransport.createUser(json.getJSONObject("user")));
            }
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
        return result;
    }

    public static PagableResponseList<UserList> createUserListList(Response res) throws TwitterException {
        try {
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("lists");
            int size = list.length();
            PagableResponseList<UserList> users =
                    new PagableResponseList<UserList>(size);
            users.setRateLimitStatus(createRateLimitStatus(res));
            users.setPreviousCursor(ParseUtil.getLong("previous_cursor", json));
            users.setNextCursor(ParseUtil.getLong("next_cursor", json));

            for (int i = 0; i < size; i++) {
                users.add(TwitterTransport.createUserList(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    public static DirectMessage createDirectMessage(Response res) throws TwitterException {
        RateLimitStatus rateLimit = createRateLimitStatus(res);
        DirectMessage result = createDirectMessage(res.asJSONObject());
        result.setRateLimitStatus(rateLimit);
        return result;

    }

    public static DirectMessage createDirectMessage(JSONObject json) throws TwitterException {
        DirectMessage result=new DirectMessage();
        result.setId(getInt("id", json));
        result.setText(ParseUtil.getText("text", json));
        result.setSenderId(getInt("sender_id", json));
        result.setRecipientId(getInt("recipient_id", json));
        result.setCreatedAt(getDate("created_at", json));
        result.setSenderScreenName(ParseUtil.getText("sender_screen_name", json));
        result.setRecipientScreenName(ParseUtil.getText("recipient_screen_name", json));
        try {
            result.setSender(createUser(json.getJSONObject("sender")));
            result.setRecipient(createUser(json.getJSONObject("recipient")));
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        return result;
    }

    public static ResponseList<DirectMessage> createDirectMessageList(Response res) throws TwitterException {
        try {
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<DirectMessage> directMessages = new ResponseList<DirectMessage>(size);
            directMessages.setRateLimitStatus(createRateLimitStatus(res));
            for (int i = 0; i < size; i++) {
                directMessages.add(createDirectMessage(list.getJSONObject(i)));
            }
            return directMessages;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
    }

    public static Tweet createTweet(JSONObject json) throws TwitterException {
        Tweet result=new Tweet();
        try {
            result.setText(getString("text", json, false));
            try{
                result.setToUserId(json.getInt("to_user_id"));
                result.setToUser(json.getString("to_user"));
            }catch(JSONException ignore){
                // to_user_id can be "null"
                // to_user can be missing
            }
            result.setFromUser(json.getString("from_user"));
            result.setId(json.getLong("id"));
            result.setFromUserId(json.getInt("from_user_id"));
            try{
                result.setIsoLanguageCode(json.getString("iso_language_code"));
            }catch(JSONException ignore){
                // iso_language_code can be missing
            }
            result.setSource(getString("source", json, true));
            result.setProfileImageUrl(getString("profile_image_url", json, true));
            result.setCreatedAt(parseDate(json.getString("created_at"), "EEE, dd MMM yyyy HH:mm:ss z"));
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
        return result;
    }

    public static QueryResult createQueryResult(Response res) throws TwitterException {
        JSONObject json = res.asJSONObject();
        QueryResult result=new QueryResult();
        try {
            result.setSinceId(json.getLong("since_id"));
            result.setMaxId(json.getLong("max_id"));
            result.setRefreshUrl(getString("refresh_url", json, true));

            result.setResultsPerPage(json.getInt("results_per_page"));
            result.setWarning(getString("warning", json, false));
            result.setCompletedIn(json.getDouble("completed_in"));
            result.setPage(json.getInt("page"));
            result.setQuery(getString("query", json, true));
            JSONArray array = json.getJSONArray("results");
            List<Tweet> tweets = new ArrayList<Tweet>(array.length());
            for (int i = 0; i < array.length(); i++) {
                JSONObject tweet = array.getJSONObject(i);
                tweets.add(createTweet(tweet));
            }
            result.setTweets(tweets);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
        return result;
    }

    public static Relationship createRelationship(Response res) throws TwitterException {
        RateLimitStatus rateLimit = createRateLimitStatus(res);
        Relationship result = createRelationship(res.asJSONObject());
        result.setRateLimitStatus(rateLimit);
        return result;
    }
    public static Relationship createRelationship(JSONObject json) throws TwitterException {
        Relationship result=new Relationship();
        try {
            JSONObject relationship = json.getJSONObject("relationship");
            JSONObject sourceJson = relationship.getJSONObject("source");
            JSONObject targetJson = relationship.getJSONObject("target");
            result.setSourceUserId(getInt("id", sourceJson));
            result.setTargetUserId(getInt("id", targetJson));
            result.setSourceUserScreenName(getText("screen_name", sourceJson));
            result.setTargetUserScreenName(getText("screen_name", targetJson));
            result.setSourceBlockingTarget(getBoolean("blocking", sourceJson));
            result.setSourceFollowingTarget(getBoolean("following", sourceJson));
            result.setSourceFollowedByTarget(getBoolean("followed_by", sourceJson));
            result.setSourceNotificationsEnabled(getBoolean("notifications_enabled", sourceJson));
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + json.toString(), jsone);
        }
        return result;
    }

    public static Trend createTrend(JSONObject json) throws JSONException {
        Trend result=new Trend();
        result.setName(json.getString("name"));
        if (!json.isNull("url")) {
            result.setUrl(json.getString("url"));
        }
        if (!json.isNull("query")) {
            result.setQuery(json.getString("query"));
        }
        return result;
    }

    public static List<Trends> createTrendsList(Response res) throws
            TwitterException {
        JSONObject json = res.asJSONObject();
        List<Trends> trends;
        try {
            Date asOf = parseTrendsDate(json.getString("as_of"));
            JSONObject trendsJson = json.getJSONObject("trends");
            trends = new ArrayList<Trends>(trendsJson.length());
            Iterator ite = trendsJson.keys();
            while (ite.hasNext()) {
                String key = (String) ite.next();
                JSONArray array = trendsJson.getJSONArray(key);
                Trend[] trendsArray = jsonArrayToTrendArray(array);
                if (key.length() == 19) {
                    // current trends
                    trends.add(new Trends(asOf, parseDate(key
                            , "yyyy-MM-dd HH:mm:ss"), trendsArray));
                } else if (key.length() == 16) {
                    // daily trends
                    trends.add(new Trends(asOf, parseDate(key
                            , "yyyy-MM-dd HH:mm"), trendsArray));
                } else if (key.length() == 10) {
                    // weekly trends
                    trends.add(new Trends(asOf, parseDate(key
                            , "yyyy-MM-dd"), trendsArray));
                }
            }
            Collections.sort(trends);
            return trends;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + res.asString(), jsone);
        }
    }
    public static Trends createTrends(Response res) throws TwitterException {
        JSONObject json = res.asJSONObject();
        try {
            Date asOf = parseTrendsDate(json.getString("as_of"));
            JSONArray array = json.getJSONArray("trends");
            Trend[] trendsArray = jsonArrayToTrendArray(array);
            return new Trends(asOf, asOf, trendsArray);
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + res.asString(), jsone);
        }
    }

    private static Date parseTrendsDate(String asOfStr) throws TwitterException {
        Date parsed;
        if (asOfStr.length() == 10) {
            parsed = new Date(Long.parseLong(asOfStr) * 1000);
        } else {
            parsed = parseDate(asOfStr, "EEE, d MMM yyyy HH:mm:ss z");
        }
        return parsed;
    }

    private static Trend[] jsonArrayToTrendArray(JSONArray array) throws JSONException {
        Trend[] trends = new Trend[array.length()];
        for (int i = 0; i < array.length(); i++) {
            JSONObject trend = array.getJSONObject(i);
            trends[i] = createTrend(trend);
        }
        return trends;
    }

    public static SavedSearch createSavedSearch(Response res) throws TwitterException {
        RateLimitStatus rateLimit = createRateLimitStatus(res);
        SavedSearch result = createSavedSearch(res.asJSONObject());
        result.setRateLimitStatus(rateLimit);
        return result;

    }
    public static SavedSearch createSavedSearch(JSONObject savedSearch) throws TwitterException {
        SavedSearch result=new SavedSearch();
        try {
            result.setCreatedAt(parseDate(savedSearch.getString("created_at"), "EEE MMM dd HH:mm:ss z yyyy"));
            result.setQuery(getString("query", savedSearch, true));
            result.setPosition(getInt("position", savedSearch));
            result.setName(getString("name", savedSearch, true));
            result.setId(getInt("id", savedSearch));
        } catch (JSONException jsone) {
            throw new TwitterException(jsone.getMessage() + ":" + savedSearch.toString(), jsone);
        }
        return result;
    }

    public static ResponseList<SavedSearch> createSavedSearchList(Response res) throws TwitterException {
            JSONArray json = res.asJSONArray();
            ResponseList<SavedSearch> savedSearches;
            try {
                savedSearches = new ResponseList<SavedSearch>(json.length());
                savedSearches.setRateLimitStatus(createRateLimitStatus(res));

                for(int i=0;i<json.length();i++){
                    savedSearches.add(createSavedSearch(json.getJSONObject(i)));
                }
                return savedSearches;
            } catch (JSONException jsone) {
                throw new TwitterException(jsone.getMessage() + ":" + res.asString(), jsone);
            }
        }

    public static IDs createFriendsIDs(Response res) throws TwitterException {
        IDs friendsIDs = new IDs();
        friendsIDs.setRateLimitStatus(createRateLimitStatus(res));
        JSONObject json = res.asJSONObject();
        JSONArray idList;
        try {
            idList = json.getJSONArray("ids");
            int[] ids = new int[idList.length()];
            for (int i = 0; i < idList.length(); i++) {
                try {
                    ids[i] = Integer.parseInt(idList.getString(i));
                } catch (NumberFormatException nfe) {
                    throw new TwitterException("Twitter API returned malformed response: " + json, nfe);
                }
            }
            friendsIDs.setIDs(ids);
            friendsIDs.setPreviousCursor(ParseUtil.getLong("previous_cursor", json));
            friendsIDs.setNextCursor(ParseUtil.getLong("next_cursor", json));
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        return friendsIDs;
    }


    public static IDs createBlockIDs(Response res) throws TwitterException {
        IDs blockIDs = new IDs();
        blockIDs.setRateLimitStatus(createRateLimitStatus(res));
        JSONArray idList = null;
        try {
            idList = res.asJSONArray();
            int[] ids = new int[idList.length()];
            for (int i = 0; i < idList.length(); i++) {
                try {
                    ids[i] = Integer.parseInt(idList.getString(i));
                } catch (NumberFormatException nfe) {
                    throw new TwitterException("Twitter API returned malformed response: " + idList, nfe);
                }
            }
            blockIDs.setIDs(ids);

        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        }
        return blockIDs;
    }

    public static StatusDeletionNotice createStatusDeletionNotice(JSONObject json) throws JSONException {
        //{"delete":{"status":{"id":4821647803,"user_id":16346228}}}
        StatusDeletionNotice result=new StatusDeletionNotice();
        JSONObject status = json.getJSONObject("delete").getJSONObject("status");
        result.setStatusId(ParseUtil.getLong("id", status));
        result.setUserId(ParseUtil.getInt("user_id", status));
        return result;
    }

    public static int createTrackLimitationNotice(JSONObject json) throws JSONException {
        return ParseUtil.getInt("track", json.getJSONObject("limit"));
    }
}
