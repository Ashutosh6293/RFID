

package com.solar.rfid.model;

public class PanelData {

    private String id;
    private String pmax;
    private String voc;
    private String isc;
    private String eff;
    private String bin;
    private String date;

    private double ipm;
    private double vpm;

    public String getId() { return id; }
    public String getPmax() { return pmax; }
    public String getVoc() { return voc; }
    public String getIsc() { return isc; }
    public String getEff() { return eff; }
    public String getBin() { return bin; }
    public String getDate() { return date; }

    public double getIpm() { return ipm; }
    public double getVpm() { return vpm; }

    public void setId(String id) { this.id = id; }
    public void setPmax(String pmax) { this.pmax = pmax; }
    public void setVoc(String voc) { this.voc = voc; }
    public void setIsc(String isc) { this.isc = isc; }
    public void setEff(String eff) { this.eff = eff; }
    public void setBin(String bin) { this.bin = bin; }
    public void setDate(String date) { this.date = date; }

    public void setIpm(double ipm) { this.ipm = ipm; }
    public void setVpm(double vpm) { this.vpm = vpm; }
}




