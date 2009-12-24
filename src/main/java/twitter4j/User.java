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

import twitter4j.http.Response;
import twitter4j.org.json.JSONArray;
import twitter4j.org.json.JSONException;
import twitter4j.org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import static twitter4j.ParseUtil.*;

/**
 * A data class representing Basic user information element
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @see <a href="http://apiwiki.twitter.com/REST+API+Documentation#Basicuserinformationelement">REST API Documentation - Basic user information element</a>
 */
public class User extends TwitterResponseImpl implements java.io.Serializable {

    private int id;
    private String name;
    private String screenName;
    private String location;
    private String description;
    private String url;
    private boolean isProtected;
    private int followersCount;

    private Status status;

    private Profile profile;
    private int friendsCount;
    private Date createdAt;
    private int favouritesCount;
    private int utcOffset;
    private String timeZone;
    private int statusesCount;
    private boolean isGeoEnabled;
    private boolean isVerified;
    private static final long serialVersionUID = -6345893237975349030L;

	public static User createFromResponseHeader(Response res) throws TwitterException
	{
        RateLimitStatus rateLimit = RateLimitStatus.createFromResponseHeader(res);
        User result = createFromJSONObject(res.asJSONObject());
        result.setRateLimitStatus(rateLimit);
		return result;
	}

	public static User createFromJSONObject(JSONObject json) throws TwitterException {
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

            Profile profile = new Profile();

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
                result.setStatus(Status.createFromJSONObject(status));
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

	public User()
	{}


    /**
     * Returns the id of the user
     *
     * @return the id of the user
     */
    public int getId() {
        return id;
    }

	public void setId(int id)
	{
		this.id = id;
	}

	/**
     * Returns the name of the user
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

	public void setName(String name)
	{
		this.name = name;
	}

	/**
     * Returns the screen name of the user
     *
     * @return the screen name of the user
     */
    public String getScreenName() {
        return screenName;
    }

	public void setScreenName(String screenName)
	{
		this.screenName = screenName;
	}

	/**
     * Returns the location of the user
     *
     * @return the location of the user
     */
    public String getLocation() {
        return location;
    }

	public void setLocation(String location)
	{
		this.location = location;
	}

	/**
     * Returns the description of the user
     *
     * @return the description of the user
     */
    public String getDescription() {
        return description;
    }

	public void setDescription(String description)
	{
		this.description = description;
	}


    /**
     * Returns the url of the user
     *
     * @return the url of the user
     */
    public URL getURL() {
        try {
            return new URL(url);
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public void setURL(String url) {
        this.url = url;
    }

    /**
     * Test if the user status is protected
     *
     * @return true if the user status is protected
     */
    public boolean isProtected() {
        return isProtected;
    }

    public void setProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    /**
     * Returns the number of followers
     *
     * @return the number of followers
     * @since Twitter4J 1.0.4
     */
    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    /*package*/ static PagableResponseList<User> createPagableUserList(Response res) throws TwitterException {
        try {
            JSONObject json = res.asJSONObject();
            JSONArray list = json.getJSONArray("users");
            int size = list.length();
            PagableResponseList<User> users =
                    new PagableResponseList<User>(size, json, res);
            for (int i = 0; i < size; i++) {
                //users.add(new User(list.getJSONObject(i)));
                users.add(createFromJSONObject(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }
    /*package*/ static ResponseList<User> createUserList(Response res) throws TwitterException {
        try {
            JSONArray list = res.asJSONArray();
            int size = list.length();
            ResponseList<User> users =
                    new ResponseList<User>(size, res);
            for (int i = 0; i < size; i++) {
                //users.add(new User(list.getJSONObject(i)));
                users.add(createFromJSONObject(list.getJSONObject(i)));
            }
            return users;
        } catch (JSONException jsone) {
            throw new TwitterException(jsone);
        } catch (TwitterException te) {
            throw te;
        }
    }

    /**
     *
     * @return the Status or null if the user is protected
     */
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(int friendsCount) {
        this.friendsCount = friendsCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public int getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(int utcOffset) {
        this.utcOffset = utcOffset;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public void setStatusesCount(int statusesCount) {
        this.statusesCount = statusesCount;
    }

    /**
     * 
     * @return the user is enabling geo location
     * @since Twitter4J 2.0.10
     */
    public boolean isGeoEnabled() {
        return isGeoEnabled;
    }

    public void setGeoEnabled(boolean geoEnabled) {
        isGeoEnabled = geoEnabled;
    }

    /**
     *
     * @return returns true if the user is a verified celebrity
     * @since Twitter4J 2.0.10
     */
    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj instanceof User && ((User) obj).id == this.id;
    }

    @Override
    public String toString() {
        return "User{" +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", screenName='" + screenName + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", isProtected=" + isProtected +
                ", followersCount=" + followersCount +
                ", status=" + status +
                ", profile='" + profile + '\'' +
                ", friendsCount=" + friendsCount +
                ", createdAt=" + createdAt +
                ", favouritesCount=" + favouritesCount +
                ", utcOffset=" + utcOffset +
                ", timeZone='" + timeZone + '\'' +
                ", statusesCount=" + statusesCount +
                ", geoEnabled=" + isGeoEnabled +
                ", verified=" + isVerified +
                '}';
    }

    public static class Profile
        implements Serializable
    {
        private String textColor;
        private String linkColor;
        private String imageUrl;
        private String backgroundColor;
        private String backgroundImageUrl;
        private String backgroundTile;
        private String sidebarFillColor;
        private String sidebarBorderColor;
        private static final long serialVersionUID = -4426066477774540374L;

        public Profile() {
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getBackgroundImageUrl() {
            return backgroundImageUrl;
        }

        public void setBackgroundImageUrl(String backgroundImageUrl) {
            this.backgroundImageUrl = backgroundImageUrl;
        }

        public String getBackgroundTile() {
            return backgroundTile;
        }

        public void setBackgroundTile(String backgroundTile) {
            this.backgroundTile = backgroundTile;
        }

        public String getLinkColor() {
            return linkColor;
        }

        public void setLinkColor(String linkColor) {
            this.linkColor = linkColor;
        }

        public String getSidebarBorderColor() {
            return sidebarBorderColor;
        }

        public void setSidebarBorderColor(String sidebarBorderColor) {
            this.sidebarBorderColor = sidebarBorderColor;
        }

        public String getSidebarFillColor() {
            return sidebarFillColor;
        }

        public void setSidebarFillColor(String sidebarFillColor) {
            this.sidebarFillColor = sidebarFillColor;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Profile profile = (Profile) o;

            if (backgroundColor != null ? !backgroundColor.equals(profile.backgroundColor) : profile.backgroundColor != null)
                return false;
            if (backgroundImageUrl != null ? !backgroundImageUrl.equals(profile.backgroundImageUrl) : profile.backgroundImageUrl != null)
                return false;
            if (backgroundTile != null ? !backgroundTile.equals(profile.backgroundTile) : profile.backgroundTile != null)
                return false;
            if (imageUrl != null ? !imageUrl.equals(profile.imageUrl) : profile.imageUrl != null) return false;
            if (linkColor != null ? !linkColor.equals(profile.linkColor) : profile.linkColor != null) return false;
            if (sidebarBorderColor != null ? !sidebarBorderColor.equals(profile.sidebarBorderColor) : profile.sidebarBorderColor != null)
                return false;
            if (sidebarFillColor != null ? !sidebarFillColor.equals(profile.sidebarFillColor) : profile.sidebarFillColor != null)
                return false;
            if (textColor != null ? !textColor.equals(profile.textColor) : profile.textColor != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = textColor != null ? textColor.hashCode() : 0;
            result = 31 * result + (linkColor != null ? linkColor.hashCode() : 0);
            result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
            result = 31 * result + (backgroundColor != null ? backgroundColor.hashCode() : 0);
            result = 31 * result + (backgroundImageUrl != null ? backgroundImageUrl.hashCode() : 0);
            result = 31 * result + (backgroundTile != null ? backgroundTile.hashCode() : 0);
            result = 31 * result + (sidebarFillColor != null ? sidebarFillColor.hashCode() : 0);
            result = 31 * result + (sidebarBorderColor != null ? sidebarBorderColor.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Profile{" +
                    "backgroundColor='" + backgroundColor + '\'' +
                    ", textColor='" + textColor + '\'' +
                    ", linkColor='" + linkColor + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", backgroundImageUrl='" + backgroundImageUrl + '\'' +
                    ", backgroundTile='" + backgroundTile + '\'' +
                    ", sidebarFillColor='" + sidebarFillColor + '\'' +
                    ", sidebarBorderColor='" + sidebarBorderColor + '\'' +
                    '}';
        }
    }
}
