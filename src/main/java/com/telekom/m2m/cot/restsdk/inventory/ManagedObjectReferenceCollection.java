package com.telekom.m2m.cot.restsdk.inventory;

import com.telekom.m2m.cot.restsdk.util.StringJoiner;

import java.util.List;

/**
 * Created by Patrick Steinert on 29.08.16.
 */
public class ManagedObjectReferenceCollection {

    private final List<ManagedObjectReference> mos;
    private String self;

    public ManagedObjectReferenceCollection(List<ManagedObjectReference> mos, String self) {
        this.mos = mos;
        this.self = self;
    }

    public Iterable<ManagedObjectReference> get() {
        return mos;
    }

    public String getSelf() {
        return self;
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ManagedObjectReferenceCollection[");

        final StringJoiner sj = new StringJoiner(", ");
        for (ManagedObjectReference moRef : mos) {
            sj.add(moRef.getSelf());
        }

        builder.append(sj.result());

        builder.append("]");
        return builder.toString();
    }

}
