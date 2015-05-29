package org.sphinx.pool;

import org.apache.commons.pool2.ObjectPool;
import org.sphinx.api.ISphinxClient;
import org.sphinx.api.SphinxClient;
import org.sphinx.api.SphinxException;
import org.sphinx.api.SphinxResult;

import java.util.Map;

/**
 * A proxy object for pooled {@link SphinxClient} instances.
 *
 * This class wraps the base SphinxClient object to ensure proper return to the pool when
 * the client is closed. The pool itself will handle the opening of socket connections to the Sphinx
 * server.
 *
 * Note that depending on the data source configuration, abandoned clients may not always be reclaimed
 * by the pool. Calling {@link #Close()} will ensure that resources held by this client are properly
 * disposed of and that the client is immediately returned to the pool.
 *
 * @see PooledSphinxDataSource#getSphinxClient()
 *
 * @author Brian Cowdery
 * @since 28-05-2015
 */
public class SphinxClientProxy implements ISphinxClient {

    private SphinxClient delegate;
    private final ObjectPool<SphinxClient> pool;


    SphinxClientProxy(SphinxClient delegate, ObjectPool<SphinxClient> pool) {
        this.delegate = delegate;
        this.pool = pool;
    }

    /**
     * Returns the underlying sphinx client instance wrapped by this proxy instance. This method will
     * throw an exception if the client has been closed and returned to the pool.
     *
     * @return wrapped client instance
     * @throws IllegalStateException if client is closed
     */
    public ISphinxClient getDelegate() {
        if (delegate == null) throw new IllegalStateException("The underlying sphinx client has been closed.");
        return delegate;
    }

    public String GetLastError() {
        return getDelegate().GetLastError();
    }

    public String GetLastWarning() {
        return getDelegate().GetLastWarning();
    }

    public boolean IsConnectError() {
        return getDelegate().IsConnectError();
    }

    public void SetServer(String host, int port) throws SphinxException {
        getDelegate().SetServer(host, port);
    }

    public void SetConnectTimeout(int timeout) {
        getDelegate().SetConnectTimeout(timeout);
    }

    public void SetLimits(int offset, int limit, int max, int cutoff) throws SphinxException {
        getDelegate().SetLimits(offset, limit, max, cutoff);
    }

    public void SetLimits(int offset, int limit, int max) throws SphinxException {
        getDelegate().SetLimits(offset, limit, max);
    }

    public void SetLimits(int offset, int limit) throws SphinxException {
        getDelegate().SetLimits(offset, limit);
    }

    public void SetMaxQueryTime(int maxTime) throws SphinxException {
        getDelegate().SetMaxQueryTime(maxTime);
    }

    public void SetMatchMode(int mode) throws SphinxException {
        getDelegate().SetMatchMode(mode);
    }

    public void SetRankingMode(int ranker, String rankexpr) throws SphinxException {
        getDelegate().SetRankingMode(ranker, rankexpr);
    }

    public void SetSortMode(int mode, String sortby) throws SphinxException {
        getDelegate().SetSortMode(mode, sortby);
    }

    public void SetWeights(int[] weights) throws SphinxException {
        getDelegate().SetWeights(weights);
    }

    public void SetFieldWeights(Map fieldWeights) throws SphinxException {
        getDelegate().SetFieldWeights(fieldWeights);
    }

    public void SetIndexWeights(Map indexWeights) throws SphinxException {
        getDelegate().SetIndexWeights(indexWeights);
    }

    public void SetIDRange(int min, int max) throws SphinxException {
        getDelegate().SetIDRange(min, max);
    }

    public void SetFilter(String attribute, int[] values, boolean exclude) throws SphinxException {
        getDelegate().SetFilter(attribute, values, exclude);
    }

    public void SetFilter(String attribute, long[] values, boolean exclude) throws SphinxException {
        getDelegate().SetFilter(attribute, values, exclude);
    }

    public void SetFilter(String attribute, int value, boolean exclude) throws SphinxException {
        getDelegate().SetFilter(attribute, value, exclude);
    }

    public void SetFilter(String attribute, long value, boolean exclude) throws SphinxException {
        getDelegate().SetFilter(attribute, value, exclude);
    }

    public void SetFilterRange(String attribute, long min, long max, boolean exclude) throws SphinxException {
        getDelegate().SetFilterRange(attribute, min, max, exclude);
    }

    public void SetFilterRange(String attribute, int min, int max, boolean exclude) throws SphinxException {
        getDelegate().SetFilterRange(attribute, min, max, exclude);
    }

    public void SetFilterFloatRange(String attribute, float min, float max, boolean exclude) throws SphinxException {
        getDelegate().SetFilterFloatRange(attribute, min, max, exclude);
    }

    public void SetGeoAnchor(String latitudeAttr, String longitudeAttr, float latitude, float longitude) throws SphinxException {
        getDelegate().SetGeoAnchor(latitudeAttr, longitudeAttr, latitude, longitude);
    }

    public void SetGroupBy(String attribute, int func, String groupsort) throws SphinxException {
        getDelegate().SetGroupBy(attribute, func, groupsort);
    }

    public void SetGroupBy(String attribute, int func) throws SphinxException {
        getDelegate().SetGroupBy(attribute, func);
    }

    public void SetGroupDistinct(String attribute) {
        getDelegate().SetGroupDistinct(attribute);
    }

    public void SetRetries(int count, int delay) throws SphinxException {
        getDelegate().SetRetries(count, delay);
    }

    public void SetRetries(int count) throws SphinxException {
        getDelegate().SetRetries(count);
    }

    public void SetOverride(String attrname, int attrtype, Map values) throws SphinxException {
        getDelegate().SetOverride(attrname, attrtype, values);
    }

    public void SetSelect(String select) throws SphinxException {
        getDelegate().SetSelect(select);
    }

    public void ResetFilters() {
        getDelegate().ResetFilters();
    }

    public void ResetGroupBy() {
        getDelegate().ResetGroupBy();
    }

    public void ResetOverrides() {
        getDelegate().ResetOverrides();
    }

    public SphinxResult Query(String query) throws SphinxException {
        return getDelegate().Query(query);
    }

    public SphinxResult Query(String query, String index) throws SphinxException {
        return getDelegate().Query(query, index);
    }

    public SphinxResult Query(String query, String index, String comment) throws SphinxException {
        return getDelegate().Query(query, index, comment);
    }

    public int AddQuery(String query, String index, String comment) throws SphinxException {
        return getDelegate().AddQuery(query, index, comment);
    }

    public SphinxResult[] RunQueries() throws SphinxException {
        return getDelegate().RunQueries();
    }

    public String[] BuildExcerpts(String[] docs, String index, String words, Map opts) throws SphinxException {
        return getDelegate().BuildExcerpts(docs, index, words, opts);
    }

    public int UpdateAttributes(String index, String[] attrs, long[][] values, boolean ignorenonexistent) throws SphinxException {
        return getDelegate().UpdateAttributes(index, attrs, values, ignorenonexistent);
    }

    public int UpdateAttributesMVA(String index, long docid, String[] attrs, int[][] values, boolean ignorenonexistent) throws SphinxException {
        return getDelegate().UpdateAttributesMVA(index, docid, attrs, values, ignorenonexistent);
    }

    public int UpdateAttributes(String index, String[] attrs, long[][] values) throws SphinxException {
        return getDelegate().UpdateAttributes(index, attrs, values);
    }

    public int UpdateAttributesMVA(String index, long docid, String[] attrs, int[][] values) throws SphinxException {
        return getDelegate().UpdateAttributesMVA(index, docid, attrs, values);
    }

    public Map[] BuildKeywords(String query, String index, boolean hits) throws SphinxException {
        return getDelegate().BuildKeywords(query, index, hits);
    }

    public int FlushAttributes() throws SphinxException {
        return getDelegate().FlushAttributes();
    }

    /**
     * Return the client to the pool and close the socket connection. Once the client has been
     * closed you must retrieve a new instance from the pool.
     *
     * @return always returns true.
     */
    public boolean Close() {
        try {
            // the sphinx socket connection is closed on return by the
            // PooledSphinxClientFactory#passivateObject() method
            pool.returnObject(delegate);
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while returning sphinx client to the pool", e);
        }

        delegate = null;
        return true;
    }
}
