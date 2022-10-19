package com.xiaobo.smartcalendar.Model.Contradiction;

import com.xiaobo.smartcalendar.Model.Events.MyEvent;

public class MyIdentify {


    public enum CTC{
        ITI,
        ITI_B1, ITI_B2, ITI_B3,
        ITI_M1, ITI_M2, ITI_M3,
        DTI_O1, DTI_O2, DTI_O3,
        DTI_S1, DTI_S2, DTI_S3,
        DTI_E1, DTI_E2, DTI_E3,
        DTI_C1, DTI_C2, DTI_C3,

        WithoutInconsistency    
    }
    static CTC calCTC(MyEvent e1, MyEvent e2) {
        long event_1_commutingTime = e1.getCommutingTime() == -1 ? 0 : e1.getCommutingTime() * 1000;
        long event_1_startpoint = e1.getmDateOfEvent().getDate().getTime() - event_1_commutingTime;
        long event_1_endpoint = e1.getmDateOfEvent().getDate().getTime() + e1.getmDateOfEvent().getDuration();
        long event_2_commutingTime = e2.getCommutingTime() == -1 ? 0 : e2.getCommutingTime() * 1000;
        long event_2_startpoint = e2.getmDateOfEvent().getDate().getTime() - event_2_commutingTime;
        long event_2_endpoint = e2.getmDateOfEvent().getDate().getTime() + e2.getmDateOfEvent().getDuration();

        CTC mCTC = CTC.WithoutInconsistency;
        
        
        
        if (event_1_endpoint < event_2_startpoint + event_2_commutingTime) {
            
            
            
            if (event_1_endpoint <= event_2_startpoint) {
                mCTC = CTC.WithoutInconsistency;
            }
            
            else {
                
                
                if (event_1_endpoint < event_2_startpoint + event_2_commutingTime) {
                    
                    
                    if (event_1_startpoint < event_2_startpoint) {
                        mCTC = CTC.ITI_B1;
                    }
                    
                    
                    else if (event_1_startpoint == event_2_startpoint) {
                        mCTC = CTC.ITI_B2;
                    }
                    
                    
                    else if (event_1_startpoint > event_2_startpoint) {
                        mCTC = CTC.ITI_B3;
                    }
                }

                
                
                
                else {
                    
                    
                    if (event_1_startpoint < event_2_startpoint) {
                        mCTC = CTC.ITI_M1;
                    }
                    
                    
                    else if (event_1_startpoint == event_2_startpoint) {
                        mCTC = CTC.ITI_M2;
                    }
                    
                    
                    else if (event_1_startpoint > event_2_startpoint) {
                        mCTC = CTC.ITI_M3;
                    }
                }

            }

        }
        
        else {

            if (event_1_startpoint < event_2_startpoint && event_1_endpoint > event_2_startpoint) {
                
                
                if (event_1_endpoint < event_2_endpoint) {
                    mCTC = CTC.DTI_O1;
                }
                
                
                else if (event_1_endpoint == event_2_endpoint) {
                    mCTC = CTC.DTI_O2;
                }
                
                
                else if (event_1_endpoint > event_2_endpoint) {
                    mCTC = CTC.DTI_O3;
                }
            }
            else if (event_1_startpoint == event_2_startpoint) {
                
                
                if (event_1_endpoint > event_2_endpoint) {
                    mCTC = CTC.DTI_S1;
                }
                
                
                else if (event_1_endpoint == event_2_endpoint) {
                    mCTC = CTC.DTI_S2;
                }
                
                
                else if (event_1_endpoint < event_2_endpoint) {
                    mCTC = CTC.DTI_S3;
                }
            }

            else if (event_1_startpoint > event_2_startpoint && event_1_startpoint == event_2_startpoint + event_2_commutingTime) {
                
                
                if (event_1_endpoint > event_2_endpoint) {
                    mCTC = CTC.DTI_E1;
                }
                
                
                else if (event_1_endpoint == event_2_endpoint) {
                    mCTC = CTC.DTI_E2;
                }
                
                
                else if (event_1_endpoint < event_2_endpoint) {
                    mCTC = CTC.DTI_E3;
                }
            }

            else if (event_1_startpoint > event_2_startpoint && event_1_startpoint < event_2_startpoint + event_2_commutingTime) {
                
                
                if (event_1_endpoint > event_2_endpoint){
                    mCTC = CTC.DTI_C1;
                }
                
                
                else if (event_1_endpoint == event_2_endpoint) {
                    mCTC = CTC.DTI_C2;
                }
                
                
                else if (event_1_endpoint < event_2_endpoint) {
                    mCTC = CTC.DTI_C3;
                }
            }

        }
        return mCTC;
    }

    public static CTC getCTCFromName(String s) {
        switch (s) {
            case "ITI":
                return CTC.ITI;
            case "ITI_B1":
                return CTC.ITI_B1;
            case "ITI_B2":
                return CTC.ITI_B2;
            case "ITI_B3":
                return CTC.ITI_B3;
            case "ITI_M1":
                return CTC.ITI_M1;
            case "ITI_M2":
                return CTC.ITI_M2;
            case "ITI_M3":
                return CTC.ITI_M3;
            case "DTI_O1":
                return CTC.DTI_O1;
            case "DTI_O2":
                return CTC.DTI_O2;
            case "DTI_O3":
                return CTC.DTI_O3;
            case "DTI_S1":
                return CTC.DTI_S1;
            case "DTI_S2":
                return CTC.DTI_S2;
            case "DTI_S3":
                return CTC.DTI_S3;
            case "DTI_E1":
                return CTC.DTI_E1;
            case "DTI_E2":
                return CTC.DTI_E2;
            case "DTI_E3":
                return CTC.DTI_E3;
            case "DTI_C1":
                return CTC.DTI_C1;
            case "DTI_C2":
                return CTC.DTI_C2;
            case "DTI_C3":
                return CTC.DTI_C3;
            case "WithoutInconsistency":
                return CTC.WithoutInconsistency;

            default:
                return CTC.WithoutInconsistency;
        }
    }
}
