



package com.solar.rfid.model;

public class PanelData {
    



    private int staticId;
    private String id;
    private String pmax;
    private String voc;
    private String isc;
    private String eff;
    private String bin;
    private String ff;
    private String date;
    private String cellManufacturingDate;
    private double ipm;
    private double vpm;
    private String status;

    // ✅ ADDED: TID field (DB column: epc)
    private String tid;

    // ── Getters ──────────────────────────────────────────────
    public String getId()    { return id   != null ? id   : ""; }
    public String getPmax()  { return pmax != null ? pmax : ""; }
    public String getVoc()   { return voc  != null ? voc  : ""; }
    public String getIsc()   { return isc  != null ? isc  : ""; }
    public String getEff()   { return eff  != null ? eff  : ""; }
    public String getFf()   { return ff  != null ? ff  : ""; }
    public String getBin()   { return bin  != null ? bin  : ""; }
    public String getDate()  { return date != null ? date : ""; }
    public String getCellManufacturingDate() {
        return cellManufacturingDate != null ? cellManufacturingDate : "";
    }
    public int getStaticId() { return staticId; }
    public double getIpm()    { return ipm; }
    public double getVpm()    { return vpm; }
    public String getStatus() { return status != null ? status : ""; }
    // ✅ ADDED
    public String getTid()    { return tid != null ? tid : ""; }

    // ── Setters ──────────────────────────────────────────────
    public void setId(String id)         { this.id = id; }
    public void setPmax(String pmax)     { this.pmax = pmax; }
    public void setVoc(String voc)       { this.voc = voc; }
    public void setIsc(String isc)       { this.isc = isc; }
    public void setEff(String eff)       { this.eff = eff; }
    public void setFf(String ff)         { this.ff = ff; }
    public void setBin(String bin)       { this.bin = bin; }
    public void setDate(String date)     { this.date = date; }
    public void setCellManufacturingDate(String cellManufacturingDate) {
        this.cellManufacturingDate = cellManufacturingDate;
    }
    public void setIpm(double ipm)       { this.ipm = ipm; }
    public void setVpm(double vpm)       { this.vpm = vpm; }
    public void setStatus(String status) { this.status = status; }
    // ✅ ADDED
    public void setTid(String tid)       { this.tid = tid; }
    public void setStaticId(int staticId) { this.staticId = staticId; }
}