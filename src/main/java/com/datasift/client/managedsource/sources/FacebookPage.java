package com.datasift.client.managedsource.sources;

import com.datasift.client.DataSiftConfig;

/**
 * @author Courtney Robinson <courtney.robinson@datasift.com>
 */
public class FacebookPage extends BaseSource<FacebookPage> {

    public FacebookPage(DataSiftConfig config) {
        super(config);
    }

    public FacebookPage setParams(boolean enableLikes, boolean enableComments, boolean enablePostsByOthers) {
        return enableLikes(enableLikes)
                .enableComments(enableComments)
                .enablePostsByOthers(enablePostsByOthers);
    }

    /**
     * set to true, if you want to receive likes interactions.
     *
     * @return this
     */
    public FacebookPage enableLikes(boolean enabled) {
        return setParametersField("likes", enabled);
    }

    /**
     * set to true, if you want to receive interactions related to posts created on the monitored page by the users
     * who interact with that page, but do not administer it.
     *
     * @return this
     */
    public FacebookPage enablePostsByOthers(boolean enabled) {
        return setParametersField("posts_by_others", enabled);
    }

    /**
     * set to true, if you want to receive interactions that carry comments related to the posts published on the
     * monitored page.
     *
     * @return this
     */
    public FacebookPage enableComments(boolean enabled) {
        return setParametersField("comments", enabled);
    }

    /**
     * Adds information about a single facebook page
     *
     * @param id    the id of the facebook page
     * @param url   the facebook page's URL
     * @param title the page's title
     * @return this
     */
    public FacebookPage addPage(String id, String url, String title) {
        ResourceParams parameterSet = newResourceParams();
        parameterSet.set("id", id);
        parameterSet.set("url", url);
        parameterSet.set("title", title);
        return this;
    }

    /**
     * Adds a facebook OAuth token that will be used to fetch data from the configured pages
     *
     * @param oAuthAccessToken an oauth access token
     * @param name             a human friendly name for this auth token
     * @param expires          identity resource expiry date/time as a UTC timestamp, i.e. when the token expires
     * @return this
     */
    public FacebookPage addOAutToken(String oAuthAccessToken, String name, long expires) {
        if (oAuthAccessToken == null || oAuthAccessToken.isEmpty()) {
            throw new IllegalArgumentException("A valid OAutho token is required");
        }
        AuthParams parameterSet = newAuthParams(name, expires);
        parameterSet.set("value", oAuthAccessToken);
        return this;
    }
}
