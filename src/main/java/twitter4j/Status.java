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

import java.io.Serializable;
import java.util.Date;

/**
 * A data class representing one single status of a user.
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class Status  implements TwitterResponse {

    private Date createdAt;
    private long id;
    private String text;
    private String source;
    private boolean truncated;
    private boolean favorited;
    private GeoLocation geoLocation = null;
    private InReplyTo inReplyTo;

    private Status retweetedStatus;
    private static final long serialVersionUID = 1608000492860584608L;
    private RateLimitStatus rateLimitStatus;

    public Status()
    {}

    /**
     * Return the created_at
     *
     * @return created_at
     * @since Twitter4J 1.1.0
     */

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the id of the status
     *
     * @return the id
     */
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns the text of the status
     *
     * @return the text
     */
    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the source
     *
     * @return the source
     * @since Twitter4J 1.0.4
     */
    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Test if the status is truncated
     *
     * @return true if truncated
     * @since Twitter4J 1.0.4
     */
    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public InReplyTo getInReplyTo() {
        return inReplyTo;
    }

    public void setInReplyTo(InReplyTo inReplyTo) {
        this.inReplyTo = inReplyTo;
    }


    /**
     * Returns The location that this tweet refers to if available.
     * @return returns The location that this tweet refers to if available (can be null)
     * @since Twitter4J 2.1.0
     * @return the GeoLocation or null if no location is available
     */
    public GeoLocation getGeoLocation(){
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    /**
     * Test if the status is favorited
     *
     * @return true if favorited
     * @since Twitter4J 1.0.4
     */
    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    private User user = null;

    /**
     * Return the user
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @since Twitter4J 2.0.10
     * @return true if this is a retweet.
     */
    public boolean isRetweet(){
        return null != retweetedStatus;
    }

    public boolean isInReply()
    {
        return inReplyTo != null;
    }

    /**
     *
     * @since Twitter4J 2.1.0
     * @return the retweeted status or null if this isn't a retweet.
     */
    public Status getRetweetedStatus() {
        return retweetedStatus;
    }

    public void setRetweetedStatus(Status retweetedStatus) {
        this.retweetedStatus = retweetedStatus;
    }



    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return obj instanceof Status && ((Status) obj).id == this.id;
    }

    @Override
    public String toString() {
        return "Status{" +
                "createdAt=" + createdAt +
                ", id=" + id +
                ", text='" + text + '\'' +
                ", source='" + source + '\'' +
                ", truncated=" + truncated +
                ", inReplyTo=" + inReplyTo +
                ", favorited=" + favorited +
                ", geoLocation=" + geoLocation +
                ", retweetedStatus=" + retweetedStatus +
                ", user=" + user +
                ", rateLimitStatus=" + rateLimitStatus +
                '}';
    }

    public RateLimitStatus getRateLimitStatus() {
        return rateLimitStatus;
    }

    public void setRateLimitStatus(RateLimitStatus rateLimitStatus) {
        this.rateLimitStatus = rateLimitStatus;
    }

    public static class InReplyTo
        implements Serializable
    {
        private long statusId;
        private int userId;
        private String userScreenName;
        private static final long serialVersionUID = 6770645822342779430L;

        public long getStatusId() {
            return statusId;
        }

        public void setStatusId(long statusId) {
            this.statusId = statusId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserScreenName() {
            return userScreenName;
        }

        public void setUserScreenName(String userScreenName) {
            this.userScreenName = userScreenName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InReplyTo inReplyTo = (InReplyTo) o;

            if (statusId != inReplyTo.statusId) return false;
            if (userId != inReplyTo.userId) return false;
            if (userScreenName != null ? !userScreenName.equals(inReplyTo.userScreenName) : inReplyTo.userScreenName != null)
                return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) (statusId ^ (statusId >>> 32));
            result = 31 * result + userId;
            result = 31 * result + (userScreenName != null ? userScreenName.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "InReplyTo{" +
                    "statusId=" + statusId +
                    ", userId=" + userId +
                    ", userScreenName='" + userScreenName + '\'' +
                    '}';
        }
    }
}
