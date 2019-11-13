package com.telekom.m2m.cot.restsdk.retentionrule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.StreamSupport;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.telekom.m2m.cot.restsdk.CloudOfThingsRestClient;
import com.telekom.m2m.cot.restsdk.measurement.Measurement;
import com.telekom.m2m.cot.restsdk.util.ExtensibleObject;
import com.telekom.m2m.cot.restsdk.util.Filter;
import com.telekom.m2m.cot.restsdk.util.IterableObjectPagination;
import com.telekom.m2m.cot.restsdk.util.ObjectMapper;

public class RetentionRuleCollection extends IterableObjectPagination<RetentionRule> {

    private static final String COLLECTION_CONTENT_TYPE = "application/vnd.com.nsn.cumulocity.retentionRuleCollection+json;charset=UTF-8;ver=0.9";
    private static final String COLLECTION_ELEMENT_NAME = "retentionRules";

    /**
     * Creates a RetentionRuleCollection.
     *
     * Use {@link RetentionRuleApi} to get RetentionRuleCollection.
     *
     * @param cloudOfThingsRestClient the necessary REST client to send requests to the CoT.
     * @param relativeApiUrl          relative url of the REST API without leading slash.
     * @param gson                    the necessary json De-/serializer.
     * @param filterBuilder           the build criteria or null if all items should be retrieved.
     */
    RetentionRuleCollection(
        final CloudOfThingsRestClient cloudOfThingsRestClient,
        final String relativeApiUrl,
        final Gson gson,
        final Filter.FilterBuilder filterBuilder
    ) {
        super(
                new ObjectMapper<RetentionRule>() {
                    @Override
                    public RetentionRule apply(JsonElement jsonElement) {
                        return new RetentionRule(gson.fromJson(jsonElement, ExtensibleObject.class));
                    }
                },

//            ruleJson ->  new RetentionRule(gson.fromJson(ruleJson, ExtensibleObject.class)),
            cloudOfThingsRestClient,
            relativeApiUrl,
            gson,
            COLLECTION_CONTENT_TYPE,
            COLLECTION_ELEMENT_NAME,
            filterBuilder
        );
    }


    /**
     * Retrieves the current page.
     * <p>
     * Retrieves the rules influenced by filters set in construction.
     *
     * @return array of found rules
     */
    public RetentionRule[] getRetentionRules() {
        final JsonArray jsonRules = getJsonArray();

        if (jsonRules == null) return new RetentionRule[0];

        Iterator<JsonElement> it = jsonRules.iterator();
        final ArrayList<RetentionRule> al = new ArrayList<>();
        while (it.hasNext()) {
            al.add( objectMapper.apply(it.next()) );
        }

        RetentionRule[] res = new RetentionRule[al.size()];
        return al.toArray(res);


//        return (jsonRules == null) ? new RetentionRule[0] : StreamSupport.stream(jsonRules.spliterator(), false).
//                map(objectMapper).
//                toArray(RetentionRule[]::new);
    }

}
