package com.core.util;

import android.content.Context;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 * @author bin.teng
 */
public class CellIDInfoUtil {
	private TelephonyManager manager;

	private GsmCellLocation gsm;

	private CdmaCellLocation cdma;

	int lac;

	private int networkType = -1;

	String current_ci, mcc, mnc;

	private ArrayList<CellIDInfo> CellID;

	public CellIDInfoUtil() {
	}

	public ArrayList<CellIDInfo> getCellIDInfo(Context context, boolean readSignalState) throws Exception {
		manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		CellID = new ArrayList<CellIDInfo>();
		CellIDInfo currentCell = new CellIDInfo();
		int type = manager.getPhoneType();
		networkType = manager.getNetworkType();

		if (type == TelephonyManager.PHONE_TYPE_GSM) {
			gsm = ((GsmCellLocation) manager.getCellLocation());
			if (gsm == null) return null;
			try {
				lac = gsm.getLac();
				mcc = manager.getNetworkOperator().substring(0, 3);
				mnc = manager.getNetworkOperator().substring(3, 5);

				if (!readSignalState) return null;
			} catch (Exception e) {
				Logger.e(manager.getNetworkOperator());
				return null;
			}
			currentCell.cellId = gsm.getCid();
			currentCell.mobileCountryCode = mcc;
			currentCell.mobileNetworkCode = mnc;
			currentCell.locationAreaCode = lac;
			currentCell.radioType = "gsm";
			currentCell.signal_strength = -80;
			CellID.add(currentCell);

			List<NeighboringCellInfo> list = manager.getNeighboringCellInfo();
			int size = list.size();
			for (int i = 0; i < size; i++) {
				if (list.get(i).getCid() > 0) {
					CellIDInfo info = new CellIDInfo();
					info.cellId = list.get(i).getCid();
					info.mobileCountryCode = mcc;
					info.mobileNetworkCode = mnc;
					info.locationAreaCode = list.get(i).getLac();
					info.signal_strength = calcDMB(list.get(i).getRssi());
					CellID.add(info);
				}
			}
			return CellID;

		} else if (type == TelephonyManager.PHONE_TYPE_CDMA) {
			if (!readSignalState) return null;
			cdma = ((CdmaCellLocation) manager.getCellLocation());
			if (cdma == null) return null;

			// if ("460".equals(manager.getSimOperator().substring(0, 3)))
			// return null;

			int sid = cdma.getSystemId();
			int bid = cdma.getBaseStationId();
			int nid = cdma.getNetworkId();

			currentCell.cellId = bid;
			currentCell.locationAreaCode = nid;
			currentCell.mobileNetworkCode = String.valueOf(sid);
			String str = manager.getSimOperator();
			if (str.length() >= 3) {
				currentCell.mobileCountryCode = manager.getSimOperator().substring(0, 3);
			}
			currentCell.radioType = "cdma";
			currentCell.signal_strength = -80;
			CellID.add(currentCell);

			return CellID;
		}
		return null;
	}

	private int calcDMB(int signalStrength) {
		int dBm = -1;
		int asu = (signalStrength == 99 ? -1 : signalStrength);
		if (asu != -1) {
			dBm = -113 + 2 * asu;
		}
		return dBm;
	}

	public int getNetworkType(Context context) {
		if (networkType == -1) try {
			getCellIDInfo(context, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return networkType;
	}

	public String getMnc(Context context) {
		if (null == mnc || "".equalsIgnoreCase(mnc)) try {
			getCellIDInfo(context, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mnc;
	}

}

class CellIDInfo {

	public int cellId;
	public String mobileCountryCode;
	public String mobileNetworkCode;
	public int locationAreaCode;
	public String radioType;
	public int signal_strength;

	public CellIDInfo() {
	}
}