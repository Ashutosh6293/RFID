package com.solar.rfid.model;

public class PanelFullData {

    private PanelData panel;
    private StaticPanelData staticData;

    public PanelFullData(PanelData panel, StaticPanelData staticData) {
        this.panel = panel;
        this.staticData = staticData;
    }

    public PanelData getPanel() {
        return panel;
    }

    public StaticPanelData getStaticData() {
        return staticData;
    }
}