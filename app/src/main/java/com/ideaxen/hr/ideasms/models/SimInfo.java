package com.ideaxen.hr.ideasms.models;

public class SimInfo {
    private int id_;
    private String operatorName;
    private String icc_id;
    private int slot;

    public SimInfo(int id_, String operatorName, String icc_id, int slot) {
        this.id_ = id_;
        this.operatorName = operatorName;
        this.icc_id = icc_id;
        this.slot = slot;
    }

    public int getId_() {
        return id_;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getIcc_id() {
        return icc_id;
    }

    public int getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        return "SimInfo{" +
                "id_=" + id_ +
                ", operatorName='" + operatorName + '\'' +
                ", icc_id='" + icc_id + '\'' +
                ", slot=" + slot +
                '}';
    }
}
