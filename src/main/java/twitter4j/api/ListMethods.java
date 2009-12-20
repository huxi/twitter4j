package twitter4j.api;

import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.UserList;

public interface ListMethods
{
	/**
	 * Creates a new list for the authenticated user.
	 * <br>This method calls http://api.twitter.com/1/user/lists.json
	 * @param listName The name of the list you are creating. Required.
	 * @param isPublicList set true if you wish to make a public list
	 * @param description The description of the list you are creating. Optional.
	 * @return the list that was created
	 * @throws twitter4j.TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-POST-lists">Twitter REST API Method: POST lists</a>
	 * @since Twitter4J 2.1.0
	 */
	UserList createUserList(String listName, boolean isPublicList, String description)
			throws TwitterException;

	/**
	 * Updates the specified list.
	 * <br>This method calls http://api.twitter.com/1/user/lists/id.json
	 * @param listId The id of the list to update.
	 * @param name What you'd like to change the list's name to.
	 * @param isPublicList Whether your list is public or private. Optional. Values can be public or private. Lists are public by default if no mode is specified.
	 * @param description What you'd like to change the list description to.
	 * @return the updated list
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-POST-lists-id">Twitter REST API Method: POST lists id</a>
	 * @since Twitter4J 2.1.0
	 */
	UserList updateUserList(int listId, String name, boolean isPublicList, String description)
			throws TwitterException;

	/**
	 * List the lists of the specified user. Private lists will be included if the authenticated users is the same as the user whose lists are being returned.
	 * <br>This method calls http://api.twitter.com/1/user/lists.json
	 * @param user The specified user
	 * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the list of lists
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-lists">Twitter REST API Method: GET lists</a>
	 * @since Twitter4J 2.1.0
	 */
	PagableResponseList<UserList> getUserLists(String user, long cursor)
			throws TwitterException;

	/**
	 * Show the specified list. Private lists will only be shown if the authenticated user owns the specified list.
	 * <br>This method calls http://api.twitter.com/1/user/lists/id.json
	 * @param user The name of the authenticated user creating the list
	 * @param id The id of the list to show
	 * @return the specified list
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-id">Twitter REST API Method: GET list id</a>
	 * @since Twitter4J 2.1.0
	 */
	UserList showUserList(String user, int id)
			throws TwitterException;

	/**
	 * Deletes the specified list. Must be owned by the authenticated user.
	 * <br>This method calls http://api.twitter.com/1/[user]/lists/[id].json
	 * @param listId The id of the list to delete
	 * @return the deleted list
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-DELETE-list-id">Twitter REST API Method: DELETE /:user/lists/:id</a>
	 * @since Twitter4J 2.1.0
	 */
	UserList deleteUserList(int listId)
			throws TwitterException;

	/**
	 * Show tweet timeline for members of the specified list.
	 * <br>http://api.twitter.com/1/user/lists/list_id/statuses.json
	 * @param user The name of the authenticated user deleting the list
	 * @param id The id of the list to delete
	 * @param paging controls pagination. Supports since_id, max_id, count and page parameters.
	 * @return list of statuses for members of the specified list
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-statuses">Twitter REST API Method: GET list statuses</a>
	 * @since Twitter4J 2.1.0
	 */
	ResponseList<Status> getUserListStatuses(String user, int id, Paging paging)
			throws TwitterException;

	/**
	 * List the lists the specified user has been added to.
	 * <br>This method calls http://api.twitter.com/1/user/lists/memberships.json
	 * @param user The specified user
	 * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the list of lists
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-memberships">Twitter REST API Method: GET /:user/lists/memberships</a>
	 * @since Twitter4J 2.1.0
	 */
	PagableResponseList<UserList> getUserListMemberships(String user, long cursor)
			throws TwitterException;

	/**
	 * List the lists the specified user follows.
	 * <br>This method calls http://api.twitter.com/1/[user]/lists/subscriptions.json
	 * @param user The specified user
	 * @param cursor Breaks the results into pages. A single page contains 20 lists. Provide a value of -1 to begin paging. Provide values as returned to in the response body's next_cursor and previous_cursor attributes to page back and forth in the list.
	 * @return the list of lists
	 * @throws TwitterException when Twitter service or network is unavailable
	 * @see <a href="http://apiwiki.twitter.com/Twitter-REST-API-Method%3A-GET-list-subscriptions">Twitter REST API Method: GET list subscriptions</a>
	 * @since Twitter4J 2.1.0
	 */
	PagableResponseList<UserList> getUserListSubscriptions(String user, long cursor)
			throws TwitterException;
}
