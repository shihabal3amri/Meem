package com.meem;

public class MyServices {
    private String serviceName;
    private String serviceId;
    private MyServices en;
    private MyServices ar;

    public MyServices getEn() {
        return en;
    }

    public void setEn(MyServices en) {
        this.en = en;
    }

    public MyServices getAr() {
        return ar;
    }

    public void setAr(MyServices ar) {
        this.ar = ar;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
