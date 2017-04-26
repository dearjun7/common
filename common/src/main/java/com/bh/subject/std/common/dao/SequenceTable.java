package com.bh.subject.std.common.dao;

public enum SequenceTable {
    MODELTYPE("modelType"),
    MODELGROUP("modelGroup"),
    CUSTATTRTMPLT("custattrTmplt"),
    CUSTATTR("custattr"),
    PROC("proc"),
    WORKREQUEST("workRequest"),
    OBJTYPE("objType"),
    OBJ("obj"),
    ATTACH("attach"),
    ISOINTRO("isoIntro"),
    BULT("bult"),
    TAG("tag");

    private String value;

    SequenceTable(final String value) {
        this.value = value;
    }

    private String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
