package org.sphinx.api;

import java.util.Map;

/**
 * ISphinxClient
 *
 * @author Brian Cowdery
 * @since 28-05-2015
 */
public interface ISphinxClient {

	String GetLastError();

	String GetLastWarning();

	boolean IsConnectError();

	void SetServer(String host, int port) throws SphinxException;

	void SetConnectTimeout(int timeout);

	void SetLimits(int offset, int limit, int max, int cutoff) throws SphinxException;

	void SetLimits(int offset, int limit, int max) throws SphinxException;

	void SetLimits(int offset, int limit) throws SphinxException;

	void SetMaxQueryTime(int maxTime) throws SphinxException;

	void SetMatchMode(int mode) throws SphinxException;

	void SetRankingMode(int ranker, String rankexpr) throws SphinxException;

	void SetSortMode(int mode, String sortby) throws SphinxException;

	void SetWeights(int[] weights) throws SphinxException;

	void SetFieldWeights (Map fieldWeights) throws SphinxException;

	void SetIndexWeights(Map indexWeights) throws SphinxException;

	void SetIDRange(int min, int max) throws SphinxException;

	void SetFilter(String attribute, int[] values, boolean exclude) throws SphinxException;

	void SetFilter(String attribute, long[] values, boolean exclude) throws SphinxException;

	void SetFilter(String attribute, int value, boolean exclude) throws SphinxException;

	void SetFilter(String attribute, long value, boolean exclude) throws SphinxException;

	void SetFilterRange(String attribute, long min, long max, boolean exclude) throws SphinxException;

	void SetFilterRange(String attribute, int min, int max, boolean exclude) throws SphinxException;

	void SetFilterFloatRange(String attribute, float min, float max, boolean exclude) throws SphinxException;

	void SetGeoAnchor(String latitudeAttr, String longitudeAttr, float latitude, float longitude) throws SphinxException;

	void SetGroupBy(String attribute, int func, String groupsort) throws SphinxException;

	void SetGroupBy(String attribute, int func) throws SphinxException;

	void SetGroupDistinct(String attribute);

	void SetRetries(int count, int delay) throws SphinxException;

	void SetRetries(int count) throws SphinxException;

	void SetOverride(String attrname, int attrtype, Map values) throws SphinxException;

	void SetSelect(String select) throws SphinxException;

	void ResetFilters();

	void ResetGroupBy();

	void ResetOverrides();

	SphinxResult Query(String query) throws SphinxException;

	SphinxResult Query(String query, String index) throws SphinxException;

	SphinxResult Query(String query, String index, String comment) throws SphinxException;

	int AddQuery(String query, String index, String comment) throws SphinxException;

	SphinxResult[] RunQueries() throws SphinxException;

	String[] BuildExcerpts(String[] docs, String index, String words, Map opts) throws SphinxException;

	int UpdateAttributes(String index, String[] attrs, long[][] values, boolean ignorenonexistent) throws SphinxException;

	int UpdateAttributesMVA(String index, long docid, String[] attrs, int[][] values, boolean ignorenonexistent) throws SphinxException;

	int UpdateAttributes(String index, String[] attrs, long[][] values) throws SphinxException;

	int UpdateAttributesMVA(String index, long docid, String[] attrs, int[][] values) throws SphinxException;

	Map[] BuildKeywords(String query, String index, boolean hits) throws SphinxException;

	int FlushAttributes() throws SphinxException;

	boolean Close();
}
