package com.mytian.lb.event;

/**
 * Created by bin on 15/7/23.
 */
public class AgreementStateEventType {

    public final static int AGREEMENT_END = 0;
    public final static int AGREEMENT_ING = AGREEMENT_END+1;
    public int state;
    public String babyUid;

    public AgreementStateEventType(String babyUid, int state) {
        this.babyUid = babyUid;
        this.state = state;
    }
}
